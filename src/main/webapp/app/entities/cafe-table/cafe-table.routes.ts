import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import CafeTableResolve from './route/cafe-table-routing-resolve.service';

const cafeTableRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cafe-table.component').then(m => m.CafeTableComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cafe-table-detail.component').then(m => m.CafeTableDetailComponent),
    resolve: {
      cafeTable: CafeTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cafe-table-update.component').then(m => m.CafeTableUpdateComponent),
    resolve: {
      cafeTable: CafeTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cafe-table-update.component').then(m => m.CafeTableUpdateComponent),
    resolve: {
      cafeTable: CafeTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cafeTableRoute;
