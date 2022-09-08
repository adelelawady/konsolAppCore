import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPk } from '../pk.model';
import { PkService } from '../service/pk.service';

@Injectable({ providedIn: 'root' })
export class PkRoutingResolveService implements Resolve<IPk | null> {
  constructor(protected service: PkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPk | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pk: HttpResponse<IPk>) => {
          if (pk.body) {
            return of(pk.body);
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
