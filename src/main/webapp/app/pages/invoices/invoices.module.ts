// app/pages/invoices/invoices.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { InvoicesComponent } from './invoices.component';
import { SalesInvoicesComponent } from './sales-invoices/sales-invoices.component';
import { PurchaseInvoicesComponent } from './purchase-invoices/purchase-invoices.component';
import { InvoiceDetailsComponent } from './invoice-details/invoice-details.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [InvoicesComponent, SalesInvoicesComponent, PurchaseInvoicesComponent, InvoiceDetailsComponent],
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
      {
        path: ':invoiceId',
        component: InvoiceDetailsComponent,
        data: {
          pageTitle: 'Invoice Details',
        },
      },
    ]),
  ],
  exports: [],
})
export class InvoicesModule {}
