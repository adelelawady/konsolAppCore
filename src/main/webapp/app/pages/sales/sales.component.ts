import { Component } from '@angular/core';
import { PosInvoiceComponent } from '../../shared/components/pos-invoice/pos-invoice.component';

@Component({
  selector: 'jhi-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.scss'],
})
export class SalesComponent extends PosInvoiceComponent {}
