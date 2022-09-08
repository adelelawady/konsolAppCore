import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStoreItem } from '../store-item.model';
import { StoreItemService } from '../service/store-item.service';

@Injectable({ providedIn: 'root' })
export class StoreItemRoutingResolveService implements Resolve<IStoreItem | null> {
  constructor(protected service: StoreItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStoreItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((storeItem: HttpResponse<IStoreItem>) => {
          if (storeItem.body) {
            return of(storeItem.body);
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
