import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { InvoiceDTO } from 'app/core/konsolApi/model/invoiceDTO';
import { CreateInvoiceItemDTO } from 'app/core/konsolApi/model/createInvoiceItemDTO';
import { BankDTO } from 'app/core/konsolApi/model/bankDTO';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { ItemsSearchBoxComponent } from 'app/shared/components/items-search-box/items-search-box.component';
import { InvoiceItemDTO } from 'app/core/konsolApi/model/invoiceItemDTO';
interface ErrorResponse {
  type: string;
  title: string;
  status: number;
  detail: string;
  path: string;
  message: string;
}
@Component({
  selector: 'app-pos-invoice',
  templateUrl: './pos-invoice.component.html',
  styleUrls: ['./pos-invoice.component.scss'],
})
export class PosInvoiceComponent implements OnInit {
  @Input() invoiceType: 'SALE' | 'PURCHASE' = 'SALE';
  @ViewChild('itemSearchBox') itemSearchBox!: ItemsSearchBoxComponent;
  @ViewChild('addButton') addButton!: ElementRef;
  @ViewChild('qtyInput') qtyInput!: ElementRef;
  @ViewChild('priceInput') priceInput!: ElementRef;
  @ViewChild('discountInput') discountInput!: ElementRef;

  currentQuantity = 0;
  currentPrice = 0;
  selectedItem: ItemViewDTO | null = null;
  currentInvoice: any | null = null;
  loading = false;
  selectedBankId: string | null = null;
  selectedAccountId: string | null = null;
  errorMessage: string | null = null;
  showError = false;
  additions: number = 0;
  additionsType: string = '';

  invoicePk: string = '-';
  // New properties for selected bank and store
  selectedBank: BankDTO | null = null;
  selectedStore: StoreDTO | null = null;
  private discountTimeout: any;
  editingItem: { id: string; qty: number; price: number; discount: number } | null = null;

  constructor(private invoiceService: InvoiceResourceService) {}

  ngOnInit(): void {
    this.initializeNewInvoice();
  }

