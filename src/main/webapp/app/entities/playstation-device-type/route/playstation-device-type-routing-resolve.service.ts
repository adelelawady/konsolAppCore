import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlaystationDeviceType } from '../playstation-device-type.model';
import { PlaystationDeviceTypeService } from '../service/playstation-device-type.service';

const playstationDeviceTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlaystationDeviceType> => {
  const id = route.params.id;
  if (id) {
    return inject(PlaystationDeviceTypeService)
      .find(id)
      .pipe(
        mergeMap((playstationDeviceType: HttpResponse<IPlaystationDeviceType>) => {
          if (playstationDeviceType.body) {
            return of(playstationDeviceType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default playstationDeviceTypeResolve;
