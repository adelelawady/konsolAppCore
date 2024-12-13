import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayStationSession } from '../play-station-session.model';
import { PlayStationSessionService } from '../service/play-station-session.service';

const playStationSessionResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlayStationSession> => {
  const id = route.params.id;
  if (id) {
    return inject(PlayStationSessionService)
      .find(id)
      .pipe(
        mergeMap((playStationSession: HttpResponse<IPlayStationSession>) => {
          if (playStationSession.body) {
            return of(playStationSession.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default playStationSessionResolve;
