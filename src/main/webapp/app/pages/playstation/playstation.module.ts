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
import { ProductsModule } from '../products/products.module';
import { ProductsControlComponent } from './products-control/products-control.component';
import { PlaystationService } from './services/playstation.service';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { SessionListComponent } from './components/session/session-list.component';
import { SessionInvoiceViewComponent } from './components/session/session-invoice-view.component';
import { ConfirmationModalComponent } from 'app/shared/components/confirmation-modal/confirmation-modal.component';

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
    ProductsControlComponent,
    CheckoutComponent,
    SessionListComponent,
    SessionInvoiceViewComponent,
    ConfirmationModalComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    PlaystationRoutingModule,
    LayoutsModule,
    NgbPaginationModule,
    ReactiveFormsModule,
    ProductsModule,
  ],
  exports: [
    DeviceListComponent,
    OrdersSliderComponent,
    SessionListComponent,
  ],
  providers: [
    PlaystationService
  ]
})
export class PlaystationModule { }
