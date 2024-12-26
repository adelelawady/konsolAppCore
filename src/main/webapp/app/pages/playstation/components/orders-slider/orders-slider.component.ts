import { Component, OnInit, OnDestroy, HostListener, ElementRef } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { Subscription } from 'rxjs';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { CategoryItem } from 'app/core/konsolApi/model/categoryItem';
import { ItemSimpleDTO } from 'app/core/konsolApi/model/itemSimpleDTO';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { CreateInvoiceItemDTO } from 'app/core/konsolApi/model/createInvoiceItemDTO';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PlaystationContainer } from 'app/core/konsolApi';
import { ActivatedRoute } from '@angular/router';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { ItemDTO } from 'app/core/konsolApi/model/itemDTO';
import { AccountService } from 'app/core/auth/account.service';
import { AuthoritiesConstants } from 'app/config/authorities.constants';

@Component({
  selector: 'jhi-orders-slider',
  templateUrl: './orders-slider.component.html',
  styleUrls: ['./orders-slider.component.scss'],
})
export class OrdersSliderComponent implements OnInit, OnDestroy {
  isVisible = false;
  subscription?: Subscription;
  categories: CategoryItem[] = [];
  selectedCategory?: CategoryItem;
  itemsByCategory: { [key: string]: ItemSimpleDTO[] } = {};
  loading = false;
  selectedDevice: PsDeviceDTO | null = null;
  orderChangeAnimation = false;
  container: PlaystationContainer | null | undefined;
  showEditModal = false;
  selectedItem?: ItemViewDTO;
  isAdmin = false;

  constructor(
    private playstationService: PlaystationService,
    private elementRef: ElementRef,
    private itemResourceService: ItemResourceService,
    private playstationResourceService: PlaystationResourceService,
    private route: ActivatedRoute,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    // Check admin status directly
    this.isAdmin = this.accountService.hasAnyAuthority([
      AuthoritiesConstants.ADMIN, 
      AuthoritiesConstants.SUPER_ADMIN
    ]);

    // Subscribe to account changes to update admin status
    this.subscription = this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        this.isAdmin = this.accountService.hasAnyAuthority([
          AuthoritiesConstants.ADMIN,
          AuthoritiesConstants.SUPER_ADMIN
        ]);
      }
    });

    // First try to get container from resolver data
    this.route.data.subscribe(data => {
      if (data['container']) {
        // eslint-disable-next-line no-console
        this.container = data['container'];
        this.categories = this.container?.acceptedOrderCategories.map(cat => ({ name: cat })) ?? [];
        if (this.categories.length > 0 && !this.selectedCategory) {
          this.selectCategory(this.categories[0]);
        }
        return;
      }
    });

    this.subscription = this.playstationService.showOrders$.subscribe((show: boolean) => {
      this.isVisible = show;
      if (show) {
        // this.loadCategories();
      }
    });

    this.subscription.add(
      this.playstationService.selectedDevice$.subscribe(device => {
        this.selectedDevice = device;
      })
    );

    this.subscription.add(
      this.playstationService.orderChange$.subscribe(() => {
        this.triggerOrderChangeAnimation();
      })
    );
  }

  loadCategories(): void {
    this.itemResourceService.getAllItemsCategories().subscribe({
      next: (categories: CategoryItem[]) => {
        this.categories = categories;
        if (categories.length > 0 && !this.selectedCategory) {
          this.selectCategory(categories[0]);
        }
        this.loading = false;
      },
      error: error => {
        console.error('Error loading categories:', error);
        this.loading = false;
      },
    });
  }

  loadItemsForCategory(categoryName: string): void {
    this.loading = true;
    const categoryItem: CategoryItem = {
      name: categoryName,
    };

    if (!this.container || !this.container.id) {
      return;
    }
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    this.itemResourceService.getAllItemsByCategoryAndContainerIdPrice(this.container.id ?? 'null', categoryItem).subscribe({
      next: (items: ItemSimpleDTO[]) => {
        this.itemsByCategory[categoryName] = items;
        this.loading = false;
      },
      error: error => {
        console.error(`Error loading items for category ${categoryName}:`, error);
        this.loading = false;
      },
    });
  }

  selectCategory(category: CategoryItem): void {
    this.selectedCategory = category;
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
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

    this.playstationResourceService.addOrderToDevice(this.selectedDevice.id, orderItem).subscribe({
      next: updatedDevice => {
        this.playstationService.selectDevice(updatedDevice);
        this.playstationService.reloadDevices();
        this.playstationService.notifyOrderChange();
        console.log('Order added successfully');
      },
      error(error) {
        console.error('Error adding order:', error);
      },
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
    if (!categoryName) {
      return [];
    }
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return this.itemsByCategory[categoryName] || [];
  }

  private triggerOrderChangeAnimation(): void {
    this.orderChangeAnimation = true;
    setTimeout(() => {
      this.orderChangeAnimation = false;
    }, 1000); // Animation duration
  }

  editItem(item: ItemSimpleDTO): void {
    event?.stopPropagation();
    
    if (!item.id) {
      console.error('No item ID available');
      return;
    }

    // Load full item details using ItemResource
    this.itemResourceService.getItem(item.id).subscribe({
      next: (fullItem: ItemDTO) => {
        // Convert ItemDTO to ItemViewDTO format for the add-product component
        this.selectedItem = {
          id: fullItem.id,
          name: fullItem.name,
          barcode: fullItem.barcode || '',
          category: fullItem.category || '',
          price1: fullItem.price1?.toString() || '0',
          cost: fullItem.cost || 0,
          qty: fullItem.qty || 0,
          checkQty: fullItem.checkQty || false,
          PriceOptions: fullItem.PriceOptions || [],
        };
        
        this.showEditModal = true;
      },
      error: (error) => {
        console.error('Error loading item details:', error);
        // You may want to show a toastr notification here
      }
    });
  }

  onProductSaved(updatedItem: ItemViewDTO): void {
    this.showEditModal = false;
    if (this.selectedCategory?.name) {
      this.loadItemsForCategory(this.selectedCategory.name);
    }
  }
}
