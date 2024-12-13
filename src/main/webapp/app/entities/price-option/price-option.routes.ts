import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import PriceOptionResolve from './route/price-option-routing-resolve.service';

const priceOptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/price-option.component').then(m => m.PriceOptionComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/price-option-detail.component').then(m => m.PriceOptionDetailComponent),
    resolve: {
      priceOption: PriceOptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/price-option-update.component').then(m => m.PriceOptionUpdateComponent),
    resolve: {
      priceOption: PriceOptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/price-option-update.component').then(m => m.PriceOptionUpdateComponent),
    resolve: {
      priceOption: PriceOptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default priceOptionRoute;
