import { Component, OnInit, Input, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { InvoiceDTO } from 'app/core/konsolApi/model/invoiceDTO';
import { CreateInvoiceItemDTO } from 'app/core/konsolApi/model/createInvoiceItemDTO';
import { BankDTO } from 'app/core/konsolApi/model/bankDTO';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { ItemsSearchBoxComponent } from 'app/shared/components/items-search-box/items-search-box.component';
import { InvoiceItemDTO } from 'app/core/konsolApi/model/invoiceItemDTO';
import { AppCurrencyPipe } from 'app/shared/pipes/app-currency.pipe';

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
export class PosInvoiceComponent implements OnInit, AfterViewInit {
  @Input() invoiceType: 'SALE' | 'PURCHASE' = 'SALE';
  @Input() existingInvoiceId: string | null = null;
  @ViewChild('itemSearchBox') itemSearchBox!: ItemsSearchBoxComponent;
  @ViewChild('addButton') addButton!: ElementRef;
  @ViewChild('qtyInput') qtyInput!: ElementRef;
  @ViewChild('priceInput') priceInput!: ElementRef;
  @ViewChild('discountInput') discountInput!: ElementRef;

  isLoadingExistingInvoiceId = false;
  currentQuantity = 0;
  currentPrice = 0;
  selectedItem: ItemViewDTO | null = null;
  currentInvoice: any | null = null;
  loading = false;
  selectedBankId: string | null = null;
  selectedAccountId: string | null = null;
  additions: number = 0;
  additionsType: string = '';

  // New properties for selected bank and store
  selectedBank: BankDTO | null = null;
  selectedStore: StoreDTO | null = null;
  private discountTimeout: any;
  editingItem: { id: string; qty: number; price: number; discount: number } | null = null;

  // Add deferred property
  isDeferred = false;

  constructor(
    protected appCurrency: AppCurrencyPipe,
    private route: ActivatedRoute,
    private router: Router,
    protected invoiceService: InvoiceResourceService
  ) {}
  ngAfterViewInit(): void {
    if (this.isLoadingExistingInvoiceId) {
      // Set bank and store directly from the invoice
      if (this.currentInvoice && this.currentInvoice.bank) {
        this.selectedBank = this.currentInvoice.bank;
        this.selectedBankId = this.currentInvoice.bank.id;
      }

      if (this.currentInvoice && this.currentInvoice.store) {
        this.selectedStore = this.currentInvoice.store;
      }
    }
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const invoiceId = params.get('invoiceId');
      if (invoiceId) {
        this.isLoadingExistingInvoiceId = true;
        this.existingInvoiceId = invoiceId;
        this.loadExistingInvoice(this.existingInvoiceId);
      } else {
        this.initializeNewInvoice();
      }
    });
  }

  protected loadExistingInvoice(id: string): void {
    this.loading = true;

    this.invoiceService.getInvoice(id).subscribe({
      next: (invoice: any) => {
        // Ensure the invoice structure matches what the template expects
        this.currentInvoice = invoice;

        this.additions = invoice.additions;
        this.additionsType = invoice.additionsType;
        this.isDeferred = invoice.deferred || false;

        // Set bank and store with a slight delay to ensure components are ready
        setTimeout(() => {
          if (invoice.bank) {
            this.selectedBank = { ...invoice.bank };
            this.selectedBankId = invoice.bank.id;
          }

          if (invoice.store) {
            this.selectedStore = { ...invoice.store };
          }

          if (invoice.account) {
            this.selectedAccountId = invoice.account.id;
          }

          // Initialize invoice items array if not present
          if (!this.currentInvoice.invoiceItems) {
            this.currentInvoice.invoiceItems = [];
          }

          this.loading = false;
          this.isLoadingExistingInvoiceId = false;
        }, 0);
      },
      error: error => {
        console.error('Error loading invoice:', error);
        this.loading = false;
        this.isLoadingExistingInvoiceId = false;
      },
    });
  }

  initializeNewInvoice(): void {
    console.log('initializeNewInvoice');
    this.loading = true;
    this.invoiceService.initializeNewInvoice(this.invoiceType).subscribe({
      next: invoice => {
        switch (this.invoiceType) {
          case 'SALE':
            this.router.navigate(['/', 'sales', invoice.id], {
              relativeTo: this.route,
              replaceUrl: true,
            });
            break;
          case 'PURCHASE':
            this.router.navigate(['/', 'purchase', invoice.id], {
              relativeTo: this.route,
              replaceUrl: true,
            });
            break;
        }
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

      if (this.currentInvoice.kind === 'SALE') {
        this.currentPrice = Number(item?.price1) || 0;
      } else if (this.currentInvoice.kind === 'PURCHASE') {
        this.currentPrice = Number(item?.cost) || 0;
      }

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

      // this.loading = true;
      this.invoiceService.addInvoiceItem(this.currentInvoice.id, invoiceItem).subscribe({
        next: updatedInvoice => {
          this.currentInvoice = updatedInvoice;
          this.resetInputs();
          this.loading = false;

          this.itemSearchBox.clearSelection();
          setTimeout(() => {
            this.itemSearchBox.searchInput?.nativeElement.focus();
          });
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error adding invoice item:', error);
          this.loading = false;
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
      this.invoiceService.updateInvoice(this.currentInvoice.id, { bankId }).subscribe({
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

  onDiscountChange(value: number): void {
    // Clear any existing timeout
    if (this.discountTimeout) {
      clearTimeout(this.discountTimeout);
    }

    // Set a new timeout to update the discount
    this.discountTimeout = setTimeout(() => {
      if (this.currentInvoice?.id) {
        this.loading = true;
        this.invoiceService.updateInvoice(this.currentInvoice.id, { discount: value }).subscribe({
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
      this.invoiceService.updateInvoice(this.currentInvoice.id, { discount: value }).subscribe({
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

  reloadInvoice(showLoading = true): void {
    if (this.currentInvoice?.id) {
      this.loading = showLoading;
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
        next: () => {
          this.loading = false;
          // Show notification if deferred is true
          if (this.isDeferred && this.selectedAccountId) {
            // You can use your notification service here
            alert('Money has been added to the selected account');
          }
          this.itemSearchBox.loadInitialItems();
          this.selectedAccountId = null;
          this.initializeNewInvoice();
        },
        error: (error: HttpErrorResponse) => {
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
        this.reloadInvoice(false);
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

            this.loading = false;
          },
        });
    }
  }

  // Handle bank selection
  onBankSelected(bank: BankDTO): void {
    if (this.isLoadingExistingInvoiceId) {
      return;
    }
    this.selectedBank = bank;
    if (this.currentInvoice) {
      this.currentInvoice.bank = bank; // Assuming bankId is a property in InvoiceDTO
      this.invoiceService.updateInvoice(this.currentInvoice.id, { bankId: bank.id }).subscribe({
        next: () => {
          this.reloadInvoice(false);
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
    if (this.isLoadingExistingInvoiceId) {
      return;
    }
    this.selectedStore = store;
    if (this.currentInvoice) {
      this.currentInvoice.store = store; // Assuming storeId is a property in InvoiceDTO
      this.invoiceService.updateInvoice(this.currentInvoice.id, { storeId: store.id }).subscribe({
        next: () => {
          this.reloadInvoice(false);
        },
        error: error => {
          console.error('Error updating store:', error);
          this.loading = false;
        },
      });
    }
  }

  onAdditionsChange(value: number): void {
    if (this.isLoadingExistingInvoiceId) {
      return;
    }
    this.additions = value;
    if (this.currentInvoice) {
      this.currentInvoice.additions = value;
      this.invoiceService.updateInvoice(this.currentInvoice.id, { additions: value }).subscribe({
        next: () => {
          this.reloadInvoice(false);
        },
        error: error => {
          console.error('Error updating additions:', error);
          this.loading = false;
        },
      });
    }
  }

  onAdditionsTypeChange(value: string): void {
    if (this.isLoadingExistingInvoiceId) {
      return;
    }
    this.additionsType = value;
    if (this.currentInvoice) {
      this.currentInvoice.additionsType = value;
      this.invoiceService.updateInvoice(this.currentInvoice.id, { additionsType: value }).subscribe({
        next: () => {
          this.reloadInvoice(false);
        },
        error: error => {
          console.error('Error updating additions type:', error);
          this.loading = false;
        },
      });
    }
  }

  onAccountSelected(account: any): void {
    if (!account || !account.id) {
      if (!this.currentInvoice) {
        return;
      }
    }

    if (this.currentInvoice) {
      this.invoiceService.updateInvoice(this.currentInvoice.id, { accountId: account.id }).subscribe({
        next: () => {
          // this.reloadInvoice(false);
        },
        error: (error: any) => {
          console.error('Error updating account:', error);
          this.loading = false;
        },
      });
    }
  }

  onDeferredChange(event: any): void {
    const isDeferred = event.target.checked;
    if (this.currentInvoice?.id) {
      this.loading = true;
      this.invoiceService.updateInvoice(this.currentInvoice.id, { deferred: isDeferred }).subscribe({
        next: () => {
          this.isDeferred = isDeferred;
          this.reloadInvoice(false);
        },
        error: error => {
          console.error('Error updating deferred status:', error);
          this.loading = false;
        },
      });
    }
  }
}
