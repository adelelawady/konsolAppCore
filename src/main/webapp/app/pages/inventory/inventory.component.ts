import { Component, OnInit, ViewChild } from '@angular/core';
import { StoreResourceService } from '../../core/konsolApi/api/storeResource.service';
import { StoreDTO } from '../../core/konsolApi/model/storeDTO';
import { StoreItemDTO } from '../../core/konsolApi/model/storeItemDTO';
import { TableColumn } from '../../shared/components/data-table/table-column.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent } from '../../shared/components/data-table/data-table.component';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'jhi-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.scss'],
})
export class InventoryComponent implements OnInit {
  @ViewChild('dataTable') dataTable!: DataTableComponent;

  stores: StoreDTO[] = [];
  storeItems: StoreItemDTO[] = [];
  selectedStore?: StoreDTO;
  loading = false;
  showAddStoreModal = false;
  showEditStoreModal = false;
  editingStore?: StoreDTO;

  // Columns for store items table
  columns: TableColumn[] = [
    { field: 'item.name', header: 'inventory.fields.itemName', sortable: true },
    { field: 'item.barcode', header: 'inventory.fields.barcode', sortable: true },
    { field: 'qty', header: 'inventory.fields.quantity', type: 'number', sortable: true },
    { field: 'item.price1', header: 'inventory.fields.price', type: 'currency', sortable: true },
    { field: 'item.cost', header: 'inventory.fields.cost', type: 'currency', sortable: true },
    { field: 'actions', header: '', type: 'actions', width: '120px' },
  ];

  constructor(private storeService: StoreResourceService, private toastr: ToastrService, private translate: TranslateService) {}

  ngOnInit(): void {
    this.loadStores();
  }

  loadStores(): void {
    this.loading = true;
    this.storeService.getAllStores().subscribe({
      next: stores => {
        this.stores = stores;
        this.loading = false;
      },
      error: () => {
        this.showError('loadError');
        this.loading = false;
      },
    });
  }

  loadStoreItems(store: StoreDTO): void {
    if (!store.id) return;

    this.loading = true;
    this.selectedStore = store;

    /*
    // Here you would call your API to get store items
    // For now using a placeholder API call
    this.storeService.getStoreItems(store.id).subscribe({
      next: items => {
        this.storeItems = items;
        if (this.dataTable) {
          this.dataTable.refresh();
        }
        this.loading = false;
      },
      error: () => {
        this.showError('loadItemsError');
        this.loading = false;
      },
    });
    */
  }

  onAddStore(): void {
    this.showAddStoreModal = true;
  }

  onEditStore(store: StoreDTO): void {
    this.editingStore = { ...store };
    this.showEditStoreModal = true;
  }

  onDeleteStore(store: StoreDTO): void {
    if (confirm(this.translate.instant('inventory.messages.deleteStoreConfirm'))) {
      this.storeService.deleteStore(store.id!).subscribe({
        next: () => {
          this.showSuccess('storeDeleted');
          this.loadStores();
          if (this.selectedStore?.id === store.id) {
            this.selectedStore = undefined;
            this.storeItems = [];
          }
        },
        error: () => {
          this.showError('deleteError');
        },
      });
    }
  }

  onEditSubmit(form: NgForm): void {
    if (form.valid && this.editingStore) {
      this.saveStore(this.editingStore);
    }
  }

  saveStore(store: StoreDTO): void {
    const operation = store.id ? this.storeService.updateStore(store, store.id) : this.storeService.createStore(store);

    operation.subscribe({
      next: () => {
        this.showSuccess(store.id ? 'storeUpdated' : 'storeCreated');
        this.loadStores();
        this.showAddStoreModal = false;
        this.closeEditModal();
      },
      error: () => {
        this.showError('saveError');
      },
    });
  }

  closeEditModal(): void {
    this.showEditStoreModal = false;
    this.editingStore = undefined;
  }

  private showError(key: string): void {
    this.toastr.error(this.translate.instant(`inventory.messages.${key}`));
  }

  private showSuccess(key: string): void {
    this.toastr.success(this.translate.instant(`inventory.messages.${key}`));
  }
}
