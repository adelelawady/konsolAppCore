import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { TableColumn } from 'app/shared/components/data-table/data-table.component';

interface SaleItem {
  id: number;
  name: string;
  unit: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  discount: number;
  netPrice: number;
}

@Component({
  selector: 'jhi-sales',
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.scss'],
})
export class SalesComponent implements OnInit, AfterViewInit {
  @ViewChild('tableContainer') tableContainer!: ElementRef;

  // Properties
  invoiceNumber = 6;
  invoiceDiscount = 0;
  currentQuantity = 0;
  currentPrice = 0;
  currentTotal = 0;
  saleItems: SaleItem[] = [];
  totalQuantity = 0;
  totalAmount = 0;
  totalDiscount = 0;
  netTotal = 0;
  selectedCategory = '';
  selectedBank = '';
  selectedStore = '';
  singleAccount = false;
  selectedCustomer = '';
  clickMode = false;
  loading = false;
  currentDate = new Date();
  userName = 'admin';

  private resizingColumn: HTMLElement | null = null;
  private startX: number = 0;
  private startWidth: number = 0;

  // Add table configuration
  tableColumns: TableColumn[] = [
    { field: 'id', header: '#', type: 'number', width: '60px' },
    { field: 'name', header: 'المنتج', type: 'text' },
    { field: 'unit', header: 'وحدة', type: 'text', width: '100px' },
    { field: 'unitPrice', header: 'سعر الوحدة', type: 'number', editable: true, width: '120px' },
    { field: 'quantity', header: 'كمية', type: 'number', editable: true, width: '100px' },
    { field: 'totalPrice', header: 'اجمالي', type: 'currency', width: '120px' },
    { field: 'discount', header: 'خصم', type: 'number', editable: true, width: '100px' },
    { field: 'netPrice', header: 'صافي', type: 'currency', width: '120px' },
    { field: 'actions', header: 'اجراءات', type: 'actions', width: '100px' },
  ];

  constructor() {
    // Initialize with sample data
    this.saleItems = [
      {
        id: 1,
        name: 'Apple iPhone 14',
        unit: 'piece',
        quantity: 1,
        unitPrice: 999.0,
        totalPrice: 999.0,
        discount: 0,
        netPrice: 999.0,
      },
    ];
  }

  ngOnInit(): void {
    this.calculateTotals();
  }

  ngAfterViewInit() {}

  // Item Management Methods
  editItem(index: number): void {
    // Implement edit functionality
    console.log('Editing item at index:', index);
  }

  removeItem(index: number): void {
    this.saleItems.splice(index, 1);
    this.calculateTotals();
  }

  incrementQuantity(index: number): void {
    this.saleItems[index].quantity++;
    this.updateQuantity(index, this.saleItems[index].quantity);
  }

  decrementQuantity(index: number): void {
    if (this.saleItems[index].quantity > 1) {
      this.saleItems[index].quantity--;
      this.updateQuantity(index, this.saleItems[index].quantity);
    }
  }

  // Update Methods
  updateQuantity(index: number, quantity: number): void {
    this.saleItems[index].quantity = quantity;
    this.updateItemTotal(index);
  }

  updateDiscount(index: number, discount: number): void {
    this.saleItems[index].discount = discount;
    this.updateItemTotal(index);
  }

  updateItemTotal(index: number): void {
    const item = this.saleItems[index];
    item.totalPrice = item.quantity * item.unitPrice;
    item.netPrice = item.totalPrice - item.discount;
    this.calculateTotals();
  }

  // Calculation Methods
  calculateTotals(): void {
    this.totalQuantity = 0;
    this.totalAmount = 0;
    this.totalDiscount = 0;
    this.netTotal = 0;

    this.saleItems.forEach(item => {
      this.totalQuantity += item.quantity;
      this.totalAmount += item.totalPrice;
      this.totalDiscount += item.discount;
    });

    // Add invoice discount to total discount
    this.totalDiscount += this.invoiceDiscount;
    this.netTotal = this.totalAmount - this.totalDiscount;

    // Update current values
    this.currentQuantity = this.totalQuantity;
    this.currentTotal = this.netTotal;
  }

  updateTotals(): void {
    this.calculateTotals();
  }

  // Action Methods
  saveInvoice(): void {
    // Implement save invoice logic
    console.log('Saving invoice...');
    console.log('Items:', this.saleItems);
    console.log('Total Amount:', this.totalAmount);
    console.log('Total Discount:', this.totalDiscount);
    console.log('Net Total:', this.netTotal);
  }

  toggleClickMode(): void {
    this.clickMode = !this.clickMode;
  }

  onItemSelected(item: ItemViewDTO): void {
    if (item) {
      const newItem: SaleItem = {
        id: this.saleItems.length + 1,
        name: item.name,
        unit: '-',
        quantity: 1,
        unitPrice: Number(item.price1) || 0,
        totalPrice: Number(item.price1) || 0,
        discount: 0,
        netPrice: Number(item.price1) || 0,
      };

      this.saleItems.push(newItem);
      this.calculateTotals();
    }
  }

  // Add table event handlers
  onTableEdit(row: any): void {
    this.editItem(row.id - 1);
  }

  onTableDelete(row: any): void {
    this.removeItem(row.id - 1);
  }

  onTableValueChange(event: { row: any; field: string; value: any }): void {
    const index = this.saleItems.findIndex(item => item.id === event.row.id);
    if (index === -1) return;

    switch (event.field) {
      case 'unitPrice':
        this.updateItemTotal(index);
        break;
      case 'quantity':
        this.updateQuantity(index, event.value);
        break;
      case 'discount':
        this.updateDiscount(index, event.value);
        break;
    }
  }
}
