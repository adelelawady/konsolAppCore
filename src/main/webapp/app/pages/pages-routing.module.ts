import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

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

const routes: Routes = [
  {
    path: '',
    component: NavigationComponent,
  },
  {
    path: 'invoices',
    component: InvoicesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'categories',
    component: CategoriesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'accounts',
    component: AccountsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'inventory',
    component: InventoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'purchase',
    component: PurchaseComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'sales',
    component: SalesComponent,
    canActivate: [UserRouteAccessService],
  },
  // Sub-routes
  {
    path: 'categories/new',
    component: NewCategoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'accounts/money-movement',
    component: MoneyMovementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'accounts/exchange',
    component: ExchangeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'accounts/receipt',
    component: ReceiptComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
