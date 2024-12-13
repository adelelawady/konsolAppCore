import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPriceOption } from '../price-option.model';
import { PriceOptionService } from '../service/price-option.service';

const priceOptionResolve = (route: ActivatedRouteSnapshot): Observable<null | IPriceOption> => {
  const id = route.params.id;
  if (id) {
    return inject(PriceOptionService)
      .find(id)
      .pipe(
        mergeMap((priceOption: HttpResponse<IPriceOption>) => {
          if (priceOption.body) {
            return of(priceOption.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default priceOptionResolve;
