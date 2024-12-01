import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';
import { StoreItemIdOnlyDTO } from 'app/core/konsolApi/model/storeItemIdOnlyDTO';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { StoreResourceService } from 'app/core/konsolApi/api/storeResource.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
})
export class AddProductComponent {
  @Input() set item(value: ItemViewDTO | undefined) {
    if (value) {
      this._item = { ...value };
      this.isEditMode = !!value.id;
      if (this.isEditMode) {
        this.loadStoreItems();
      }
    } else {
      this._item = this.getDefaultItem();
      this.isEditMode = false;
    }
    this.initForm();
  }
  get item(): ItemViewDTO {
    return this._item;
  }

  @Input() show = false;
  @Output() showChange = new EventEmitter<boolean>();
  @Output() saved = new EventEmitter<void>();

  form!: FormGroup;
  isEditMode = false;
  loading = false;
  stores: StoreDTO[] = [];
  storeItems: { [key: string]: number } = {};

  private _item: ItemViewDTO = this.getDefaultItem();

  constructor(
    private fb: FormBuilder,
    private itemService: ItemResourceService,
    private storeService: StoreResourceService,
    private toastr: ToastrService,
    private translate: TranslateService
  ) {
    this.loadStores();
    this.initForm();
  }

  private getDefaultItem(): ItemViewDTO {
    return {
      name: '',
      barcode: '',
      category: '',
      price1: '0',
      cost: 0,
      qty: 0,
      checkQty: false,
    };
  }

  private initForm(): void {
    this.form = this.fb.group({
      name: [this._item.name, Validators.required],
      barcode: [this._item.barcode],
      category: [this._item.category],
      price1: [this._item.price1, Validators.required],
      cost: [this._item.cost],
      checkQty: [this._item.checkQty],
    });
  }

  private loadStores(): void {
    this.storeService.getAllStores().subscribe({
      next: stores => {
        this.stores = stores;
        // Initialize store quantities to 0
        this.stores.forEach(store => {
          this.storeItems[store.id!] = 0;
        });
      },
      error: error => {
        console.error('Error loading stores:', error);
        this.toastr.error(this.translate.instant('products.messages.loadStoresError'));
      },
    });
  }

  private loadStoreItems(): void {
    if (this._item.id) {
      this.storeService.getAllStoresItemsInAllStoresForItem(this._item.id).subscribe({
        next: storeItems => {
          // Reset store quantities
          this.stores.forEach(store => {
            this.storeItems[store.id!] = 0;
          });
          // Update quantities from response
          storeItems.forEach(storeItem => {
            if (storeItem.store?.id) {
              this.storeItems[storeItem.store.id] = storeItem.qty || 0;
            }
          });
        },
        error: error => {
          console.error('Error loading store items:', error);
          this.toastr.error(this.translate.instant('products.messages.loadStoreItemsError'));
        },
      });
    }
  }

  updateStoreQuantity(storeId: string, qty: number): void {
    if (!this._item.id) return;

    const storeItem: StoreItemIdOnlyDTO = {
      itemId: this._item.id,
      storeId: storeId,
      qty: qty,
    };

    this.storeService.setStoreItem(storeItem).subscribe({
      next: response => {
        this.storeItems[storeId] = response.qty || 0;
        this.toastr.success(this.translate.instant('products.messages.updateStoreSuccess'));
      },
      error: error => {
        console.error('Error updating store quantity:', error);
        this.toastr.error(this.translate.instant('products.messages.updateStoreError'));
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }

    const itemData = {
      ...this._item,
      ...this.form.value,
    };

    this.loading = true;
    const operation =
      this.isEditMode && this.item.id ? this.itemService.updateItem(itemData, this.item.id) : this.itemService.createItem(itemData);

    operation.subscribe({
      next: response => {
        this.toastr.success(
          this.translate.instant(this.isEditMode ? 'products.messages.updateSuccess' : 'products.messages.createSuccess')
        );
        this.loading = false;
        this.closeModal();
        this.saved.emit();
      },
      error: error => {
        console.error('Error saving item:', error);
        this.toastr.error(this.translate.instant(this.isEditMode ? 'products.messages.updateError' : 'products.messages.createError'));
        this.loading = false;
      },
    });
  }

  closeModal(): void {
    this.show = false;
    this.showChange.emit(this.show);
  }
}
