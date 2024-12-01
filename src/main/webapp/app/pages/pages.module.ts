import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { PagesRoutingModule } from './pages-routing.module';

import { InvoicesComponent } from './invoices/invoices.component';
import { InventoryComponent } from './inventory/inventory.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { SalesComponent } from './sales/sales.component';

import { ProductsComponent } from './products/products.component';
import { ProductsModule } from './products/products.module';
import { AccountsModule } from './accounts/accounts.module';

@NgModule({
  declarations: [InvoicesComponent, InventoryComponent, PurchaseComponent, SalesComponent],
  imports: [CommonModule, SharedModule, RouterModule, PagesRoutingModule, ProductsModule, AccountsModule],
  exports: [],
})
export class PagesModule {}
