// app/pages/invoices/sales-invoices/sales-invoices.component.ts
import { Component } from '@angular/core';
import { InvoicesView } from '../invoices-view';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { InvoiceViewDTO } from 'app/core/konsolApi/model/invoiceViewDTO';
import { InvoiceViewSimpleDTO } from 'app/core/konsolApi/model/invoiceViewSimpleDTO';

@Component({
  selector: 'app-sales-invoices',
  templateUrl: './sales-invoices.component.html',
})
export class SalesInvoicesComponent extends InvoicesView {
  type = 'SALE' as const;

  constructor(protected override invoiceService: InvoiceResourceService) {
    super(invoiceService);
  }
}
