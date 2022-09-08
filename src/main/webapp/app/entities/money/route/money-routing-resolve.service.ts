import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMoney } from '../money.model';
import { MoneyService } from '../service/money.service';

@Injectable({ providedIn: 'root' })
export class MoneyRoutingResolveService implements Resolve<IMoney | null> {
  constructor(protected service: MoneyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMoney | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((money: HttpResponse<IMoney>) => {
          if (money.body) {
            return of(money.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
