import { HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subject, concatMap, flatMap, map, of, switchMap, take, tap, throwError } from 'rxjs';
import { AnyAdapter } from 'src/app/core/adatpers';
import { RoleType } from 'src/app/core/enums';
import { LoginModel, User } from 'src/app/core/models';
import { UserSessionStoreService } from 'src/app/core/store/user-session-store.service';
import { environment } from 'src/environments/environment';
import { Location } from '@angular/common';
import { LoginAdapter, SignUpAdapter, CurrentUserAdapter } from 'src/app/shared/adapters';
import { SignupCommand, GetCurrentUserCommand, KeycloakLoginCommand, GetTokenCommand, RefreshTokenCommand, LogoutCommand, VerifyTokenCommand } from 'src/app/shared/commands';
import { ApiService } from 'src/app/core/service/api.service';
import { AuthTokenSkipHeader } from 'src/app/core/interceptors/auth.interceptor';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  signUp(user: any) {
    const command = new SignupCommand(
      this.apiService,
      this.signUpAdapter,
    );

    command.parameters = {
      data: user,
    };

    return command.execute();
  }

  redirectUrl: string;

  private readonly authHeaders = new HttpHeaders().set(AuthTokenSkipHeader, '');
  tokenRefreshed = new Subject<void>();
  constructor(
    private readonly router: Router,
    private readonly store: UserSessionStoreService,
    private readonly apiService: ApiService,
    private readonly loginAdapter: LoginAdapter,
    private readonly signUpAdapter: SignUpAdapter,
    private readonly currentUserAdapter: CurrentUserAdapter,
    private readonly anyAdapter: AnyAdapter,
    private readonly location: Location,
  ) { }

  public isLoggedIn(): Observable<{
    isLoggedIn: boolean;
    url?: string;
    username?: string;
    isSuperAdmin?: boolean;
  }> {
    return this.currentUser().pipe(
      switchMap(user => {
        if (user && user.id && this.store.getAccessToken()) {
          if (user.roleType === RoleType.SuperAdmin) {
            return of({ isLoggedIn: true, isSuperAdmin: true });
          }
          return of({ isLoggedIn: true, url: environment.homePath, username: user.email });
        } else {
          return of({ isLoggedIn: false });
        }
      }),
    );
  }

  public currentUser(): Observable<User> {
    const user = this.loadUserFromStore();

    if (user) {
      return of(user);
    } else {
      return this.getCurrentUser();
    }
  }

  getCurrentUser() {
    const command: GetCurrentUserCommand<User> = new GetCurrentUserCommand(
      this.apiService,
      this.currentUserAdapter,
    );
    return command.execute().pipe(
      take(1),
      tap(user => {
        this.store.setUser(user);
      }),
    );
  }


  loadUserFromStore() {
    const user = this.store.getUser();
    if (user && user.id) {
      return user;
    }
    return null;
  }

  // // sonarignore:start
  // public login(username: string, password: string): Observable<any> {
  //   // sonarignore:end
  //   this.store.setUser({
  //     username,
  //   } as User);
  //   const command: LoginCommand<LoginModel> = new LoginCommand(
  //     this.apiService,
  //     this.loginAdapter,
  //   );
  //   command.parameters = {
  //     data: {
  //       username: username.toLowerCase(),
  //       password,
  //       clientId: environment.clientId,
  //       publicKey: environment.publicKey,
  //     },
  //     observe: 'response',
  //     headers: this.authHeaders,
  //   };
  //   return command.execute();
  // }

  // sonarignore:start
  public keycloakLogin(): Observable<any> {
    // sonarignore:end
    const command: KeycloakLoginCommand<LoginModel> = new KeycloakLoginCommand(
      this.apiService,
      this.loginAdapter,
    );
    command.parameters = {
      data: {
        clientId: environment.clientId,
        clientSecret: environment.publicKey,
        username: undefined,
        password: undefined
      },
      headers: this.authHeaders,
    };
    return command.execute();
  }

  public authorize(code: string, redirect = true): Observable<unknown> {
    const command: GetTokenCommand<unknown> = new GetTokenCommand(
      this.apiService,
      this.anyAdapter,
    );
    command.parameters = {
      data: {
        clientId: environment.clientId,
        code,
      },
      headers: this.authHeaders,
    };
    return command.execute().pipe(
      tap((response: { accessToken: string; refreshToken: string }) => {
        if (response.accessToken && response.refreshToken) {
          this.setTokens(response.accessToken, response.refreshToken);
          if (redirect) {
            let pathToRedirect = environment.homePath;
            if (!!this.store.getRedirectUrl()) {
              pathToRedirect = decodeURI(this.store.getRedirectUrl());
              const baseHref = (this.location as any)._baseHref || '';
              window.location.href = `${window.location.origin}${baseHref}${pathToRedirect}`;
            } else {
              this.router.navigateByUrl(pathToRedirect);
            }
          }
        }
      }),
    );
  }

  // sonarignore:start
  public refreshToken(): Observable<any> {
    // sonarignore:end
    const refreshToken = this.store.getRefreshToken();
    if (!refreshToken) {
      return of(false);
    }
    // sonarignore:start
    const command: RefreshTokenCommand<any> = new RefreshTokenCommand(
      this.apiService,
      this.anyAdapter,
    );
    // sonarignore:end
    command.parameters = {
      data: {
        refreshToken,
      },
    };
    return command
      .execute()
      .pipe(
        tap(
          response => {
            if (response.accessToken && response.refreshToken) {
              this.store.clearAll();
              this.setTokens(response.accessToken, response.refreshToken);
              this.tokenRefreshed.next();
            } else {
              this.logout().subscribe();
            }
          },
          err => {
            this._handleError(err);
          },
        ),
      )
      .pipe(flatMap(() => this.currentUser()));
  }

  // sonarignore:start
  public logout(): Observable<any> {
    // sonarignore:end
    const refreshToken = this.store.getRefreshToken();
    // sonarignore:start
    const command: LogoutCommand<any> = new LogoutCommand(
      this.apiService,
      this.anyAdapter,
    );
    // sonarignore:start
    command.parameters = {
      data: {
        refreshToken,
      },
    };
    return command.execute().pipe(
      tap(
        res => {
          this.store.clearAll();
          const baseHref = (this.location as any)._baseHref || '';
          window.location.href = `${baseHref}/login`;
        },
        err => {
          this._handleError(err);
        },
      ),
    );
  }

  pipeTokenRefresh(observable: Observable<any>) {
    return observable.pipe(
      concatMap(res => this.refreshToken().pipe(map(response => res))),
    );
  }

  verifyToken() {
    const command = new VerifyTokenCommand(this.apiService, this.anyAdapter);
    command.parameters = {
      headers: new HttpHeaders({
        'X-Skip-Error-Toast': '',
      }),
    };
    return command.execute();
  }

  // createExternalUser(user) {
  //   const command = new CreateExternalUserCommand(
  //     this.apiService,
  //     this.signUpAdapter,
  //   );

  //   command.parameters = {
  //     data: user,
  //   };

  //   return command.execute();
  // }

  hasAccessToken() {
    return !!this.store.getAccessToken();
  }


  setTokens(accessToken: string, refreshToken: string) {
    this.store.saveAccessToken(accessToken);
    this.store.saveRefreshToken(refreshToken);
  }

  private _handleError(error: HttpErrorResponse) {
    this.store.clearAll();
    const baseHref = (this.location as any)._baseHref || '';
    window.location.href = `${baseHref}/login`;
    return throwError(error.message);
  }

  loginViaSSO(): void {
    const keyclockForm = document.createElement('form');
    keyclockForm.method = 'POST';
    keyclockForm.enctype = 'application/json'
    keyclockForm.action = `${environment.authApiUrl}/keycloak/login`;
    keyclockForm.style.display = 'none';

    const clientId = document.createElement('input');
    clientId.type = 'hidden';
    clientId.name = 'clientId';
    clientId.value = environment.clientId;
    keyclockForm.appendChild(clientId);

    const clientSecret = document.createElement('input');
    clientSecret.type = 'hidden';
    clientSecret.name = 'clientSecret';
    clientSecret.value = environment.publicKey;
    keyclockForm.appendChild(clientSecret);
    document.body.appendChild(keyclockForm);
    keyclockForm.submit();
  }
}
