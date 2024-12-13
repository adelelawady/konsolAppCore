import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICafeTable } from '../cafe-table.model';
import { CafeTableService } from '../service/cafe-table.service';

const cafeTableResolve = (route: ActivatedRouteSnapshot): Observable<null | ICafeTable> => {
  const id = route.params.id;
  if (id) {
    return inject(CafeTableService)
      .find(id)
      .pipe(
        mergeMap((cafeTable: HttpResponse<ICafeTable>) => {
          if (cafeTable.body) {
            return of(cafeTable.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default cafeTableResolve;
