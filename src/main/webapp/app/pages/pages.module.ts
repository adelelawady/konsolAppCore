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

@NgModule({
  declarations: [InventoryComponent, PurchaseComponent, SalesComponent],
  imports: [CommonModule, SharedModule, RouterModule, PagesRoutingModule, ProductsModule, AccountsModule, InvoicesModule],
  exports: [],
})
export class PagesModule {}
