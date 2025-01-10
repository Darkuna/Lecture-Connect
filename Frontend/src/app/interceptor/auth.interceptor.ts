import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Token aus localStorage oder sessionStorage abrufen
    const token = localStorage.getItem('jwt-token') || sessionStorage.getItem('jwt-token');

    if (token) {
      // Anfrage klonen und Authorization-Header hinzufügen
      const clonedRequest = req.clone({
        headers:
          req.headers
            .set('Authorization', `Bearer ${token}`)
            .set("Access-Control-Allow-Origin", "*")
            .set("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
            .set('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS'),
      });
      return next.handle(clonedRequest);
    }

    return next.handle(req);
  }
}
