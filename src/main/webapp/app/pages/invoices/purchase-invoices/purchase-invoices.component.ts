// app/pages/invoices/purchase-invoices/purchase-invoices.component.ts
import { Component } from '@angular/core';
import { InvoicesView } from '../invoices-view';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { InvoiceViewDTO } from 'app/core/konsolApi/model/invoiceViewDTO';
import { InvoiceViewSimpleDTO } from 'app/core/konsolApi/model/invoiceViewSimpleDTO';
import { Router } from '@angular/router';

@Component({
  selector: 'app-purchase-invoices',
  templateUrl: './purchase-invoices.component.html',
})
export class PurchaseInvoicesComponent extends InvoicesView {
  type = 'PURCHASE' as const;

  constructor(protected override router: Router, protected override invoiceService: InvoiceResourceService) {
    super(router, invoiceService);
  }
}
