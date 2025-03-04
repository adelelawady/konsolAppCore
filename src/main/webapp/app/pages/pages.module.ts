import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { PagesRoutingModule } from './pages-routing.module';

import { InventoryComponent } from './inventory/inventory.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { SalesComponent } from './sales/sales.component';

import { ProductsModule } from './products/products.module';
import { AccountsModule } from './accounts/accounts.module';
import { InvoicesModule } from './invoices/invoices.module';
import { BanksModule } from './banks/banks.module';
import { MoneyModule } from './money/money.module';
import { PlaystationModule } from './playstation/playstation.module';
import { AdminModule } from './admin/admin.module';
import { AccountsEmployeeModule } from './accounts copy/accounts.module';

@NgModule({
  declarations: [InventoryComponent, PurchaseComponent, SalesComponent],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    PagesRoutingModule,
    ProductsModule,
    AccountsModule,
    InvoicesModule,
    BanksModule,
    MoneyModule,
    PlaystationModule,
    AdminModule,
    AccountsEmployeeModule,
  ],
  exports: [],
})
export class PagesModule {}
