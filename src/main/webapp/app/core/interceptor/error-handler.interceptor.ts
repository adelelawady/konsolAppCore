import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpErrorResponse, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {
  constructor(
    private toastr: ToastrService, 
    private translateService: TranslateService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap({
        error: (err: HttpErrorResponse) => {
          if (err.status === 401 && err.error?.detail?.includes('System license is invalid or expired')) {
            this.router.navigate(['/public/license']);
            return;
          }

          if (err.status === 401 && (err.message === '' || err.url?.includes('api/account'))) {
            return;
          }

          let errorMessage: string;
          let errorTitle: string;

          if (err.status === 403) {
            errorMessage = err.error?.detail || this.translateService.instant('error.http.403');
            errorTitle = this.translateService.instant('error.title');
          } else if (err.error?.type === 'https://www.jhipster.tech/problem/problem-with-message') {
            errorMessage = err.error.detail || err.error.message || this.translateService.instant('error.general');
            errorTitle = err.error.title || this.translateService.instant('error.title');
          } else {
            errorMessage = err.error?.message || err.message || this.translateService.instant('error.general');
            errorTitle = this.translateService.instant('error.title');
          }

          this.toastr.error(errorMessage, errorTitle, {
            enableHtml: true,
            closeButton: false,
            progressBar: true,
            timeOut: 5000,
            positionClass: 'toast-top-right',
          });

          console.error('Error occurred:', err);
        },
      })
    );
  }
}
