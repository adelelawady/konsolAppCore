import { NgModule, inject } from '@angular/core';
import { RouterModule, Routes, ActivatedRouteSnapshot } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PlaystationLayoutComponent } from '../../layouts/playstation/playstation-layout.component';
import { DevicesControlComponent } from './devices-control/devices-control.component';
import { PlaystationDeviceUpdateComponent } from 'app/entities/playstation-device/update/playstation-device-update.component';
import PlaystationDeviceResolve from 'app/entities/playstation-device/route/playstation-device-routing-resolve.service';
import { DeviceTypeControlComponent } from './device-type-control/device-type-control.component';
import { DeviceTypeFormComponent } from './device-type-control/device-type-form.component';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { of } from 'rxjs';

const routes: Routes = [
  {
    path: '',
    component: PlaystationLayoutComponent,
    children: [
      {
        path: '',
        component: NavigationPageComponent,
        canActivate: [UserRouteAccessService],
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [UserRouteAccessService],
      },
      {
        path: 'controls',
        children: [
          {
            path: '',
            component: DevicesControlComponent,
          },
          {
            path: 'new',
            component: PlaystationDeviceUpdateComponent,
            resolve: {
              playstationDevice: PlaystationDeviceResolve,
            },
          },
          {
            path: ':id/edit',
            component: PlaystationDeviceUpdateComponent,
            resolve: {
              playstationDevice: PlaystationDeviceResolve,
            },
          },
        ],
      },
      {
        path: 'device-types',
        children: [
          {
            path: '',
            component: DeviceTypeControlComponent,
          },
          {
            path: 'new',
            component: DeviceTypeFormComponent,
          },
          {
            path: ':id/edit',
            component: DeviceTypeFormComponent,
            resolve: {
              deviceType: (route: ActivatedRouteSnapshot) => {
                const id = route.paramMap.get('id');
                if (id) {
                  return inject(PlaystationResourceService).getDeviceType(id);
                }
                return of(null);
              },
            },
          },
        ],
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PlaystationRoutingModule { }
