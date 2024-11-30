import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { StoreResourceService } from 'app/core/konsolApi/api/storeResource.service';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';

@Component({
  selector: 'app-store-selector',
  templateUrl: './store-selector.component.html',
  styleUrls: ['./store-selector.component.scss'],
})
export class StoreSelectorComponent implements OnInit {
  @Output() storeSelected = new EventEmitter<StoreDTO>();

  stores: StoreDTO[] = [];
  selectedStore: StoreDTO | null = null;
  loading = false;
  errorMessage: string | null = null;

  constructor(private storeService: StoreResourceService) {}

  ngOnInit(): void {
    this.loadStores();
  }

  loadStores(): void {
    this.loading = true;
    this.storeService.getAllStores().subscribe({
      next: stores => {
        this.stores = stores;
        this.loading = false;
      },
      error: error => {
        console.error('Error loading stores:', error);
        this.loading = false;
        this.errorMessage = 'Failed to load stores';
      },
    });
  }

  onStoreChange(): void {
    this.storeSelected.emit(this.selectedStore);
  }
}
