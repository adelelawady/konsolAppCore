// app/pages/invoices/invoices-view.ts
import { Directive, OnInit } from '@angular/core';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { InvoiceViewDTO } from 'app/core/konsolApi/model/invoiceViewDTO';
import { InvoicesSearchModel } from 'app/core/konsolApi/model/invoicesSearchModel';
import { InvoiceViewSimpleDTO } from 'app/core/konsolApi/model/invoiceViewSimpleDTO';
import { InvoiceItemDTO } from 'app/core/konsolApi/model/invoiceItemDTO';
import { InvoiceDTO } from 'app/core/konsolApi/model/invoiceDTO';
import { InvoiceItemViewDTO } from 'app/core/konsolApi/model/invoiceItemViewDTO';
import { formatDate } from '@angular/common';

@Directive()
export abstract class InvoicesView implements OnInit {
  abstract type: InvoicesSearchModel.KindEnum;

  invoices: InvoiceViewSimpleDTO[] = [];
  invoiceItems: InvoiceItemViewDTO[] = [];
  selectedInvoice?: InvoiceViewSimpleDTO;
  selectedInvoiceDetails?: InvoiceViewDTO;

  // Pagination and loading state
  loading = false;
  currentPage = 0;
  pageSize = 10;
  totalRecords = 0;
  searchTerm = '';
  sortField = 'created_date';
  sortDirection: 'asc' | 'desc' = 'desc';

  // Table columns configuration
  columns = [
    { field: 'pk', header: 'konsolCoreApp.invoice.id' },
    {
      field: 'created_date',
      header: 'konsolCoreApp.invoice.detail.info.date',
      format: (value: string) => this.formatDate(value),
    },
    { field: 'account', header: 'konsolCoreApp.invoice.detail.info.account', format: (row: any) => row?.name || 'NA' },
    {
      field: 'totalCost',
      header: 'konsolCoreApp.invoice.detail.info.totalCost',
      format: (value: number) => value?.toFixed(2) || '0.00',
      visible: () => this.type === 'PURCHASE',
    },
    {
      field: 'totalPrice',
      header: 'konsolCoreApp.invoice.detail.info.totalPrice',
      format: (value: number) => value?.toFixed(2) || '0.00',
      visible: () => this.type === 'SALE',
    },
    {
      field: 'netCost',
      header: 'konsolCoreApp.invoice.detail.info.netCost',
      format: (value: number) => value?.toFixed(2) || '0.00',
      visible: () => this.type === 'PURCHASE',
    },
    {
      field: 'netPrice',
      header: 'konsolCoreApp.invoice.detail.info.netPrice',
      format: (value: number) => value?.toFixed(2) || '0.00',
      visible: () => this.type === 'SALE',
    },
    { field: 'deferred', header: 'konsolCoreApp.invoice.deferred' },
  ];

  get itemColumns() {
    const baseColumns = [
      { field: 'pk', header: 'konsolCoreApp.invoice.id' },
      { field: 'item', header: 'konsolCoreApp.invoice.item', format: (row: any) => row?.name || 'NA' },
      {
        field: 'userQty',
        header: 'konsolCoreApp.invoice.items.quantity',
        format: (value: number) => value?.toFixed(2) || '0.00',
      },
    ];

    const salesColumns = [
      {
        field: 'price',
        header: 'konsolCoreApp.invoice.items.price',
        format: (value: number) => value?.toFixed(2) || '0.00',
      },
      {
        field: 'qtyOut',
        header: 'konsolCoreApp.invoice.items.quantity',
        format: (value: number) => value?.toFixed(2) || '0.00',
      },
      {
        field: 'totalPrice',
        header: 'konsolCoreApp.invoice.totalPrice',
        format: (value: number) => value?.toFixed(2) || '0.00',
      },
      {
        field: 'netPrice',
        header: 'konsolCoreApp.invoice.netPrice',
        format: (value: number) => value?.toFixed(2) || '0.00',
        visible: () => this.type === 'SALE',
      },
    ];

    const purchaseColumns = [
      {
        field: 'cost',
        header: 'konsolCoreApp.invoice.items.cost',
        format: (value: number) => value?.toFixed(2) || '0.00',
      },
      {
        field: 'qtyIn',
        header: 'konsolCoreApp.invoice.items.quantity',
        format: (value: number) => value?.toFixed(2) || '0.00',
      },
      {
        field: 'totalCost',
        header: 'konsolCoreApp.invoice.totalCost',
        format: (value: number) => value?.toFixed(2) || '0.00',
      },
      {
        field: 'netCost',
        header: 'konsolCoreApp.invoice.netCost',
        format: (value: number) => value?.toFixed(2) || '0.00',
        visible: () => this.type === 'PURCHASE',
      },
    ];

    return [...baseColumns, ...(this.type === 'SALE' ? salesColumns : purchaseColumns)];
  }

