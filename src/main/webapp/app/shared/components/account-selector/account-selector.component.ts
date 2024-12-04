import { Component, OnInit, Output, EventEmitter, ViewChild, ElementRef, Input, OnChanges, SimpleChanges } from '@angular/core';
import { AccountUserContainer } from 'app/core/konsolApi/model/accountUserContainer';
import { AccountUserResourceService } from 'app/core/konsolApi/api/accountUserResource.service';
import { AccountUserSearchModel } from 'app/core/konsolApi/model/accountUserSearchModel';
import { AccountUserDTO } from 'app/core/konsolApi/model/accountUserDTO';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';

@Component({
  selector: 'jhi-account-selector',
  templateUrl: './account-selector.component.html',
  styleUrls: ['./account-selector.component.scss'],
})
export class AccountSelectorComponent implements OnInit, OnChanges {
  @Output() accountSelected = new EventEmitter<AccountUserDTO | undefined>();
  @Input() selectedAccountId: string | null = null;
  @ViewChild('searchInput') searchInput!: ElementRef;

  searchTerm = '';
  searchResults: AccountUserDTO[] = [];
  selectedAccount: AccountUserDTO | null = null;
  showResults = false;
  showSelectedAccount = false;
  isLoading = false;
  private searchSubject = new Subject<string>();

  constructor(private accountUserResourceService: AccountUserResourceService) {}

  ngOnInit(): void {
    this.setupSearch();
    this.initialLoad();
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('selectedAccountId', changes['selectedAccountId'].currentValue);
    if (changes['selectedAccountId']) {
      if (changes['selectedAccountId'].currentValue) {
        this.loadSelectedAccount(changes['selectedAccountId'].currentValue);
      } else {
        // Clear selection if selectedAccountId is null
        this.clearSelection();
      }
    }
  }

  private loadSelectedAccount(accountId: string): void {
    this.isLoading = true;
    this.accountUserResourceService.getAccountUser(accountId).subscribe({
      next: account => {
        if (account && account.id) {
          this.selectedAccount = account;
          this.showSelectedAccount = true;
          this.showResults = false;
        } else {
          this.clearSelection();
        }
        this.isLoading = false;
      },
      error: error => {
        console.error('Error loading account:', error);
        this.clearSelection();
        this.isLoading = false;
      },
    });
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
    this.isLoading = true;
    const searchModel: AccountUserSearchModel = {
      page: 0,
      size: 10,
    };

    if (term && term.length > 0) {
      searchModel.name = term;
    }

    this.accountUserResourceService.searchAccountUsers(searchModel).subscribe({
      next: (response: AccountUserContainer) => {
        if (response && response.result) {
          this.searchResults = response.result;
          this.showResults = true;
        }
        this.isLoading = false;
      },
      error: error => {
        console.error('Error searching accounts:', error);
        this.isLoading = false;
        this.searchResults = [];
      },
    });
  }

  private initialLoad(): void {
    const searchModel: AccountUserSearchModel = {
      page: 0,
      size: 10,
    };

    this.accountUserResourceService.searchAccountUsers(searchModel).subscribe({
      next: (response: AccountUserContainer) => {
        if (response && response.result) {
          this.searchResults = response.result;
          // Don't show results on initial load
          this.showResults = false;
        }
      },
      error: error => {
        this.searchResults = [];
      },
    });
  }

  selectAccount(account: AccountUserDTO): void {
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
    setTimeout(() => {
      this.searchInput?.nativeElement?.focus();
    });
  }

  onSelectedAccountClick(): void {
    this.clearSelection();
  }

  onSearchFocus(): void {
    if (!this.showSelectedAccount) {
      // Show results only when focused and we have results
      if (this.searchResults && this.searchResults.length > 0) {
        this.showResults = true;
      }
    }
  }

  onFocusOut(event: FocusEvent): void {
    const relatedTarget = event.relatedTarget as HTMLElement;
    if (!relatedTarget?.closest('.search-results-popup')) {
      setTimeout(() => {
        this.showResults = false;
        // If no account is selected and search term is empty, emit undefined
        if (!this.selectedAccount && !this.searchTerm) {
          this.accountSelected.emit(undefined);
        }
      }, 200);
    }
  }
}
