import { Component, OnInit, OnDestroy, HostListener, ElementRef } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { Subscription } from 'rxjs';
import { trigger } from '@angular/animations';
import { state, style } from '@angular/animations';
import { animate, transition } from '@angular/animations';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { CategoryItem } from 'app/core/konsolApi/model/categoryItem';
import { ItemSimpleDTO } from 'app/core/konsolApi/model/itemSimpleDTO';

@Component({
  selector: 'jhi-orders-slider',
  templateUrl: './orders-slider.component.html',
  styleUrls: ['./orders-slider.component.scss'],
  animations: [
    trigger('slideInOut', [
      state('out', style({
        transform: 'translateY(100%)',
        visibility: 'hidden'
      })),
      state('in', style({
        transform: 'translateY(0)',
        visibility: 'visible'
      })),
      transition('out => in', [
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')
      ]),
      transition('in => out', [
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')
      ])
    ])
  ]
})
export class OrdersSliderComponent implements OnInit, OnDestroy {
  isVisible = false;
  private subscription?: Subscription;
  categories: CategoryItem[] = [];
  selectedCategory?: CategoryItem;
  itemsByCategory: { [key: string]: ItemSimpleDTO[] } = {};

  constructor(
    private playstationService: PlaystationService, 
    private elementRef: ElementRef,
    private itemResourceService: ItemResourceService
  ) {}

  ngOnInit(): void {
    this.subscription = this.playstationService.showOrders$.subscribe(
      (show: boolean) => {
        this.isVisible = show;
        if (show) {
          this.loadCategories();
        }
      }
    );
  }

  private loadCategories(): void {
    this.itemResourceService.getAllItemsCategories().subscribe({
      next: (categories: CategoryItem[]) => {
        this.categories = categories;
        // Load items for each category
        categories.forEach(category => {
          if (category.name) {
            this.loadItemsForCategory(category.name);
          }
        });
      },
      error: (error) => {
        console.error('Error loading categories:', error);
      }
    });
  }

  private loadItemsForCategory(categoryName: string): void {
    this.itemResourceService.getItemsByCategory(categoryName).subscribe({
      next: (items: ItemSimpleDTO[]) => {
        this.itemsByCategory[categoryName] = items;
      },
      error: (error) => {
        console.error(`Error loading items for category ${categoryName}:`, error);
      }
    });
  }

  selectCategory(category: CategoryItem): void {
    this.selectedCategory = category;
    // Load items if not already loaded
    if (category.name && !this.itemsByCategory[category.name]) {
      this.loadItemsForCategory(category.name);
    }
  }

  addToOrder(item: ItemSimpleDTO): void {
    // Implement order addition logic here
    console.log('Adding item to order:', item);
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event): void {
    if (!this.elementRef.nativeElement.contains(event.target) && this.isVisible) {
      this.close();
    }
  }

  toggle(): void {
    if (this.isVisible) {
      this.playstationService.hideOrders();
    } else {
      this.playstationService.showOrdersList();
    }
  }

  close(): void {
    this.playstationService.hideOrders();
  }

  getCategoryItems(categoryName: string | undefined): ItemSimpleDTO[] {
    if (!categoryName) return [];
    return this.itemsByCategory[categoryName] || [];
  }
}
