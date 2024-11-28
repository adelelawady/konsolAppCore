import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';
import { PaginationSearchModel } from 'app/core/konsolApi/model/paginationSearchModel';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { SharedModule } from '../../shared.module';

@Component({
  selector: 'jhi-items-search-box',
  templateUrl: './items-search-box.component.html',
  styleUrls: ['./items-search-box.component.scss'],
})
export class ItemsSearchBoxComponent implements OnInit {
  @Output() itemSelected = new EventEmitter<ItemViewDTO>();

  searchTerm = '';
  searchResults: ItemViewDTO[] = [];
  isLoading = false;
  showResults = false;
  private searchSubject = new Subject<string>();

  constructor(private itemService: ItemResourceService) {
    this.searchSubject.pipe(debounceTime(300), distinctUntilChanged()).subscribe(term => {
      this.searchItems(term);
    });
  }

  ngOnInit(): void {}

  onSearchInput(event: any): void {
    const term = event.target.value;
    this.searchTerm = term;
    this.searchSubject.next(term);
  }

  onSearchFocus(): void {
    this.showResults = true;
    if (!this.searchResults.length) {
      this.loadInitialItems();
    }
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
    this.itemSelected.emit(item);
    this.clearSearch();
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.searchResults = [];
    this.showResults = false;
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
