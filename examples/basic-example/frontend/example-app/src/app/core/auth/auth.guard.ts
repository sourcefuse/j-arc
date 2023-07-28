import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, catchError, concatMap, of } from 'rxjs';
import { AuthService } from 'src/app/shared/services';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService,
    private router: Router) {

  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (route.queryParamMap.keys.length > 0) {
      const code = route.queryParamMap.get('code');
      // do not call token api if redirection is from smartsheet
      if (code) {
        return this.authService.authorize(code)
          .pipe(concatMap(() => this.checkLogin(state.url)));
      }
    }
    return this.checkLogin(state.url);
  }

  checkLogin(url: string) {
    return this.authService
      .isLoggedIn()
      .pipe(
        catchError(() => {
          this.authService.redirectUrl = url;
          this.router.navigate(['/login']);
          return of({ isLoggedIn: false });
        }),
      )
      .pipe(
        concatMap(res => {
          const data: {
            isLoggedIn: boolean;
            url?: string;
            username?: string;
            isSuperAdmin?: boolean;
          } = res;

          if (!data.isLoggedIn) {
            this.router.navigate(['/login']);
            return of(false);
          } else if (data.isSuperAdmin) {
            this.router.navigateByUrl('/main');
            return of(false);
          } else {
            // do nothing
          }
          if (data.url && url === environment.homePath) {
            this.router.navigate([data.url], {
              queryParams: { lastAccessed: true },
            });
          } else {
            // do nothing
          }
          return of(true);
        }),
      );
  }
}
