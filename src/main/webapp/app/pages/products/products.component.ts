import { Component, OnInit } from '@angular/core';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { ItemDTO } from 'app/core/konsolApi/model/itemDTO';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { ItemViewDTOContainer } from 'app/core/konsolApi/model/itemViewDTOContainer';
import { TableColumn } from 'app/shared/components/data-table/data-table.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { finalize } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'jhi-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  items: ItemViewDTO[] = [];
  loading = false;
  selectedItem: ItemDTO | null = null;
  showEditModal = false;
  pageSize = 20;
  currentPage = 0;
  totalItems = 0;
  searchTerm = '';
  private searchSubject = new Subject<string>();

  columns: TableColumn[] = [
    {
      field: 'pk',
      header: 'global.fields.pk',
      type: 'text',
      width: '80px',
    },
    {
      field: 'name',
      header: 'products.fields.name',
      type: 'text',
      sortable: true,
    },
    {
      field: 'barcode',
      header: 'products.fields.barcode',
      type: 'text',
    },
    {
      field: 'category',
      header: 'products.fields.category',
      type: 'text',
      sortable: true,
    },
    {
      field: 'qty',
      header: 'products.fields.qty',
      type: 'number',
      sortable: true,
    },
    {
      field: 'price1',
      header: 'products.fields.price',
      type: 'currency',
      sortable: true,
      editable: true,
    },
    {
      field: 'cost',
      header: 'products.fields.cost',
      type: 'currency',
      sortable: true,
      editable: true,
    },
    {
      field: 'actions',
      header: '',
      type: 'actions',
      width: '120px',
    },
  ];

  constructor(private itemService: ItemResourceService, private toastr: ToastrService, private translateService: TranslateService) {
    // Setup search debounce
    this.searchSubject.pipe(debounceTime(300), distinctUntilChanged()).subscribe(term => {
      if (term.trim()) {
        this.loadItems(0, term);
      } else {
        this.loadItems(0);
      }
    });
  }

  ngOnInit(): void {
    this.loadItems(0);
  }

  onSearch(term: string): void {
    this.searchTerm = term;
    this.searchSubject.next(term);
  }

  loadItems(page: number = 0, searchText: string = ''): void {
    if (this.loading) return;

    this.loading = true;
    this.currentPage = page;

    const searchModel = {
      page,
      size: this.pageSize,
      sortField: 'name',
      sortOrder: 'ASC',
      searchText,
    };

    this.itemService
      .itemsViewSearchPaginate(searchModel)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (response: ItemViewDTOContainer) => {
          if (response?.result && Array.isArray(response.result)) {
            this.items = response.result;
            this.totalItems = response.total ?? response.result.length;
          } else {
            console.warn('Unexpected response format:', response);
            this.items = [];
            this.totalItems = 0;
          }
        },
        error: error => {
          console.error('Error loading items:', error);
          this.toastr.error(this.translateService.instant('products.load.error'), this.translateService.instant('error.title'));
          this.items = [];
          this.totalItems = 0;
        },
      });
  }

  onPageChange(page: number): void {
    this.loadItems(page, this.searchTerm);
  }

  onEdit(item: ItemViewDTO): void {
    this.selectedItem = {
      id: item.id,
      name: item.name,
      barcode: item.barcode,
      price1: item.price1,
      qty: item.qty,
      cost: item.cost,
      checkQty: item.checkQty,
    };
    this.showEditModal = true;
  }

  onDelete(item: ItemViewDTO): void {
    if (!item.id) {
      this.toastr.error(this.translateService.instant('products.delete.error'), this.translateService.instant('error.title'));
      return;
    }

    if (confirm(this.translateService.instant('products.delete.confirm'))) {
      this.loading = true;
      this.itemService
        .deleteItem(item.id)
        .pipe(finalize(() => (this.loading = false)))
        .subscribe({
          next: () => {
            this.loadItems(this.currentPage, this.searchTerm);
            this.toastr.success(this.translateService.instant('products.delete.success'), this.translateService.instant('success.title'));
          },
          error: error => {
            console.error('Error deleting item:', error);
            this.toastr.error(this.translateService.instant('products.delete.error'), this.translateService.instant('error.title'));
          },
        });
    }
  }

  saveItem(item: ItemDTO): void {
    if (this.loading) return;

    this.loading = true;
    const operation = item.id ? this.itemService.updateItem(item, item.id) : this.itemService.createItem(item);

    operation
      .pipe(
        finalize(() => {
          this.loading = false;
          this.showEditModal = false;
        })
      )
      .subscribe({
        next: () => {
          this.loadItems(this.currentPage, this.searchTerm);
          this.toastr.success(
            this.translateService.instant(item.id ? 'products.update.success' : 'products.create.success'),
            this.translateService.instant('success.title')
          );
        },
        error: error => {
          console.error('Error saving item:', error);
          this.toastr.error(
            this.translateService.instant(item.id ? 'products.update.error' : 'products.create.error'),
            this.translateService.instant('error.title')
          );
        },
      });
  }

  createNewItem(): void {
    this.selectedItem = {
      name: '',
      price1: '0',
      qty: 0,
      cost: 0,
      checkQty: true,
    };
    this.showEditModal = true;
  }

  refresh(): void {
    this.loadItems(this.currentPage, this.searchTerm);
  }

  onValueChange(event: { row: any; field: string; value: any }): void {
    const { row, field, value } = event;
    const item: ItemDTO = {
      id: row.id,
      name: row.name,
      barcode: row.barcode,
      price1: row.price1,
      qty: row.qty,
      cost: row.cost,
      checkQty: row.checkQty,
      [field]: value,
    };

    this.saveItem(item);
  }

  onRowClick(item: ItemViewDTO): void {
    this.selectedItem = item;
  }

  onRowSelect(item: ItemViewDTO): void {
    this.selectedItem = item;
  }

  clearSelectedItem(): void {
    this.selectedItem = null;
  }
}
