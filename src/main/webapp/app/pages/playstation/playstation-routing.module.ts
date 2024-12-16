import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DevicesControlComponent } from './devices-control/devices-control.component';
import { DeviceTypeControlComponent } from './device-type-control/device-type-control.component';
import { ProductsControlComponent } from './products-control/products-control.component';
import { SessionListComponent } from './components/session/session-list.component';

const routes: Routes = [
  {
    path: '',
    component: NavigationPageComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  {
    path: 'devices',
    component: DevicesControlComponent
  },
  {
    path: 'device-types',
    component: DeviceTypeControlComponent
  },
  {
    path: 'products',
    component: ProductsControlComponent
  },
  {
    path: 'sessions',
    component: SessionListComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PlaystationRoutingModule { }
