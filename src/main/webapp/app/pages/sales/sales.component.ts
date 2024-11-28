import { Component, OnInit } from '@angular/core';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';

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
export class SalesComponent implements OnInit {
  // Current Item Details
  currentQuantity = 0;
  currentPrice = 0;
  currentTotal = 0;

  // Invoice Items
  saleItems: SaleItem[] = [];

  // Summary Totals
  totalQuantity = 0;
  totalAmount = 0;
  totalDiscount = 0;
  netTotal = 0;

  // Selections
  selectedCategory: string = '';
  selectedBank: string = 'Bank_1';
  selectedStore: string = 'Store_1';
  singleAccount: boolean = false;
  selectedCustomer: string = '';

  // UI States
  clickMode: boolean = false;
  loading: boolean = false;
  currentDate = new Date();
  userName = 'admin';

  constructor() {
    // Sample data
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
      // Add more sample items...
    ];
  }

  ngOnInit(): void {
    this.calculateTotals();
  }

  addItem(): void {
    // TODO: Implement add item logic
  }

  removeItem(index: number): void {
    this.saleItems.splice(index, 1);
    this.calculateTotals();
  }

  updateQuantity(index: number, quantity: number): void {
    this.saleItems[index].quantity = quantity;
    this.calculateItemTotal(index);
  }

  updateDiscount(index: number, discount: number): void {
    this.saleItems[index].discount = discount;
    this.calculateItemTotal(index);
  }

  calculateItemTotal(index: number): void {
    const item = this.saleItems[index];
    item.totalPrice = item.quantity * item.unitPrice;
    item.netPrice = item.totalPrice - item.discount;
    this.calculateTotals();
  }

  calculateTotals(): void {
    this.totalQuantity = 0;
    this.totalAmount = 0;
    this.totalDiscount = 0;
    this.netTotal = 0;

    this.saleItems.forEach(item => {
      this.totalQuantity += item.quantity;
      this.totalAmount += item.totalPrice;
      this.totalDiscount += item.discount;
      this.netTotal += item.netPrice;
    });

    // Update current values
    this.currentQuantity = this.totalQuantity;
    this.currentTotal = this.netTotal;
  }

  saveInvoice(): void {
    // TODO: Implement save logic
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
}
