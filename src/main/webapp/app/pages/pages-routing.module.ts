import { inject, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import { InvoicesComponent } from './invoices/invoices.component';
import { AccountsComponent } from './accounts/accounts.component';
import { InventoryComponent } from './inventory/inventory.component';
import { PurchaseComponent } from './purchase/purchase.component';
import { SalesComponent } from './sales/sales.component';
import { MoneyComponent } from './money/money.component';
import { NavigationComponent } from './navigation/navigation.component';
import { AccountDetailsComponent } from './accounts/account-details/account-details.component';
import { InvoiceDetailsComponent } from './invoices/invoice-details/invoice-details.component';
import { PlaystationContainerResourceService } from 'app/core/konsolApi';
import { of } from 'rxjs';
import { PlaystationContainerResolver } from './playstation/resolvers/playstation-container.resolver';

const routes: Routes = [
  {
    path: '',
    component: NavigationComponent,
    canActivate: [UserRouteAccessService],
    data: { breadcrumb: 'Dashboard' },
  },
  {
    path: 'invoices',
    data: { breadcrumb: 'Invoices' },
    children: [
      {
        path: '',
        component: InvoicesComponent,
        canActivate: [UserRouteAccessService],
      },
      {
        path: ':invoiceId',
        component: InvoiceDetailsComponent,
        canActivate: [UserRouteAccessService],
        data: { breadcrumb: 'Invoice Details' },
      },
    ],
  },
  {
    path: 'products',
    data: { breadcrumb: 'Products' },
    loadChildren: () => import('./products/products.module').then(m => m.ProductsModule),
  },
  {
    path: 'financial-reports',
    data: { breadcrumb: 'Financial Reports' },
    loadChildren: () => import('./financial-reports/financial-reports.module').then(m => m.FinancialReportsModule),
  },
  {
    path: 'accounts',
    data: { breadcrumb: 'Accounts' },
    children: [
      {
        path: '',
        component: AccountsComponent,
        canActivate: [UserRouteAccessService],
      },
      {
        path: ':id',
        component: AccountDetailsComponent,
        canActivate: [UserRouteAccessService],
        data: { breadcrumb: 'Account Details' },
      },
    ],
  },
  {
    path: 'inventory',
    component: InventoryComponent,
    canActivate: [UserRouteAccessService],
    data: { breadcrumb: 'Inventory' },
  },
  {
    path: 'purchase',
    data: { breadcrumb: 'Purchase' },
    children: [
      {
        path: '',
        component: PurchaseComponent,
        canActivate: [UserRouteAccessService],
      },
      {
        path: ':invoiceId',
        component: PurchaseComponent,
        canActivate: [UserRouteAccessService],
        data: { breadcrumb: 'Purchase Details' },
      },
    ],
  },
  {
    path: 'sales',
    data: { breadcrumb: 'Sales' },
    children: [
      {
        path: '',
        component: SalesComponent,
        canActivate: [UserRouteAccessService],
      },
      {
        path: ':invoiceId',
        component: SalesComponent,
        canActivate: [UserRouteAccessService],
        data: { breadcrumb: 'Sale Details' },
      },
    ],
  },
  {
    path: 'money',
    component: MoneyComponent,
    canActivate: [UserRouteAccessService],
    data: { breadcrumb: 'Money Transactions' },
  },
  {
    path: '',
    loadChildren: () => import('./playstation/playstation.module').then(m => m.PlaystationModule),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
