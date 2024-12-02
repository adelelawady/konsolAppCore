// app/pages/invoices/invoices.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { InvoicesComponent } from './invoices.component';
import { SalesInvoicesComponent } from './sales-invoices/sales-invoices.component';
import { PurchaseInvoicesComponent } from './purchase-invoices/purchase-invoices.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [InvoicesComponent, SalesInvoicesComponent, PurchaseInvoicesComponent],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild([
      {
        path: '',
        component: InvoicesComponent,
        data: {
          pageTitle: 'invoices.title',
        },
      },
    ]),
  ],
  exports: [],
})
export class InvoicesModule {}
