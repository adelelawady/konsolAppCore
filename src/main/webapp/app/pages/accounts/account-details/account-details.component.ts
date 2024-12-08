import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountUserResourceService } from '../../../core/konsolApi/api/accountUserResource.service';
import { AccountTransactionsContainer } from '../../../core/konsolApi/model/accountTransactionsContainer';
import { PaginationSearchModel } from '../../../core/konsolApi/model/paginationSearchModel';
import { AccountUserDTO } from '../../../core/konsolApi/model/accountUserDTO';
import { TableColumn } from 'app/shared/components/data-table/table-column.model';

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
    { field: 'pk', header: 'accounts.transactions.fields.pk', type: 'text', sortable: true },
    { field: 'createdDate', header: 'accounts.transactions.fields.date', type: 'date', sortable: true },
    { field: 'sourceType', header: 'accounts.transactions.fields.type', type: 'text', sortable: true },
    { field: 'sourceKind', header: 'accounts.transactions.fields.kind', type: 'text', sortable: true },
    { field: 'sourcePk', header: 'accounts.transactions.fields.sourcePk', type: 'text', sortable: true },
    { field: 'details', header: 'accounts.transactions.fields.details', type: 'text', sortable: true },
    { field: 'moneyIn', header: 'accounts.transactions.fields.moneyIn', type: 'currency', sortable: true },
    { field: 'moneyOut', header: 'accounts.transactions.fields.moneyOut', type: 'currency', sortable: true },
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
}
