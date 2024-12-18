import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlaystationContainer } from '../playstation-container.model';
import { PlaystationContainerService } from '../service/playstation-container.service';

const playstationContainerResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlaystationContainer> => {
  const id = route.params.id;
  if (id) {
    return inject(PlaystationContainerService)
      .find(id)
      .pipe(
        mergeMap((playstationContainer: HttpResponse<IPlaystationContainer>) => {
          if (playstationContainer.body) {
            return of(playstationContainer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        })
      );
  }
  return of(null);
};

export default playstationContainerResolve;
