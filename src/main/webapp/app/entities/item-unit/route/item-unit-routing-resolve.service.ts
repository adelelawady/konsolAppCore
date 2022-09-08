import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemUnit } from '../item-unit.model';
import { ItemUnitService } from '../service/item-unit.service';

@Injectable({ providedIn: 'root' })
export class ItemUnitRoutingResolveService implements Resolve<IItemUnit | null> {
  constructor(protected service: ItemUnitService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemUnit | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemUnit: HttpResponse<IItemUnit>) => {
          if (itemUnit.body) {
            return of(itemUnit.body);
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
