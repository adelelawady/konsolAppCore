import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { PagesRoutingModule } from './pages-routing.module';

import { InvoicesComponent } from './invoices/invoices.component';
import { CategoriesComponent } from './categories/categories.component';
import { AccountsComponent } from './accounts/accounts.component';
import { InventoryComponent } from './inventory/inventory.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { SalesComponent } from './sales/sales.component';

// Sub-pages
import { NewCategoryComponent } from './categories/new-category/new-category.component';
import { MoneyMovementComponent } from './accounts/money-movement/money-movement.component';
import { ExchangeComponent } from './accounts/exchange/exchange.component';
import { ReceiptComponent } from './accounts/receipt/receipt.component';
import { NavigationComponent } from './navigation/navigation.component';
import { SalesModule } from './sales/sales.module';

@NgModule({
  declarations: [
    InvoicesComponent,
    CategoriesComponent,
    AccountsComponent,
    InventoryComponent,
    PurchaseComponent,
    NewCategoryComponent,
    MoneyMovementComponent,
    ExchangeComponent,
    ReceiptComponent,
    NavigationComponent,
  ],
  imports: [CommonModule, SharedModule, RouterModule, PagesRoutingModule, SalesModule],
  exports: [NavigationComponent],
})
export class PagesModule {}
