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

@NgModule({
  declarations: [
    NavigationPageComponent,
    DashboardComponent,
    PlaystationLayoutComponent,
    DeviceListComponent,
    OrdersSliderComponent,
    DeviceCardComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    PlaystationRoutingModule,
    LayoutsModule
  ],
  exports: [
    DeviceListComponent,
    OrdersSliderComponent
  ]
})
export class PlaystationModule { }
