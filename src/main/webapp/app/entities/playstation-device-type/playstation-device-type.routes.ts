import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import PlaystationDeviceTypeResolve from './route/playstation-device-type-routing-resolve.service';

const playstationDeviceTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/playstation-device-type.component').then(m => m.PlaystationDeviceTypeComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/playstation-device-type-detail.component').then(m => m.PlaystationDeviceTypeDetailComponent),
    resolve: {
      playstationDeviceType: PlaystationDeviceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/playstation-device-type-update.component').then(m => m.PlaystationDeviceTypeUpdateComponent),
    resolve: {
      playstationDeviceType: PlaystationDeviceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/playstation-device-type-update.component').then(m => m.PlaystationDeviceTypeUpdateComponent),
    resolve: {
      playstationDeviceType: PlaystationDeviceTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default playstationDeviceTypeRoute;
