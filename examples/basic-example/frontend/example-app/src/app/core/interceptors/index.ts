import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AuthInterceptor } from './auth.interceptor';
import { CustomInterceptor } from './custom.interceptor';
import { ErrorInterceptor } from './error.interceptor';
import { SessionRecoveryInterceptor } from './session-recovery.interceptor';

export const HttpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi: true },

  {
    provide: HTTP_INTERCEPTORS,
    useClass: SessionRecoveryInterceptor,
    multi: true,
  },
];
