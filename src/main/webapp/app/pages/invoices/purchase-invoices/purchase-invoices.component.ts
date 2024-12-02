// app/pages/invoices/purchase-invoices/purchase-invoices.component.ts
import { Component } from '@angular/core';
import { InvoicesView } from '../invoices-view';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { InvoiceViewDTO } from 'app/core/konsolApi/model/invoiceViewDTO';
import { InvoiceViewSimpleDTO } from 'app/core/konsolApi/model/invoiceViewSimpleDTO';

@Component({
  selector: 'app-purchase-invoices',
  templateUrl: './purchase-invoices.component.html',
})
export class PurchaseInvoicesComponent extends InvoicesView {
  type = 'PURCHASE' as const;

  constructor(protected override invoiceService: InvoiceResourceService) {
    super(invoiceService);
  }
}
