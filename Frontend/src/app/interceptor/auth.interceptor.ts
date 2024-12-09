import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Token aus localStorage oder sessionStorage abrufen
    const token = localStorage.getItem('jwt-token') || sessionStorage.getItem('jwt-token');

    if (token) {
      // Anfrage klonen und Authorization-Header hinzufügen
      const clonedRequest = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      console.log(req);
      return next.handle(clonedRequest);
    }

    return next.handle(req);
  }
}
