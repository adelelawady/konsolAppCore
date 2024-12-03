import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PosInvoiceComponent } from '../../shared/components/pos-invoice/pos-invoice.component';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';

@Component({
  selector: 'jhi-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.scss'],
})
export class SalesComponent {}
