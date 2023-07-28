import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpParameterCodec,
  HttpParams,
} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class CustomInterceptor implements HttpInterceptor {
  constructor() {
    // This is intentional
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler,
  ): Observable<HttpEvent<any>> {
    const params = new HttpParams({
      encoder: new CustomEncoder(),
      fromString: req.params.toString(),
    });
    return next.handle(req.clone({params}));
  }
}
class CustomEncoder implements HttpParameterCodec {
  encodeKey(key: string): string {
    return encodeURIComponent(key);
  }

  encodeValue(value: string): string {
    return encodeURIComponent(value);
  }

  decodeKey(key: string): string {
    return decodeURIComponent(key);
  }

  decodeValue(value: string): string {
    return decodeURIComponent(value);
  }
}
