import { inject, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, RouterModule, Routes } from '@angular/router';
import { PlaystationContainerResolver } from './resolvers/playstation-container.resolver';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DevicesControlComponent } from './devices-control/devices-control.component';
import { DeviceTypeControlComponent } from './device-type-control/device-type-control.component';
import { ProductsControlComponent } from './products-control/products-control.component';
import { SessionListComponent } from './components/session/session-list.component';
import { PlaystationLayoutComponent } from 'app/layouts/playstation/playstation-layout.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlaystationDeviceUpdateComponent } from 'app/entities/playstation-device/update/playstation-device-update.component';
import playstationDeviceResolve from 'app/entities/playstation-device/route/playstation-device-routing-resolve.service';
import { DeviceTypeFormComponent } from './device-type-control/device-type-form.component';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { of } from 'rxjs';
import { SessionInvoiceViewComponent } from './components/session/session-invoice-view.component';
import { SessionDetailsComponent } from './session-history-control/session-details/session-details.component';
import playStationSessionResolve from 'app/entities/play-station-session/route/play-station-session-routing-resolve.service';
import { LastSessionsComponent } from './components/last-sessions/last-sessions.component';

const routes: Routes = [
  {
    path: 'container/:containerId/navigation',
    component: PlaystationLayoutComponent,
    resolve: {
      container: PlaystationContainerResolver,
    },
    children: [
      {
        path: '',
        component: NavigationPageComponent,

        data: { breadcrumb: 'Overview' },
        canActivate: [UserRouteAccessService],
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        data: { breadcrumb: 'Dashboard' },
        canActivate: [UserRouteAccessService],
        resolve: {
          container: PlaystationContainerResolver,
        },
      },
      {
        path: 'controls',
        data: { breadcrumb: 'Devices' },
        resolve: {
          container: PlaystationContainerResolver,
        },
        children: [
          {
            path: '',
            component: DevicesControlComponent,
          },
          {
            path: 'new',
            component: PlaystationDeviceUpdateComponent,
            data: { breadcrumb: 'New Device' },
            resolve: {
              playstationDevice: playstationDeviceResolve,
            },
          },
          {
            path: ':id/edit',
            component: PlaystationDeviceUpdateComponent,
            data: { breadcrumb: 'Edit Device' },
            resolve: {
              playstationDevice: playstationDeviceResolve,
            },
          },
        ],
      },
      {
        path: 'device-types',
        data: { breadcrumb: 'Device Types' },
        children: [
          {
            path: '',
            component: DeviceTypeControlComponent,
          },
          {
            path: 'new',
            component: DeviceTypeFormComponent,
            data: { breadcrumb: 'New Type' },
          },
          {
            path: ':id/edit',
            component: DeviceTypeFormComponent,
            data: { breadcrumb: 'Edit Type' },
            resolve: {
              deviceType(route: ActivatedRouteSnapshot) {
                const id = route.paramMap.get('id');
                return id ? inject(PlaystationResourceService).getDeviceType(id) : of(null);
              },
            },
          },
        ],
      },
      {
        path: 'products',
        component: ProductsControlComponent,
        data: { breadcrumb: 'Products' },
      },
      {
        path: 'sessions',
        data: { breadcrumb: 'Sessions' },
        resolve: {
          container: PlaystationContainerResolver,
        },
        children: [
          {
            path: '',
            component: SessionListComponent,
          },
          {
            path: ':id/view',
            component: SessionInvoiceViewComponent,
            data: { breadcrumb: 'Session Details' },
            resolve: {
              session(route: ActivatedRouteSnapshot) {
                const id = route.paramMap.get('id');
                return id ? inject(PlaystationResourceService).getSession(id) : of(null);
              },
            },
          },
        ],
      },
    ],
  },
  {
    path: 'last-sessions',
    component: LastSessionsComponent,
    data: {
      hasPlaystationLayout: true,
      breadcrumb: 'Last Sessions',
    },
    resolve: {
      container: PlaystationContainerResolver,
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PlaystationRoutingModule {}
