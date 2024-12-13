import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import PlaystationDeviceResolve from './route/playstation-device-routing-resolve.service';

const playstationDeviceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/playstation-device.component').then(m => m.PlaystationDeviceComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/playstation-device-detail.component').then(m => m.PlaystationDeviceDetailComponent),
    resolve: {
      playstationDevice: PlaystationDeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/playstation-device-update.component').then(m => m.PlaystationDeviceUpdateComponent),
    resolve: {
      playstationDevice: PlaystationDeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/playstation-device-update.component').then(m => m.PlaystationDeviceUpdateComponent),
    resolve: {
      playstationDevice: PlaystationDeviceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default playstationDeviceRoute;
