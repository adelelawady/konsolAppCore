import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { PlaystationRoutingModule } from './playstation-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PlaystationLayoutComponent } from '../../layouts/playstation/playstation-layout.component';
import { LayoutsModule } from '../../layouts/layouts.module';
import { DeviceListComponent } from './components/device-list/device-list.component';
import { OrdersSliderComponent } from './components/orders-slider/orders-slider.component';
import { DeviceCardComponent } from './components/device-card/device-card.component';
import { DeviceDetailsComponent } from './components/device-details/device-details.component';
import { DevicesControlComponent } from './devices-control/devices-control.component';
import { NgbPaginationModule } from '@ng-bootstrap/ng-bootstrap';
import { DeviceTypeControlComponent } from './device-type-control/device-type-control.component';
import { DeviceTypeDeleteDialogComponent } from './device-type-control/device-type-delete-dialog.component';
import { DeviceTypeFormComponent } from './device-type-control/device-type-form.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    NavigationPageComponent,
    DashboardComponent,
    PlaystationLayoutComponent,
    DeviceListComponent,
    OrdersSliderComponent,
    DeviceCardComponent,
    DeviceDetailsComponent,
    DevicesControlComponent,
    DeviceTypeControlComponent,
    DeviceTypeDeleteDialogComponent,
    DeviceTypeFormComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    PlaystationRoutingModule,
    LayoutsModule,
    NgbPaginationModule,
    ReactiveFormsModule,
  ],
  exports: [
    DeviceListComponent,
    OrdersSliderComponent
  ]
})
export class PlaystationModule { }
