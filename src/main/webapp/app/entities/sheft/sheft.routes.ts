import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import SheftResolve from './route/sheft-routing-resolve.service';

const sheftRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sheft.component').then(m => m.SheftComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sheft-detail.component').then(m => m.SheftDetailComponent),
    resolve: {
      sheft: SheftResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/sheft-update.component').then(m => m.SheftUpdateComponent),
    resolve: {
      sheft: SheftResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/sheft-update.component').then(m => m.SheftUpdateComponent),
    resolve: {
      sheft: SheftResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sheftRoute;
