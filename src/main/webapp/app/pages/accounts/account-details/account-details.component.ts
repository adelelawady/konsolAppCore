import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
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
  @ViewChild('sourceTypeTemplate') sourceTypeTemplate: any;
  @ViewChild('sourceKindTemplate') sourceKindTemplate: any;
  @ViewChild('moneyInTemplate') moneyInTemplate: any;
  @ViewChild('moneyOutTemplate') moneyOutTemplate: any;
  @ViewChild('detailsTemplate', { static: true }) detailsTemplate: any;

  accountId: string;
  account: AccountUserDTO | null = null;
  transactions: any | null = null;
  loading = false;
  currentPage = 0;
  pageSize = 10;

  columns: TableColumn[] = [
    {
      field: 'createdDate',
      header: 'accounts.transactions.fields.date',
      type: 'date',
      sortable: true,
      width: '150px',
    },
    {
      field: 'sourceType',
      header: 'accounts.transactions.fields.type',
      type: 'text',
      sortable: true,
      width: '120px',
    },
    {
      field: 'sourceKind',
      header: 'accounts.transactions.fields.kind',
      type: 'text',
      sortable: true,
      width: '120px',
    },
    {
      field: 'moneyIn',
      header: 'accounts.transactions.fields.moneyIn',
      type: 'currency',
      sortable: true,
      width: '120px',
    },
    {
      field: 'moneyOut',
      header: 'accounts.transactions.fields.moneyOut',
      type: 'currency',
      sortable: true,
      width: '120px',
    },
    {
      field: 'details',
      header: 'accounts.transactions.fields.details',
      type: 'template',
      templateRef: 'detailsTemplate',
      sortable: true,
      width: '200px',
    },
  ];

  constructor(
    private route: ActivatedRoute,
    private accountUserService: AccountUserResourceService,
    private translateService: TranslateService
  ) {
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

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadTransactions();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadTransactions();
  }

  getSourceTypeClass(type: string): string {
    switch (type) {
      case 'INVOICE':
        return 'bg-blue';
      case 'PAYMENT':
        return 'bg-green';
      case 'REFUND':
        return 'bg-orange';
      default:
        return 'bg-secondary';
    }
  }
  viewDetails(row: any): void {
    // Implement your view details logic here
    console.log('View details for:', row);
    // You can open a modal or navigate to a details page
  }
  getSourceKindClass(kind: string): string {
    switch (kind) {
      case 'CASH':
        return 'bg-success';
      case 'CREDIT':
        return 'bg-info';
      case 'BANK':
        return 'bg-primary';
      default:
        return 'bg-secondary';
    }
  }
}