  constructor(protected invoiceService: InvoiceResourceService) {}

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(): void {
    this.loading = true;

    const searchModel: InvoicesSearchModel = {
      page: this.currentPage, // Convert from 1-based to 0-based
      size: this.pageSize,
      sortField: this.sortField,
      sortOrder: this.sortDirection,
      searchText: this.searchTerm || undefined,
      kind: this.type,
    };

    this.invoiceService.invoicesViewSearchPaginate(searchModel).subscribe({
      next: response => {
        if (response) {
          this.invoices = response.result || [];
          this.totalRecords = response.total || 0;
        }
        this.loading = false;
      },
      error: error => {
        console.error('Error loading invoices:', error);
        this.loading = false;
      },
    });
  }

  onItemClick(invoice: InvoiceViewSimpleDTO): void {
    this.selectedInvoice = invoice;
    if (invoice.id) {
      this.loading = true;
      this.invoiceService.getInvoice(invoice.id).subscribe({
        next: (response: InvoiceDTO) => {
          // Convert InvoiceDTO to InvoiceViewDTO
          this.selectedInvoiceDetails = {
            ...response,
            invoiceItems: response.invoiceItems
              ? Array.from(response.invoiceItems).map(
                  item =>
                    ({
                      ...item,
                      totalPrice: this.type === 'SALE' ? (item.userQty || 0) * (item.price || 0) : undefined,
                      totalCost: this.type === 'PURCHASE' ? (item.userQty || 0) * (item.cost || 0) : undefined,
                    } as InvoiceItemViewDTO)
                )
              : [],
          } as InvoiceViewDTO;

          this.invoiceItems = this.selectedInvoiceDetails.invoiceItems || [];
          this.loading = false;
        },
        error: error => {
          console.error('Error loading invoice details:', error);
          this.loading = false;
        },
      });
    }
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadInvoices();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadInvoices();
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.currentPage = 0;
    this.loadInvoices();
  }

  onSort(event: { field: string; order: string }): void {
    this.sortField = event.field;
    this.sortDirection = event.order as 'asc' | 'desc';
    this.loadInvoices();
  }

  onEdit(invoice: InvoiceViewSimpleDTO): void {
    this.selectedInvoice = invoice;
    if (invoice.id) {
      this.loadInvoiceItems(invoice.id);
    }
  }

  onDelete(invoice: InvoiceViewSimpleDTO): void {
    if (invoice.id && confirm('konsolCoreApp.invoice.delete.question')) {
      this.invoiceService.deleteInvoice(invoice.id).subscribe({
        next: () => {
          this.loadInvoices();
          this.selectedInvoice = undefined;
          this.selectedInvoiceDetails = undefined;
        },
        error: error => {
          console.error('Error deleting invoice:', error);
        },
      });
    }
  }

  private loadInvoiceItems(invoiceId: string): void {
    this.loading = true;
    this.invoiceService.getInvoiceItems(invoiceId).subscribe({
      next: items => {
        this.invoiceItems = Array.isArray(items)
          ? items.map(
              item =>
                ({
                  ...item,
                  totalPrice: this.type === 'SALE' ? (item.userQty || 0) * (item.price || 0) : undefined,
                  totalCost: this.type === 'PURCHASE' ? (item.userQty || 0) * (item.cost || 0) : undefined,
                } as InvoiceItemViewDTO)
            )
          : [];
        this.loading = false;
      },
      error: error => {
        console.error('Error loading invoice items:', error);
        this.loading = false;
      },
    });
  }

  private formatDate(date: string): string {
    if (!date) return '';
    try {
      return formatDate(date, 'yyyy-MM-dd HH:mm', 'en-US');
    } catch (error) {
      console.error('Error formatting date:', error);
      return date;
    }
  }
}
