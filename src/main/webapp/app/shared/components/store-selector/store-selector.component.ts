import { Component, OnInit, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { StoreResourceService } from 'app/core/konsolApi/api/storeResource.service';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';

@Component({
  selector: 'app-store-selector',
  templateUrl: './store-selector.component.html',
  styleUrls: ['./store-selector.component.scss'],
})
export class StoreSelectorComponent implements OnInit, OnChanges {
  @Output() storeSelected = new EventEmitter<StoreDTO>();
  @Input() selectedStore: StoreDTO | null = null;

  stores: StoreDTO[] = [];
  loading = false;
  errorMessage: string | null = null;

  constructor(private storeService: StoreResourceService) {}

  ngOnInit(): void {
    this.loadStores();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedStore'] && changes['selectedStore'].currentValue) {
      const newStore = changes['selectedStore'].currentValue;
      // Only reload if we don't have the store in our list
      if (this.stores.length > 0 && !this.stores.find(s => s.id === newStore.id)) {
        this.loadStores();
      } else if (this.stores.length === 0) {
        // If we don't have any stores yet, load them
        this.loadStores();
      } else {
        // We have the store in our list, just select it
        const matchingStore = this.stores.find(s => s.id === newStore.id);
        if (matchingStore) {
          this.selectedStore = matchingStore;
        }
      }
    }
  }

  loadStores(): void {
    this.loading = true;
    this.errorMessage = null;

    this.storeService.getAllStores().subscribe({
      next: stores => {
        this.stores = stores;

        // If we have a selected store, find and select it from loaded stores
        if (this.selectedStore) {
          const matchingStore = this.stores.find(s => s.id === this.selectedStore?.id);
          if (matchingStore) {
            this.selectedStore = matchingStore;
          }
        }

        this.loading = false;
      },
      error: error => {
        console.error('Error loading stores:', error);
        this.errorMessage = 'Error loading stores. Please try again.';
        this.loading = false;
      },
    });
  }

  onStoreChange(): void {
    if (this.selectedStore) {
      this.storeSelected.emit(this.selectedStore);
    }
  }
}
