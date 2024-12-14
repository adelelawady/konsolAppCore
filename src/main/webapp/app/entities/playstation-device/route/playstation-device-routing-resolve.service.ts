import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';

const playstationDeviceResolve = (route: ActivatedRouteSnapshot): Observable<null | PsDeviceDTO> => {
  const id = route.params.id;
  if (id) {
    return inject(PlaystationResourceService)
      .getDevice(id)
      .pipe(
        mergeMap((device: PsDeviceDTO) => {
          if (device) {
            return of(device);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default playstationDeviceResolve;
