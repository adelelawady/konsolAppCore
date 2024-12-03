import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PosInvoiceComponent } from '../../shared/components/pos-invoice/pos-invoice.component';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';

@Component({
  selector: 'jhi-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.scss'],
})
export class PurchaseComponent {}
