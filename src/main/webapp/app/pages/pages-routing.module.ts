import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import { InvoicesComponent } from './invoices/invoices.component';
import { AccountsComponent } from './accounts/accounts.component';
import { InventoryComponent } from './inventory/inventory.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { SalesComponent } from './sales/sales.component';
import { MoneyComponent } from './money/money.component';

import { NavigationComponent } from './navigation/navigation.component';
import { ProductsComponent } from './products/products.component';

const routes: Routes = [
  {
    path: '',
    component: NavigationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'invoices',
    component: InvoicesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'products',
    loadChildren: () => import('./products/products.module').then(m => m.ProductsModule),
    data: { title: 'Products' },
  },
  {
    path: 'financial-reports',
    loadChildren: () => import('./financial-reports/financial-reports.module').then(m => m.FinancialReportsModule),
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
    path: 'purchase/:invoiceId',
    component: PurchaseComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'sales',
    component: SalesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'sales/:invoiceId',
    component: SalesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'money',
    component: MoneyComponent,
    canActivate: [UserRouteAccessService],
    data: { title: 'Money Transactions' },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