  initializeNewInvoice(): void {
    console.log(`Initializing new invoice of type: ${this.invoiceType}`);
    this.loading = true;
    this.invoiceService.initializeNewInvoice(this.invoiceType).subscribe({
      next: invoice => {
        this.currentInvoice = invoice;
        this.invoicePk = String(invoice.pk);
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
    if (item) {
      this.selectedItem = item;
      this.currentQuantity = 1;
      this.currentPrice = Number(item.price1) || 0;

      setTimeout(() => {
        this.qtyInput?.nativeElement.focus();
        this.qtyInput?.nativeElement.select();
      });
    }
  }

  addCurrentItem(): void {
    if (this.selectedItem && this.currentInvoice?.id) {
      const invoiceItem: CreateInvoiceItemDTO = {
        itemId: this.selectedItem.id || '',
        qty: this.currentQuantity,
        price: this.currentPrice,
      };

      this.loading = true;
      this.invoiceService.addInvoiceItem(this.currentInvoice.id, invoiceItem).subscribe({
        next: updatedInvoice => {
          this.currentInvoice = updatedInvoice;
          this.resetInputs();
          this.loading = false;
          this.showError = false;
          this.errorMessage = null;

          this.itemSearchBox.clearSelection();
          setTimeout(() => {
            this.itemSearchBox.searchInput?.nativeElement.focus();
          });
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error adding invoice item:', error);
          this.loading = false;

          const errorResponse = error.error as ErrorResponse;
          this.errorMessage = errorResponse.detail || 'حدث خطأ أثناء إضافة المنتج';
          this.showError = true;

          setTimeout(() => {
            this.showError = false;
            this.errorMessage = null;
          }, 5000);
        },
      });
    }
  }

  onQuantityKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.priceInput?.nativeElement.focus();
      this.priceInput?.nativeElement.select();
    }
  }

  onPriceKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.addCurrentItem();
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

  onDiscountChange(value: number): void {
    // Clear any existing timeout
    if (this.discountTimeout) {
      clearTimeout(this.discountTimeout);
    }

    // Set a new timeout to update the discount
    this.discountTimeout = setTimeout(() => {
      if (this.currentInvoice?.id) {
        this.loading = true;
        this.invoiceService.updateInvoice({ discount: value }, this.currentInvoice.id).subscribe({
          next: () => {
            this.reloadInvoice();
          },
          error: error => {
            console.error('Error updating discount:', error);
            this.loading = false;
          },
        });
      }
    }, 500); // Wait for 500ms after the user stops typing
  }

  updateDiscount(): void {
    // Clear any existing timeout
    if (this.discountTimeout) {
      clearTimeout(this.discountTimeout);
      this.discountTimeout = null;
    }

    // Force update on blur
    const value = Number(this.discountInput.nativeElement.value);
    if (this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService.updateInvoice({ discount: value }, this.currentInvoice.id).subscribe({
        next: () => {
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
          this.itemSearchBox.loadInitialItems();
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
    return this.currentInvoice.invoiceItems.reduce((total: any, item: any) => total + (item.qtyIn || 0), 0);
  }

  ngOnDestroy(): void {
    if (this.discountTimeout) {
      clearTimeout(this.discountTimeout);
    }
  }

  startEditingItem(item: InvoiceItemDTO): void {
    this.editingItem = {
      id: item.id || '',
      qty: item.userQty || 0,
      price: item.price || 0,
      discount: item.discount || 0,
    };
  }

  cancelEditingItem(): void {
    this.editingItem = null;
  }

  saveEditingItem(): void {
    if (this.editingItem && this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService
        .updateInvoiceItem(this.editingItem.id, {
          qty: this.editingItem.qty,
          unitPrice: this.editingItem.price,
          price: this.editingItem.price,
          discount: this.editingItem.discount,
        })
        .subscribe({
          next: () => {
            this.editingItem = null;
            this.reloadInvoice();
          },
          error: (error: HttpErrorResponse) => {
            console.error('Error updating item:', error);
            const errorResponse = error.error as ErrorResponse;
            this.errorMessage = errorResponse.detail || 'حدث خطأ أثناء تحديث المنتج';
            this.showError = true;
            this.loading = false;

            setTimeout(() => {
              this.showError = false;
              this.errorMessage = null;
            }, 5000);
          },
        });
    }
  }

  // Handle bank selection
  onBankSelected(bank: BankDTO): void {
    this.selectedBank = bank;
    if (this.currentInvoice) {
      this.currentInvoice.bankId = bank.id; // Assuming bankId is a property in InvoiceDTO
      this.invoiceService.updateInvoice({ bankId: bank.id }, this.currentInvoice.id).subscribe({
        next: () => {
          this.reloadInvoice();
        },
        error: error => {
          console.error('Error updating bank:', error);
          this.loading = false;
        },
      });
    }
  }

  // Handle store selection
  onStoreSelected(store: StoreDTO): void {
    this.selectedStore = store;
    if (this.currentInvoice) {
      this.currentInvoice.storeId = store.id; // Assuming storeId is a property in InvoiceDTO
    }
  }

  onAdditionsChange(value: number): void {
    this.additions = value;
    if (this.currentInvoice) {
      this.currentInvoice.additions = value;
      this.invoiceService.updateInvoice({ additions: value }, this.currentInvoice.id).subscribe({
        next: () => {
          this.reloadInvoice();
        },
        error: error => {
          console.error('Error updating additions:', error);
          this.loading = false;
        },
      });
    }
  }

  onAdditionsTypeChange(value: string): void {
    this.additionsType = value;
    if (this.currentInvoice) {
      this.currentInvoice.additionsType = value;
      this.invoiceService.updateInvoice({ additionsType: value }, this.currentInvoice.id).subscribe({
        next: () => {
          this.reloadInvoice();
        },
        error: error => {
          console.error('Error updating additions type:', error);
          this.loading = false;
        },
      });
    }
  }
}
