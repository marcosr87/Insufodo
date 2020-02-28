import { Injectable } from '@angular/core'
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http'
import { Observable, throwError } from 'rxjs'
import { Router } from '@angular/router'
import { catchError } from 'rxjs/internal/operators/catchError'

@Injectable({
    providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {

    constructor(private router: Router) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const token = localStorage.getItem('token')
        let request = req

        if (token) {
            request = req.clone({
                setHeaders: { authorization: 'Bearer ' + token }
            })
        }

        return next.handle(request).pipe(
            catchError((err: HttpErrorResponse) => {
                if (err.status === 401) {
                    this.router.navigateByUrl('/login')
                }
                return throwError(err)
            })
        )
    }
}