import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';

export interface TableColumn {
  field: string;
  header: string;
  type?: 'text' | 'number' | 'currency' | 'date' | 'actions';
  width?: string;
  sortable?: boolean;
  filterable?: boolean;
  editable?: boolean;
}

@Component({
  selector: 'app-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.scss'],
})
export class DataTableComponent implements OnInit {
  @Input() columns: TableColumn[] = [];
  @Input() data: any[] = [];
  @Input() loading: boolean = false;
  @Input() pageSize: number = 10;
  @Input() pageSizeOptions: number[] = [5, 10, 25, 50];
  @Input() showPagination: boolean = true;
  @Input() showSearch: boolean = true;
  @Input() emptyMessage: string = 'لا توجد بيانات';

  @Output() edit = new EventEmitter<any>();
  @Output() delete = new EventEmitter<any>();
  @Output() pageChange = new EventEmitter<number>();
  @Output() sortChange = new EventEmitter<{ field: string; direction: string }>();
  @Output() rowSelect = new EventEmitter<any>();
  @Output() valueChange = new EventEmitter<{ row: any; field: string; value: any }>();

  filteredData: any[] = [];
  currentPage: number = 1;
  searchText: string = '';

  ngOnInit(): void {
    this.filteredData = [...this.data];
  }

  onSearch(value: string): void {
    this.searchText = value;
    this.filterData();
  }

  filterData(): void {
    if (!this.searchText) {
      this.filteredData = [...this.data];
      return;
    }

    this.filteredData = this.data.filter(item => {
      return this.columns.some(column => {
        const value = item[column.field];
        if (value == null) return false;
        return value.toString().toLowerCase().includes(this.searchText.toLowerCase());
      });
    });
  }

  onEdit(row: any): void {
    this.edit.emit(row);
  }

  onDelete(row: any): void {
    this.delete.emit(row);
  }

  onValueChange(row: any, field: string, value: any): void {
    this.valueChange.emit({ row, field, value });
  }

  formatValue(value: any, column: TableColumn): string {
    if (value == null) return '';

    switch (column.type) {
      case 'currency':
        return new Intl.NumberFormat('ar-SA', { style: 'currency', currency: 'SAR' }).format(value);
      case 'date':
        return new Date(value).toLocaleDateString('ar-SA');
      case 'number':
        return new Intl.NumberFormat('ar-SA').format(value);
      default:
        return value.toString();
    }
  }
}
