import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import PlaystationContainerResolve from './route/playstation-container-routing-resolve.service';

const playstationContainerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/playstation-container.component').then(m => m.PlaystationContainerComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/playstation-container-detail.component').then(m => m.PlaystationContainerDetailComponent),
    resolve: {
      playstationContainer: PlaystationContainerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/playstation-container-update.component').then(m => m.PlaystationContainerUpdateComponent),
    resolve: {
      playstationContainer: PlaystationContainerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/playstation-container-update.component').then(m => m.PlaystationContainerUpdateComponent),
    resolve: {
      playstationContainer: PlaystationContainerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default playstationContainerRoute;
