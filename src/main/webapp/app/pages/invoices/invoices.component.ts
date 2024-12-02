// app/pages/invoices/invoices.component.ts
import { Component, ViewChild } from '@angular/core';
import { InvoiceDTO } from 'app/core/konsolApi/model/invoiceDTO';
import { SalesInvoicesComponent } from './sales-invoices/sales-invoices.component';
import { PurchaseInvoicesComponent } from './purchase-invoices/purchase-invoices.component';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-invoices',
  templateUrl: './invoices.component.html',
})
export class InvoicesComponent {
  @ViewChild('salesInvoices') salesInvoicesComponent?: SalesInvoicesComponent;
  @ViewChild('purchaseInvoices') purchaseInvoicesComponent?: PurchaseInvoicesComponent;

  activeTab = 'sales';
  showAddEditModal = false;
  selectedInvoice?: InvoiceDTO;

  constructor(private router: Router, private route: ActivatedRoute) {
    // Get initial tab from route or query params if needed
    this.route.queryParams.subscribe(params => {
      if (params['tab']) {
        this.activeTab = params['tab'];
      }
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    // Update URL without navigation
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { tab },
      queryParamsHandling: 'merge',
    });

    // Refresh the active tab's data
    setTimeout(() => {
      if (tab === 'sales' && this.salesInvoicesComponent) {
        this.salesInvoicesComponent.loadInvoices();
      } else if (tab === 'purchases' && this.purchaseInvoicesComponent) {
        this.purchaseInvoicesComponent.loadInvoices();
      }
    });
  }

  createInvoice(): void {
    this.selectedInvoice = undefined;
    this.showAddEditModal = true;
  }

  onModalClose(): void {
    this.showAddEditModal = false;
    this.selectedInvoice = undefined;
  }

  onInvoiceSaved(invoice: InvoiceDTO): void {
    this.showAddEditModal = false;
    this.selectedInvoice = undefined;
    // Refresh the active tab's data
    if (this.activeTab === 'sales' && this.salesInvoicesComponent) {
      this.salesInvoicesComponent.loadInvoices();
    } else if (this.activeTab === 'purchases' && this.purchaseInvoicesComponent) {
      this.purchaseInvoicesComponent.loadInvoices();
    }
  }

  onDateRangeChange(dateRange: [Date, Date]): void {
    if (dateRange && dateRange.length === 2) {
      // Update date range in active tab component
      if (this.activeTab === 'sales' && this.salesInvoicesComponent) {
        // Implement date range filtering in sales component
      } else if (this.activeTab === 'purchases' && this.purchaseInvoicesComponent) {
        // Implement date range filtering in purchases component
      }
    }
  }
}
