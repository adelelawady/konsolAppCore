import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { PagesRoutingModule } from './pages-routing.module';

import { InvoicesComponent } from './invoices/invoices.component';
import { AccountsComponent } from './accounts/accounts.component';
import { InventoryComponent } from './inventory/inventory.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { SalesComponent } from './sales/sales.component';

// Sub-pages
import { MoneyMovementComponent } from './accounts/money-movement/money-movement.component';
import { ExchangeComponent } from './accounts/exchange/exchange.component';
import { ReceiptComponent } from './accounts/receipt/receipt.component';
import { ProductsComponent } from './products/products.component';

@NgModule({
  declarations: [
    InvoicesComponent,
    AccountsComponent,
    InventoryComponent,
    PurchaseComponent,
    SalesComponent,
    MoneyMovementComponent,
    ExchangeComponent,
    ReceiptComponent,
    ProductsComponent,
  ],
  imports: [CommonModule, SharedModule, RouterModule, PagesRoutingModule],
  exports: [],
})
export class PagesModule {}
