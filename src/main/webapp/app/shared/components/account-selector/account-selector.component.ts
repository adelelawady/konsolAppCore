import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { AccountUserContainer } from 'app/core/konsolApi/model/accountUserContainer';
import { AccountUserResourceService } from 'app/core/konsolApi/api/accountUserResource.service';
import { AccountUserSearchModel } from 'app/core/konsolApi/model/accountUserSearchModel';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';

@Component({
  selector: 'jhi-account-selector',
  templateUrl: './account-selector.component.html',
  styleUrls: ['./account-selector.component.scss'],
})
export class AccountSelectorComponent implements OnInit {
  @Output() accountSelected = new EventEmitter<AccountUserContainer | undefined>();
  @ViewChild('searchInput') searchInput!: ElementRef;

  searchTerm = '';
  searchResults: any[] = [];
  selectedAccount: any | null = null;
  showResults = false;
  showSelectedAccount = false;
  isLoading = false;
  private searchSubject = new Subject<string>();

  constructor(private accountUserResourceService: AccountUserResourceService) {}

  ngOnInit(): void {
    this.setupSearch();
  }

  private setupSearch(): void {
    this.searchSubject.pipe(debounceTime(300), distinctUntilChanged()).subscribe(term => {
      this.performSearch(term);
    });
  }

  onSearchInput(event: Event): void {
    const term = (event.target as HTMLInputElement).value;
    this.searchSubject.next(term);
  }

  private performSearch(term: string): void {
    if (!term || term.length < 2) {
      this.searchResults = [];
      return;
    }

    this.isLoading = true;
    const searchModel: AccountUserSearchModel = {
      name: term,
      page: 0,
      size: 10,
    };

    this.accountUserResourceService.searchAccountUsers(searchModel).subscribe({
      next: (response: any) => {
        this.searchResults = response.result || [];
      },
      error: error => {
        console.error('Error searching accounts:', error);
        this.searchResults = [];
      },
      complete: () => {
        this.isLoading = false;
      },
    });
  }

  selectAccount(account: AccountUserContainer): void {
    this.selectedAccount = account;
    this.showSelectedAccount = true;
    this.showResults = false;
    this.accountSelected.emit(account);
  }

  clearSelection(): void {
    this.selectedAccount = null;
    this.showSelectedAccount = false;
    this.searchTerm = '';
    this.accountSelected.emit(undefined);
  }

  onSearchFocus(): void {
    this.showResults = true;
    if (!this.searchTerm) {
      this.loadInitialResults();
    }
  }

  private loadInitialResults(): void {
    this.isLoading = true;
    const searchModel: AccountUserSearchModel = {
      page: 0,
      size: 10,
    };

    this.accountUserResourceService.searchAccountUsers(searchModel).subscribe({
      next: (response: any) => {
        this.searchResults = response.result || [];
      },
      error: error => {
        console.error('Error loading initial accounts:', error);
        this.searchResults = [];
      },
      complete: () => {
        this.isLoading = false;
      },
    });
  }

  onFocusOut(event: FocusEvent): void {
    setTimeout(() => {
      this.showResults = false;
    }, 200);
  }

  onSelectedAccountClick(): void {
    this.showSelectedAccount = false;
    this.showResults = true;
    this.searchTerm = '';
    setTimeout(() => {
      this.searchInput?.nativeElement?.focus();
    });
  }

  getBalanceBadgeClass(balance?: number): string {
    if (!balance) return 'bg-secondary-subtle text-secondary';
    if (balance > 0) return 'bg-success-subtle text-success';
    if (balance < 0) return 'bg-danger-subtle text-danger';
    return 'bg-secondary-subtle text-secondary';
  }
}
