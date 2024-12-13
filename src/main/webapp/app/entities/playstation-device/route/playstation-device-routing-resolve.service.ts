import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlaystationDevice } from '../playstation-device.model';
import { PlaystationDeviceService } from '../service/playstation-device.service';

const playstationDeviceResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlaystationDevice> => {
  const id = route.params.id;
  if (id) {
    return inject(PlaystationDeviceService)
      .find(id)
      .pipe(
        mergeMap((playstationDevice: HttpResponse<IPlaystationDevice>) => {
          if (playstationDevice.body) {
            return of(playstationDevice.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default playstationDeviceResolve;
