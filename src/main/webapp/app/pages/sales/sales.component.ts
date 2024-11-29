import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { InvoiceDTO } from 'app/core/konsolApi/model/invoiceDTO';
import { CreateInvoiceItemDTO } from 'app/core/konsolApi/model/createInvoiceItemDTO';
import { BankDTO } from 'app/core/konsolApi/model/bankDTO';

@Component({
  selector: 'jhi-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.scss'],
})
export class SalesComponent implements OnInit {
  @ViewChild('addButton') addButton!: ElementRef;
  @ViewChild('qtyInput') qtyInput!: ElementRef;
  @ViewChild('priceInput') priceInput!: ElementRef;

  currentQuantity = 0;
  currentPrice = 0;
  selectedItem: ItemViewDTO | null = null;
  currentInvoice: InvoiceDTO | null = null;
  loading = false;
  selectedBankId: string | null = null;
  selectedAccountId: string | null = null;

  constructor(private invoiceService: InvoiceResourceService) {}

  ngOnInit(): void {
    this.initializeNewInvoice();
  }

  initializeNewInvoice(): void {
    this.loading = true;
    this.invoiceService.initializeNewInvoice('SALE').subscribe({
      next: invoice => {
        this.currentInvoice = invoice;
        this.selectedBankId = invoice.bank?.id || null;
        this.selectedAccountId = invoice.account?.id || null;
        this.loading = false;
      },
      error: error => {
        console.error('Error initializing invoice:', error);
        this.loading = false;
      },
    });
  }

  onItemSelected(item: ItemViewDTO): void {
    if (item && this.currentInvoice?.id) {
      this.selectedItem = item;
      this.currentQuantity = 1;
      this.currentPrice = Number(item.price1) || 0;

      const invoiceItem: CreateInvoiceItemDTO = {
        itemId: item.id || '',
        qty: this.currentQuantity,
        price: this.currentPrice,
      };

      this.loading = true;
      this.invoiceService.addInvoiceItem(this.currentInvoice.id, invoiceItem).subscribe({
        next: updatedInvoice => {
          this.currentInvoice = updatedInvoice;
          this.resetInputs();
          this.loading = false;
        },
        error: error => {
          console.error('Error adding invoice item:', error);
          this.loading = false;
        },
      });
    }
  }

  onBankChange(bankId: string): void {
    if (this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService.updateInvoice({ bankId }, this.currentInvoice.id).subscribe({
        next: result => {
          this.reloadInvoice();
        },
        error: error => {
          console.error('Error updating bank:', error);
          this.loading = false;
        },
      });
    }
  }

  onAccountChange(accountId: string): void {
    if (this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService.updateInvoice({ accountId }, this.currentInvoice.id).subscribe({
        next: result => {
          this.reloadInvoice();
        },
        error: error => {
          console.error('Error updating account:', error);
          this.loading = false;
        },
      });
    }
  }

  onDiscountChange(discount: number): void {
    if (this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService.updateInvoice({ discount }, this.currentInvoice.id).subscribe({
        next: result => {
          this.reloadInvoice();
        },
        error: error => {
          console.error('Error updating discount:', error);
          this.loading = false;
        },
      });
    }
  }

  reloadInvoice(): void {
    if (this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService.getInvoice(this.currentInvoice.id).subscribe({
        next: invoice => {
          this.currentInvoice = invoice;
          this.loading = false;
        },
        error: error => {
          console.error('Error reloading invoice:', error);
          this.loading = false;
        },
      });
    }
  }

  private resetInputs(): void {
    this.selectedItem = null;
    this.currentQuantity = 0;
    this.currentPrice = 0;
  }

  // Add other necessary methods for handling invoice actions
  saveInvoice(): void {
    if (this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService.saveInvoice(this.currentInvoice.id).subscribe({
        next: result => {
          // Handle successful save
          this.loading = false;
          this.initializeNewInvoice(); // Start new invoice after saving
        },
        error: error => {
          console.error('Error saving invoice:', error);
          this.loading = false;
        },
      });
    }
  }

  deleteInvoiceItem(itemId: string): void {
    this.loading = true;
    this.invoiceService.deleteInvoiceItemFromInvoice(itemId).subscribe({
      next: () => {
        this.reloadInvoice();
      },
      error: error => {
        console.error('Error deleting invoice item:', error);
        this.loading = false;
      },
    });
  }

  getTotalQuantity(): number {
    if (!this.currentInvoice?.invoiceItems) return 0;
    return this.currentInvoice.invoiceItems.reduce((total, item) => total + (item.qtyIn || 0), 0);
  }
}
