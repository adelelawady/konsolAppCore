import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountUserResourceService } from '../../../core/konsolApi/api/accountUserResource.service';
import { AccountTransactionsContainer } from '../../../core/konsolApi/model/accountTransactionsContainer';
import { PaginationSearchModel } from '../../../core/konsolApi/model/paginationSearchModel';
import { AccountUserDTO } from '../../../core/konsolApi/model/accountUserDTO';
import { TableColumn } from 'app/shared/components/data-table/data-table.component';

@Component({
  selector: 'app-account-details',
  templateUrl: './account-details.component.html',
  styleUrls: ['./account-details.component.scss'],
})
export class AccountDetailsComponent implements OnInit {
  accountId: string;
  account: AccountUserDTO | null = null;
  transactions: any | null = null;
  loading = false;
  currentPage = 0;
  pageSize = 10;
  columns: TableColumn[] = [
    { field: 'pk', header: 'banks.transactions.pk', type: 'text', sortable: true },
    { field: 'createdDate', header: 'banks.transactions.date', type: 'date', sortable: true },
    { field: 'sourceType', header: 'banks.transactions.type', type: 'text', sortable: true },
    { field: 'sourceKind', header: 'banks.transactions.kind', type: 'text', sortable: true },
    { field: 'details', header: 'banks.transactions.details', type: 'text', sortable: true },
    { field: 'moneyIn', header: 'banks.transactions.moneyIn', type: 'currency', sortable: true },
    { field: 'moneyOut', header: 'banks.transactions.moneyOut', type: 'currency', sortable: true },
  ];

  constructor(private route: ActivatedRoute, private accountUserService: AccountUserResourceService) {
    this.accountId = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.loadAccountDetails();
    this.loadTransactions();
  }

  loadAccountDetails(): void {
    this.accountUserService.getAccountUser(this.accountId).subscribe(
      account => {
        this.account = account;
      },
      error => {
        console.error('Error loading account details:', error);
      }
    );
  }

  loadTransactions(): void {
    this.loading = true;
    const searchModel: PaginationSearchModel = {
      page: this.currentPage,
      size: this.pageSize,
      sortField: 'createdDate',
      sortOrder: 'ASC',
    };

    this.accountUserService.processAccountTransactions(this.accountId, searchModel).subscribe(
      data => {
        this.transactions = data;
        this.loading = false;
      },
      error => {
        console.error('Error loading transactions:', error);
        this.loading = false;
      }
    );
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadTransactions();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadTransactions();
  }

  onSortChange(event: any): void {
    const searchModel: PaginationSearchModel = {
      page: 0,
      size: 10,
      sortField: event.field,
      sortOrder: event.order === 1 ? 'asc' : 'desc',
    };

    this.accountUserService.processAccountTransactions(this.accountId, searchModel).subscribe(
      data => {
        this.transactions = data;
      },
      error => {
        console.error('Error loading transactions:', error);
      }
    );
  }
}
