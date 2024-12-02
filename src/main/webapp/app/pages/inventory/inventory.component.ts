import { Component, OnInit, ViewChild } from '@angular/core';
import { StoreResourceService } from '../../core/konsolApi/api/storeResource.service';
import { StoreDTO } from '../../core/konsolApi/model/storeDTO';
import { StoreItemDTO } from '../../core/konsolApi/model/storeItemDTO';
import { TableColumn } from '../../shared/components/data-table/table-column.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent } from '../../shared/components/data-table/data-table.component';
import { NgForm } from '@angular/forms';
import { StoreItemIdOnlyDTO } from '../../core/konsolApi/model/storeItemIdOnlyDTO';
import { PaginationSearchModel } from '../../core/konsolApi/model/paginationSearchModel';

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
  currentPage = 0;
  pageSize = 10;
  totalItems = 0;
  searchText = '';
  sortField = 'id';
  sortOrder = 'ASC';
  newStore: StoreDTO = {
    name: ''
  };

  // Columns for store items table
  columns: TableColumn[] = [
    { 
      field: 'item', 
      header: 'inventory.fields.itemName', 
      sortable: true,
      format: (row: any) => row.name || ''
    },
    { 
      field: 'qty', 
      header: 'inventory.fields.quantity', 
      type: 'number', 
      sortable: true,
      editable: true
    },
    { 
      field: 'item', 
      header: 'inventory.fields.price', 
      type: 'currency', 
      sortable: true,
      format: (row: any) => row.price1?.toString() || '0'
    },
    { 
      field: 'item', 
      header: 'inventory.fields.cost', 
      type: 'currency', 
      sortable: true,
      format: (row: any) => row.cost?.toString() || '0'
    },
    { 
      field: 'actions', 
      header: '', 
      type: 'actions', 
      width: '120px'
    },
  ];

  transformedStoreItems: any[] = [];

  constructor(private storeService: StoreResourceService, private toastr: ToastrService, private translate: TranslateService) {}

  ngOnInit(): void {
    this.loadStores();
  }

  loadStores(): void {
    this.loading = true;
    this.storeService.getAllStores().subscribe({
      next: stores => {
        this.stores = stores.map(store => ({
          ...store,
          name: store.name || `Store_${store.id}`
        }));
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

    const searchModel: PaginationSearchModel = {
      page: this.currentPage,
      size: this.pageSize,
      sortField: this.sortField,
      sortOrder: this.sortOrder,
      searchText: this.searchText
    };

    this.storeService.getStoresItemsForStore(store.id, searchModel).subscribe({
      next: items => {
        this.storeItems = items.map(item => ({
          ...item,
          name: item.item?.name || '',
          price1: item.item?.price1,
          cost: item.item?.cost
        })) || [];
        this.transformedStoreItems = this.storeItems.map((item:any) => ({
          id: item.id,
          name: item.name,
          qty: item.qty,
          price: item.price1,
          cost: item.cost,
          originalItem: item
        }));
        if (this.dataTable) {
          this.dataTable.data = this.transformedStoreItems;
          this.dataTable.refresh();
        }
        this.loading = false;
      },
      error: () => {
        this.showError('loadItemsError');
        this.loading = false;
      },
    });
  }

  onAddStore(): void {
    this.showAddStoreModal = true;
  }

  onAddStoreSubmit(form: NgForm): void {
    if (!form.valid) return;

    this.storeService.createStore(this.newStore).subscribe({
      next: () => {
        this.showSuccess('storeAdded');
        this.loadStores();
        this.showAddStoreModal = false;
        this.newStore = { name: '' };
      },
      error: () => {
        this.showError('addError');
      }
    });
  }

  onEditStore(store: StoreDTO): void {
    this.editingStore = { ...store };
    this.showEditStoreModal = true;
  }

  onEditStoreSubmit(form: NgForm): void {
    if (!form.valid || !this.editingStore?.id) return;

    this.storeService.updateStore(this.editingStore.id, this.editingStore).subscribe({
      next: () => {
        this.showSuccess('storeUpdated');
        this.loadStores();
        this.showEditStoreModal = false;
        this.editingStore = undefined;
      },
      error: () => {
        this.showError('updateError');
      }
    });
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

  onEditClick(item: any): void {
    if (!item.id || !item.store?.id || !item.item?.id) return;

    const storeItemIdOnly: StoreItemIdOnlyDTO = {
      storeId: item?.store.id,
      itemId: item?.item.id,
      qty: item?.qty
    };

    this.storeService.setStoreItem(storeItemIdOnly).subscribe({
      next: () => {
        this.showSuccess('itemUpdated');
        this.loadStoreItems(this.selectedStore!);
      },
      error: () => {
        this.showError('updateError');
      }
    });
  }

  onDeleteClick(item: any): void {
    if (confirm(this.translate.instant('inventory.messages.deleteItemConfirm'))) {
      this.deleteStoreItem(item);
    }
  }

  deleteStoreItem(item: any): void {
    console.log(item);
    if ( !item.store?.id || !item.item?.id) return;

    const storeItemIdOnly: StoreItemIdOnlyDTO = {
      storeId: item?.store.id,
      itemId: item?.item.id,
      qty: item?.qty
    };

    this.storeService.setStoreItem(storeItemIdOnly).subscribe({
      next: () => {
        this.showSuccess('itemDeleted');
        this.loadStoreItems(this.selectedStore!);
      },
      error: () => {
        this.showError('deleteError');
      }
    });
  }

  onValueChange(event: { row: any; field: string; value: any }): void {
    if (event.field === 'qty' && this.selectedStore) {
      const item = this.storeItems.find(i => i.id === event.row.id);
      if (item) {
        item.qty = event.value;
        this.onEditClick(item);
      }
    }
  }

  onSearch(term: string): void {
    this.searchText = term;
    this.currentPage = 0; // Reset to first page when searching
    if (this.selectedStore) {
      this.loadStoreItems(this.selectedStore);
    }
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    if (this.selectedStore) {
      this.loadStoreItems(this.selectedStore);
    }
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0; // Reset to first page when changing page size
    if (this.selectedStore) {
      this.loadStoreItems(this.selectedStore);
    }
  }

  onSortChange(event: { field: string; direction: string }): void {
    this.sortField = event.field;
    this.sortOrder = event.direction.toUpperCase();
    if (this.selectedStore) {
      this.loadStoreItems(this.selectedStore);
    }
  }

  private showError(key: string): void {
    this.toastr.error(this.translate.instant(`inventory.messages.${key}`));
  }

  private showSuccess(key: string): void {
    this.toastr.success(this.translate.instant(`inventory.messages.${key}`));
  }
}
