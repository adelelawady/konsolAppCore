import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';
import { StoreItemIdOnlyDTO } from 'app/core/konsolApi/model/storeItemIdOnlyDTO';
import { ItemUnitDTO } from 'app/core/konsolApi/model/itemUnitDTO';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { StoreResourceService } from 'app/core/konsolApi/api/storeResource.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { finalize } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
})
export class AddProductComponent implements OnInit {
  @Input() set item(value: ItemViewDTO | undefined) {
    if (value) {
      this._item = {
        ...value,
        checkQty: !!value.checkQty,
      };
      this.isEditMode = !!value.id;
      if (this.isEditMode) {
        this.loadStoreItems();
        this.loadItemUnits();
      }
    } else {
      this._item = this.getDefaultItem();
      this.isEditMode = false;
      this.stores = [];
      this.storeItems = {};
      this.itemUnits = [];
    }
    this.initForm();
  }
  get item(): any {
    return this._item;
  }

  @Input() show = false;
  @Output() showChange = new EventEmitter<boolean>();
  @Output() saved = new EventEmitter<ItemViewDTO>();

  form!: FormGroup;
  isEditMode = false;
  loading = false;
  stores: StoreDTO[] = [];
  storeItems: { [key: string]: number } = {};
  itemUnits: ItemUnitDTO[] = [];
  activeTab = 'quantities';

  private _item: ItemViewDTO = this.getDefaultItem();

  constructor(
    private fb: FormBuilder,
    private itemService: ItemResourceService,
    private storeService: StoreResourceService,
    private toastr: ToastrService,
    private translate: TranslateService,
    private httpClient: HttpClient
  ) {
    this.loadStores();
    this.initForm();
  }

  ngOnInit(): void {
    this.isEditMode = !!this.item?.id;
    if (this.isEditMode) {
      this.form.patchValue(this.item!);
    }
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
      id: [null],
      name: [this._item.name, Validators.required],
      barcode: [this._item.barcode],
      category: [this._item.category],
      price1: [this._item.price1, [Validators.required, Validators.min(0)]],
      cost: [this._item.cost, [Validators.required, Validators.min(0)]],
      checkQty: [!!this._item.checkQty],
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

  private loadItemUnits(): void {
    if (!this._item) return;
    // this.itemUnits = this.item?.itemUnits || [];

    this.itemService.getItemUnits(this.item?.id).subscribe({
      next: units => {
        this.itemUnits = units;
      },
      error: error => {
        console.error('Error loading item units:', error);
        this.toastr.error(this.translate.instant('products.messages.loadUnitsError'));
      },
    });
  }

  addItemUnit(): void {
    const newUnit: ItemUnitDTO = {
      name: '',
      pieces: 1,
      price: this.form.get('price1')?.value || 0,
      cost: this.form.get('cost')?.value || 0,
      basic: false,
    };
    this.itemUnits.push(newUnit);
  }

  removeItemUnit(index: number): void {
    const unit = this.itemUnits[index];
    if (unit.id) {
      this.itemService.deleteUnitItemById(unit.id).subscribe({
        next: () => {
          this.itemUnits.splice(index, 1);
          this.toastr.success(this.translate.instant('products.messages.deleteUnitSuccess'));
        },
        error: error => {
          console.error('Error deleting item unit:', error);
          this.toastr.error(this.translate.instant('products.messages.deleteUnitError'));
        },
      });
    } else {
      this.itemUnits.splice(index, 1);
    }
  }

  updateStoreQuantity(storeId: string, qty: number): void {
    if (!this._item?.id) return;

    const storeItem: StoreItemIdOnlyDTO = {
      itemId: typeof this._item.id === 'object' ? (this._item.id as any).id?.toString() : this._item.id.toString(),
      storeId: storeId?.toString(),
      qty: qty || 0,
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
      id: this.isEditMode && this.item.id ? this.item.id : null,
      ...this._item,
      ...this.form.value,
      itemUnits: this.itemUnits, // Include units directly in the item data
    };

    this.loading = true;
    const operation =
      this.isEditMode && this.item.id ? this.itemService.updateItem(this.item.id, itemData) : this.itemService.createItem(itemData);

    operation.subscribe({
      next: response => {
        this.toastr.success(
          this.translate.instant(this.isEditMode ? 'products.messages.updateSuccess' : 'products.messages.createSuccess')
        );
        this.loading = false;
        this.closeModal();
        this.saved.emit(response);
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
