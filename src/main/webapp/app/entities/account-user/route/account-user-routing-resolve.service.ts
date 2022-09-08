import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountUser } from '../account-user.model';
import { AccountUserService } from '../service/account-user.service';

@Injectable({ providedIn: 'root' })
export class AccountUserRoutingResolveService implements Resolve<IAccountUser | null> {
  constructor(protected service: AccountUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountUser: HttpResponse<IAccountUser>) => {
          if (accountUser.body) {
            return of(accountUser.body);
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
