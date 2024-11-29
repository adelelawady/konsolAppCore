import { Component, EventEmitter, OnInit, OnDestroy, Output, ViewChild, ElementRef } from '@angular/core';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { PaginationSearchModel } from 'app/core/konsolApi/model/paginationSearchModel';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { SharedModule } from '../../shared.module';
import { CurrencyConfigService } from 'app/core/config/currency-config.service';

@Component({
  selector: 'jhi-items-search-box',
  templateUrl: './items-search-box.component.html',
  styleUrls: ['./items-search-box.component.scss'],
})
export class ItemsSearchBoxComponent implements OnInit, OnDestroy {
  @Output() itemSelected = new EventEmitter<ItemViewDTO>();
  @Output() onItemHighlighted = new EventEmitter<ItemViewDTO>();
  @ViewChild('searchInput') searchInput?: ElementRef<HTMLInputElement>;

  searchTerm = '';
  searchResults: ItemViewDTO[] = [];
  isLoading = false;
  showResults = false;
  currentCurrency: string;

  // New properties for keyboard navigation
  selectedIndex = -1;
  currentSelectedItem: ItemViewDTO | null = null;
  isSelectedItemValid = false;

  // New properties for selection display
  showSelectedItem = false;
  selectedItem: ItemViewDTO | null = null;

  private searchSubject = new Subject<string>();

  constructor(private itemService: ItemResourceService, private currencyConfig: CurrencyConfigService) {
    this.searchSubject.pipe(debounceTime(300), distinctUntilChanged()).subscribe(term => {
      this.searchItems(term);
    });
    this.currentCurrency = this.currencyConfig.getCurrentCurrencyValue();
  }

  onSelectedItemClick(): void {
    this.clearSelection();
    // Use setTimeout to ensure the input is visible before focusing
    setTimeout(() => {
      this.searchInput?.nativeElement.focus();
    });
  }

  ngOnInit(): void {}

  ngOnDestroy(): void {
    this.searchSubject.unsubscribe();
  }

  onSearchInput(event: any): void {
    const term = event.target.value;
    this.searchTerm = term;
    this.searchSubject.next(term);
  }

  onSearchFocus(): void {
    if (this.showSelectedItem) {
      // If there's a selected item, clear it when focusing on search
      this.clearSelection();
    }
    this.showResults = true;
    if (!this.searchResults.length) {
      this.loadInitialItems();
    }
    this.selectedIndex = -1;
  }

  loadInitialItems(): void {
    this.isLoading = true;
    const searchModel: PaginationSearchModel = {
      page: 0,
      size: 20,
      sortField: 'name',
      sortOrder: 'ASC',
    };

    this.itemService.itemsViewSearchPaginate(searchModel).subscribe({
      next: response => {
        if (response && response.result) {
          this.searchResults = response.result;
          this.showResults = true;
        }
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.searchResults = [];
      },
    });
  }

  searchItems(term: string): void {
    this.isLoading = true;
    const searchModel: PaginationSearchModel = {
      searchText: term,
      page: 0,
      size: 20,
      sortField: 'name',
      sortOrder: 'ASC',
    };

    this.itemService.itemsViewSearchPaginate(searchModel).subscribe({
      next: response => {
        if (response && response.result) {
          this.searchResults = response.result;
          this.showResults = true;
        }
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
        this.searchResults = [];
      },
    });
  }

  selectItem(item: ItemViewDTO): void {
    this.selectedItem = item;
    this.currentSelectedItem = item;
    this.isSelectedItemValid = true;
    this.showSelectedItem = true;
    this.showResults = false;
    this.itemSelected.emit(item);
  }

  clearSelection(): void {
    this.selectedItem = null;
    this.showSelectedItem = false;
    this.currentSelectedItem = null;
    this.isSelectedItemValid = false;
    this.searchTerm = '';
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.searchResults = [];
    this.showResults = false;
    this.selectedIndex = -1;
  }

  onFocusOut(event: FocusEvent): void {
    const relatedTarget = event.relatedTarget as HTMLElement;
    if (!relatedTarget?.closest('.search-results')) {
      setTimeout(() => {
        this.showResults = false;
      }, 200);
    }
  }
}
