import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpHandler, HttpInterceptor, HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class CredentialsInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // Aplica solo a llamadas que van a tu backend → /api/...
    const reqWithCreds = req.url.startsWith('/api/')
      ? req.clone({ withCredentials: true })
      : req;

    return next.handle(reqWithCreds);
  }
}
