import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

export interface TableColumn {
  field: string;
  header: string;
  type?: 'text' | 'number' | 'currency' | 'date' | 'actions';
  width?: string;
  sortable?: boolean;
  filterable?: boolean;
  editable?: boolean;
  format?: string | ((value: any) => string);
}

@Component({
  selector: 'app-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.scss'],
})
export class DataTableComponent implements OnInit, OnChanges {
  @Input() columns: TableColumn[] = [];
  @Input() data: any[] = [];
  @Input() loading: boolean = false;
  @Input() pageSize: number = 10;
  @Input() totalItems: number = 0;
  @Input() currentPage: number = 0;
  @Input() showPagination: boolean = true;
  @Input() showSearch: boolean = true;
  @Input() emptyMessage: string = '';

  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<any>();
  @Output() pageChange = new EventEmitter<number>();
  @Output() pageSizeChange = new EventEmitter<number>();

  @Output() sortChange = new EventEmitter<{ field: string; direction: string }>();
  @Output() search = new EventEmitter<string>();
  @Output() valueChange = new EventEmitter<{ row: any; field: string; value: any }>();
  @Output() rowClick = new EventEmitter<any>();
  @Output() rowSelect = new EventEmitter<any>();

  filteredData: any[] = [];
  searchText: string = '';
  pageSizeOptions: number[] = [10, 20, 50, 100];
  private searchSubject = new Subject<string>();
  Math = Math;

  constructor(private translateService: TranslateService, private cdr: ChangeDetectorRef) {
    this.searchSubject.pipe(debounceTime(300), distinctUntilChanged()).subscribe(term => {
      this.search.emit(term);
    });
  }

  ngOnInit(): void {
    this.updateFilteredData();
    this.translateEmptyMessage();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data']) {
      this.updateFilteredData();
    }
  }

  private translateEmptyMessage(): void {
    if (this.emptyMessage.startsWith('dataTable.')) {
      this.translateService.get(this.emptyMessage).subscribe(translated => {
        this.emptyMessage = translated;
      });
    }
  }

  onSearch(value: string): void {
    this.searchSubject.next(value);
    this.searchText = value;
    this.updateFilteredData();
  }

  onValueChange(row: any, field: string, value: any): void {
    this.valueChange.emit({ row, field, value });
  }

  onEditClick(row: any): void {
    this.edit.emit(row);
  }

  onDeleteClick(row: any): void {
    this.delete.emit(row);
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.pageSizeChange.emit(this.pageSize);
  }

  onPageNumberChange(page: number): void {
    if (page >= 0 && page < Math.ceil(this.totalItems / this.pageSize)) {
      this.currentPage = page;
      this.pageChange.emit(page);
    }
  }

  onSort(column: TableColumn): void {
    if (!column.sortable) return;
    // Implement sorting logic here
    this.sortChange.emit({ field: column.field, direction: 'ASC' });
  }

  onRowClick(row: any): void {
    this.rowClick.emit(row);
  }

  onRowSelect(row: any): void {
    this.rowSelect.emit(row);
  }

  getPages(): number[] {
    const totalPages = Math.ceil(this.totalItems / this.pageSize);
    const currentPage = this.currentPage;
    const pages: number[] = [];

    if (totalPages <= 7) {
      return Array.from({ length: totalPages }, (_, i) => i);
    }

    // Always show first page
    pages.push(0);

    if (currentPage > 3) {
      pages.push(-1); // Separator
    }

    // Show pages around current page
    for (let i = Math.max(1, currentPage - 1); i <= Math.min(totalPages - 2, currentPage + 1); i++) {
      pages.push(i);
    }

    if (currentPage < totalPages - 4) {
      pages.push(-1); // Separator
    }

    // Always show last page
    if (totalPages > 1) {
      pages.push(totalPages - 1);
    }

    return pages;
  }

  formatCellValue(value: any, column: TableColumn): string {
    if (!value) return '';

    if (column.format) {
      if (typeof column.format === 'function') {
        return column.format(value);
      }
      return column.format;
    }

    switch (column.type) {
      case 'currency':
        return this.formatCurrency(value);
      case 'date':
        return this.formatDate(value);
      default:
        return value.toString();
    }
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat(this.translateService.currentLang, {
      style: 'currency',
      currency: 'LYD',
    }).format(value);
  }

  private formatDate(value: string | Date): string {
    return new Intl.DateTimeFormat(this.translateService.currentLang).format(new Date(value));
  }

  private updateFilteredData(): void {
    this.filteredData = this.data ? [...this.data] : [];
    if (this.searchText) {
      this.filterData();
    }
  }

  private filterData(): void {
    if (!this.searchText.trim()) {
      this.filteredData = [...this.data];
      return;
    }

    const searchTerm = this.searchText.toLowerCase().trim();
    this.filteredData = this.data.filter(item => {
      return this.columns.some(column => {
        const value = item[column.field];
        if (value == null) return false;
        return value.toString().toLowerCase().includes(searchTerm);
      });
    });
  }

  refresh() {
    this.updateFilteredData();
    this.cdr.detectChanges();
  }
}
