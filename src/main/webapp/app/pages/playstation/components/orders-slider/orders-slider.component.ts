import { Component, OnInit, OnDestroy, HostListener, ElementRef } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { Subscription } from 'rxjs';
import { trigger } from '@angular/animations';
import { state, style } from '@angular/animations';
import { animate, transition } from '@angular/animations';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { CategoryItem } from 'app/core/konsolApi/model/categoryItem';
import { ItemSimpleDTO } from 'app/core/konsolApi/model/itemSimpleDTO';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { CreateInvoiceItemDTO } from 'app/core/konsolApi/model/createInvoiceItemDTO';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';

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
        style({ visibility: 'visible' }),
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')
      ]),
      transition('in => out', [
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)'),
        style({ visibility: 'hidden' })
      ])
    ]),
    trigger('backdropFade', [
      state('out', style({
        opacity: 0,
        visibility: 'hidden'
      })),
      state('in', style({
        opacity: 1,
        visibility: 'visible'
      })),
      transition('out => in', [
        style({ visibility: 'visible' }),
        animate('200ms ease-out')
      ]),
      transition('in => out', [
        animate('200ms ease-in'),
        style({ visibility: 'hidden' })
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
  loading = false;
  private selectedDevice: PsDeviceDTO | null = null;

  constructor(
    private playstationService: PlaystationService, 
    private elementRef: ElementRef,
    private itemResourceService: ItemResourceService,
    private playstationResourceService: PlaystationResourceService
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

    this.subscription.add(
      this.playstationService.selectedDevice$.subscribe(device => {
        this.selectedDevice = device;
      })
    );
  }

  private loadCategories(): void {
    this.itemResourceService.getAllItemsCategories().subscribe({
      next: (categories: CategoryItem[]) => {
        this.categories = categories;
        if (categories.length > 0 && !this.selectedCategory) {
          this.selectCategory(categories[0]);
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        this.loading = false;
      }
    });
  }

  private loadItemsForCategory(categoryName: string): void {
    this.loading = true;
    const categoryItem: CategoryItem = {
      name: categoryName
    };
    
    this.itemResourceService.getItemsByCategory(categoryItem).subscribe({
      next: (items: ItemSimpleDTO[]) => {
        this.itemsByCategory[categoryName] = items;
        this.loading = false;
      },
      error: (error) => {
        console.error(`Error loading items for category ${categoryName}:`, error);
        this.loading = false;
      }
    });
  }

  selectCategory(category: CategoryItem): void {
    this.selectedCategory = category;
    if (category.name && !this.itemsByCategory[category.name]) {
      this.loadItemsForCategory(category.name);
    }
  }

  addToOrder(item: ItemSimpleDTO): void {
    if (!this.selectedDevice?.id || !item.id) {
      console.error('No device selected or invalid item');
      return;
    }

    const orderItem: CreateInvoiceItemDTO = {
      itemId: item.id,
      price: item.price1 ? Number(item.price1) : undefined,
      qty: 1,
    };

    this.playstationResourceService
      .addOrderToDevice(this.selectedDevice.id, orderItem)
      .subscribe({
        next: (updatedDevice) => {
          this.playstationService.selectDevice(updatedDevice);
          console.log('Order added successfully');
        },
        error: (error) => {
          console.error('Error adding order:', error);
        }
      });
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
