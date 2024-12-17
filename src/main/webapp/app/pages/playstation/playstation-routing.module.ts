import { inject, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, RouterModule, Routes } from '@angular/router';
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
              playstationDevice: playstationDeviceResolve,
            },
          },
          {
            path: ':id/edit',
            component: PlaystationDeviceUpdateComponent,
            resolve: {
              playstationDevice: playstationDeviceResolve,
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
      {
        path: 'products',
        component: ProductsControlComponent,
        data: { pageTitle: 'Products Control' }
      },

      {
        path: 'sessions',
        children: [
          {
            path: '',
            component: SessionListComponent,
            data: { pageTitle: 'Sessions History' }
          },
          {
            path: ':id/view',
            component: SessionInvoiceViewComponent,
            resolve: {
              session: (route: ActivatedRouteSnapshot) => {
                const id = route.paramMap.get('id');
                if (id) {
                  return inject(PlaystationResourceService).getSession(id);
                }
                return of(null);
              },
            },
            data: { pageTitle: 'playstation.session.invoice.title' }
          }
        ]
      },
    ],
  },
];






@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PlaystationRoutingModule { }
