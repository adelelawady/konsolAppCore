import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PlayStationSessionResolve from './route/play-station-session-routing-resolve.service';

const playStationSessionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/play-station-session.component').then(m => m.PlayStationSessionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/play-station-session-detail.component').then(m => m.PlayStationSessionDetailComponent),
    resolve: {
      playStationSession: PlayStationSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/play-station-session-update.component').then(m => m.PlayStationSessionUpdateComponent),
    resolve: {
      playStationSession: PlayStationSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/play-station-session-update.component').then(m => m.PlayStationSessionUpdateComponent),
    resolve: {
      playStationSession: PlayStationSessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default playStationSessionRoute;
