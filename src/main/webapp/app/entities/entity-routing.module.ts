import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pk',
        data: { pageTitle: 'konsolCoreApp.pk.home.title' },
        loadChildren: () => import('./pk/pk.module').then(m => m.PkModule),
      },
      {
        path: 'invoice',
        data: { pageTitle: 'konsolCoreApp.invoice.home.title' },
        loadChildren: () => import('./invoice/invoice.module').then(m => m.InvoiceModule),
      },
      {
        path: 'invoice-item',
        data: { pageTitle: 'konsolCoreApp.invoiceItem.home.title' },
        loadChildren: () => import('./invoice-item/invoice-item.module').then(m => m.InvoiceItemModule),
      },
      {
        path: 'account-user',
        data: { pageTitle: 'konsolCoreApp.accountUser.home.title' },
        loadChildren: () => import('./account-user/account-user.module').then(m => m.AccountUserModule),
      },
      {
        path: 'bank',
        data: { pageTitle: 'konsolCoreApp.bank.home.title' },
        loadChildren: () => import('./bank/bank.module').then(m => m.BankModule),
      },
      {
        path: 'money',
        data: { pageTitle: 'konsolCoreApp.money.home.title' },
        loadChildren: () => import('./money/money.module').then(m => m.MoneyModule),
      },
      {
        path: 'store',
        data: { pageTitle: 'konsolCoreApp.store.home.title' },
        loadChildren: () => import('./store/store.module').then(m => m.StoreModule),
      },
      {
        path: 'store-item',
        data: { pageTitle: 'konsolCoreApp.storeItem.home.title' },
        loadChildren: () => import('./store-item/store-item.module').then(m => m.StoreItemModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'konsolCoreApp.item.home.title' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'item-unit',
        data: { pageTitle: 'konsolCoreApp.itemUnit.home.title' },
        loadChildren: () => import('./item-unit/item-unit.module').then(m => m.ItemUnitModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
