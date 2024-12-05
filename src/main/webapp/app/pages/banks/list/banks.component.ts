import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { BankDTO, BankBalanceDTO, BankTransactionsDTO, PaginationSearchModel } from '../../../core/konsolApi';
import { BankResourceService } from '../../../core/konsolApi/api/bankResource.service';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { BankCreateModalComponent } from '../create/bank-create-modal.component';
import { TableColumn } from '../../../shared/components/data-table/table-column.model';

@Component({
  selector: 'jhi-banks',
  templateUrl: './banks.component.html',
  styleUrls: ['./banks.component.scss'],
})
export class BanksComponent implements OnInit {
  banks?: BankDTO[];
  isLoading = false;
  searchText = '';
  filteredBanks?: BankDTO[];
  selectedBank?: BankDTO;
  bankAnalysis?: BankBalanceDTO;
  bankTransactions: BankTransactionsDTO[] = [];
  totalTransactions = 0;
  currentPage = 0;
  pageSize = 10;

  // DataTable configuration
  columns: TableColumn[] = [
    { field: 'pk', header: 'banks.transactions.pk', type: 'text', sortable: true },
    { field: 'createdDate', header: 'banks.transactions.date', type: 'date', sortable: true },
    { field: 'sourceType', header: 'banks.transactions.type', type: 'text', sortable: true },
    { field: 'sourceKind', header: 'banks.transactions.kind', type: 'text', sortable: true },
    { field: 'details', header: 'banks.transactions.details', type: 'text', sortable: true },
    { field: 'moneyIn', header: 'banks.transactions.moneyIn', type: 'currency', sortable: true },
    { field: 'moneyOut', header: 'banks.transactions.moneyOut', type: 'currency', sortable: true },
    { field: 'accountName', header: 'banks.transactions.account', type: 'text', sortable: true },
  ];

  constructor(
    private bankService: BankResourceService,
    private router: Router,
    private modalService: NgbModal,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;
    this.bankService
      .getAllBanks()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (banks: BankDTO[]) => {
          this.banks = banks;
          this.filterBanks();
          if (banks.length > 0) {
            this.selectBank(banks[0]);
          }
        },
        error: () => {
          // Handle error
        },
      });
  }

  selectBank(bank: BankDTO): void {
    this.selectedBank = bank;
    if (bank.id) {
      this.isLoading = true;
      this.bankService
        .getBankAnalysis(bank.id)
        .pipe(finalize(() => (this.isLoading = false)))
        .subscribe({
          next: (analysis: BankBalanceDTO) => {
            this.bankAnalysis = analysis;
            this.currentPage = 0;
            this.loadTransactions();
          },
          error: () => {
            console.error('Failed to load bank analysis');
          },
        });
    }
  }

  loadTransactions(): void {
    if (!this.selectedBank?.id) {
      return;
    }

    this.isLoading = true;
    const searchModel: PaginationSearchModel = {
      page: this.currentPage,
      size: this.pageSize,
      sortField: 'createdDate',
      sortOrder: 'ASC',
      searchText: this.searchText,
    };

    this.bankService
      .getBankTransactions(this.selectedBank.id, searchModel)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (response: any) => {
          this.bankTransactions = response.result;
          this.totalTransactions = response.total;
        },
        error: () => {
          console.error('Failed to load bank transactions');
        },
      });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadTransactions();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadTransactions();
  }

  onSortChange(event: { field: string; direction: string }): void {
    const searchModel: PaginationSearchModel = {
      page: this.currentPage,
      size: this.pageSize,
      sortField: event.field,
      sortOrder: event.direction,
      searchText: this.searchText,
    };

    if (this.selectedBank?.id) {
      this.isLoading = true;
      this.bankService
        .getBankTransactions(this.selectedBank.id, searchModel)
        .pipe(finalize(() => (this.isLoading = false)))
        .subscribe({
          next: (response: any) => {
            this.bankTransactions = response.content;
            this.totalTransactions = response.totalElements;
          },
          error: () => {
            console.error('Failed to load bank transactions');
          },
        });
    }
  }

  onSearch(query: string): void {
    this.searchText = query;
    this.filterBanks();
    this.loadTransactions();
  }

  filterBanks(): void {
    if (!this.banks) {
      return;
    }

    if (!this.searchText) {
      this.filteredBanks = this.banks;
      return;
    }

    const searchLower = this.searchText.toLowerCase();
    this.filteredBanks = this.banks.filter(
      bank => bank.name?.toLowerCase().includes(searchLower) || bank.id?.toLowerCase().includes(searchLower)
    );
  }

  delete(id: string | undefined): void {
    if (!id) {
      return;
    }

    if (confirm(this.translateService.instant('entity.delete.warning'))) {
      this.bankService.deleteBank(id).subscribe({
        next: () => {
          this.loadAll();
          if (this.selectedBank?.id === id) {
            this.selectedBank = undefined;
            this.bankAnalysis = undefined;
          }
        },
        error: () => {
          console.error('Failed to delete bank');
        },
      });
    }
  }

  createNew(): void {
    const modalRef = this.modalService.open(BankCreateModalComponent, { size: 'lg' });
    modalRef.closed.subscribe(result => {
      if (result) {
        this.loadAll();
      }
    });
  }
}
