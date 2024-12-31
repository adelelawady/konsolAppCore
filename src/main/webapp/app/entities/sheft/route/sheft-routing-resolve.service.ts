import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISheft } from '../sheft.model';
import { SheftService } from '../service/sheft.service';

const sheftResolve = (route: ActivatedRouteSnapshot): Observable<null | ISheft> => {
  const id = route.params.id;
  if (id) {
    return inject(SheftService)
      .find(id)
      .pipe(
        mergeMap((sheft: HttpResponse<ISheft>) => {
          if (sheft.body) {
            return of(sheft.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        })
      );
  }
  return of(null);
};

export default sheftResolve;
