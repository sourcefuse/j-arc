import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
} from '@angular/common/http';

import {Observable, throwError} from 'rxjs';

import {UserSessionStoreService as StoreService} from '../store/user-session-store.service';
import {Router} from '@angular/router';

export const AuthTokenSkipHeader = 'X-Skip-Auth-Token';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private readonly store: StoreService,
    private readonly router: Router,
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    if (req.headers.has(AuthTokenSkipHeader) || req.url.includes('i18n/')) {
      const headers = req.headers.delete(AuthTokenSkipHeader);
      return next.handle(req.clone({headers}));
    }
    const authToken = this.store.getAccessToken();
    if (authToken) {
      return next.handle(
        req.clone({setHeaders: {Authorization: `Bearer ${authToken}`}}),
      );
    } else {
      if (
        this.router.url !== '/' &&
        this.router.url !== '/login' &&
        this.router.url !== '/forgot-password' &&
        this.router.url.split('?')[0] !== '/signup'
      ) {
        this.router.navigate(['/']);
      }
      return throwError('Request forbidden ! No access token available.');
    }
  }
}
