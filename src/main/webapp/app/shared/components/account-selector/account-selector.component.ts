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
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedAccountId'] && changes['selectedAccountId'].currentValue) {
      this.loadSelectedAccount(changes['selectedAccountId'].currentValue);
    }
  }

  private loadSelectedAccount(accountId: string): void {
    this.isLoading = true;
    this.accountUserResourceService.getAccountUser(accountId).subscribe({
      next: account => {
        if (account) {
          this.selectAccount(account);
        }
        this.isLoading = false;
      },
      error: error => {
        console.error('Error loading account:', error);
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
      this.showResults = true;
    }
  }

  onFocusOut(event: FocusEvent): void {
    const relatedTarget = event.relatedTarget as HTMLElement;
    if (!relatedTarget?.closest('.search-results-popup')) {
      setTimeout(() => {
        this.showResults = false;
      }, 200);
    }
  }
}
