import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { InvoiceDTO, InvoiceResourceService, InvoiceViewDTO } from 'app/core/konsolApi';

@Component({
  selector: 'app-invoice-details',
  templateUrl: './invoice-details.component.html',
  styleUrls: ['./invoice-details.component.scss'],
})
export class InvoiceDetailsComponent implements OnInit {
  invoiceId: string;
  invoice: InvoiceDTO | null = null;
  loading = true;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private invoiceService: InvoiceResourceService) {
    this.invoiceId = this.route.snapshot.paramMap.get('invoiceId') || '';
  }

  ngOnInit(): void {
    this.loadInvoiceDetails();
  }

  loadInvoiceDetails(): void {
    if (!this.invoiceId) {
      this.error = 'Invoice ID not found';
      this.loading = false;
      return;
    }

    this.invoiceService.getInvoice(this.invoiceId).subscribe({
      next: (response: InvoiceDTO) => {
        this.invoice = response;
        this.loading = false;
      },
      error: (error: any) => {
        this.error = 'Error loading invoice details';
        this.loading = false;
        console.error('Error loading invoice:', error);
      },
    });
  }
}
