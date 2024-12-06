import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MoneyResourceService } from '../../core/konsolApi/api/moneyResource.service';
import { MoneyDTO } from '../../core/konsolApi/model/moneyDTO';
import { MoniesSearchModel } from '../../core/konsolApi/model/moniesSearchModel';
import { CreateMoneyModalComponent } from './create-money-modal/create-money-modal.component';
import { TranslateService } from '@ngx-translate/core';
import { TableColumn } from '../../shared/components/data-table/table-column.model';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-money',
  templateUrl: './money.component.html',
})
export class MoneyComponent implements OnInit {
  monies: MoneyDTO[] = [];
  loading = false;
  totalItems = 0;
  page = 0;
  pageSize = 10;
  searchModel: MoniesSearchModel = {};
  columns: TableColumn[] = [];
  selectedKind: string | null = null;
  dateFrom: string | null = null;
  dateTo: string | null = null;
  selectedAccount: any | null = null;

  constructor(private moneyService: MoneyResourceService, private modalService: NgbModal, private translateService: TranslateService) {}

  ngOnInit(): void {
    this.initializeColumns();
    this.loadMonies();
  }

  private initializeColumns(): void {
    this.columns = [
      { field: 'pk', header: 'konsolCoreApp.money.fields.id', sortable: true },
      { field: 'kind', header: 'konsolCoreApp.money.fields.type', sortable: true },
      { field: 'account', header: 'konsolCoreApp.money.fields.account', format: (acc: any) => acc.name, sortable: true },
      { field: 'moneyIn', header: 'konsolCoreApp.money.fields.moneyIn', sortable: true },
      { field: 'moneyOut', header: 'konsolCoreApp.money.fields.moneyOut', sortable: true },

      { field: 'details', header: 'konsolCoreApp.money.fields.details', sortable: true },

      {
        field: 'created_date',
        header: 'konsolCoreApp.money.fields.date',
        format: (value: string) => this.formatDate(value),
        sortable: true,
      },
      { field: 'actions', header: 'konsolCoreApp.money.common.actions', type: 'actions', sortable: false },
    ];
  }

  loadMonies(): void {
    this.loading = true;
    const searchParams: any = {
      ...this.searchModel,
      page: this.page,
      size: this.pageSize,
    };

    if (this.selectedKind) {
      searchParams.kind = this.selectedKind;
    }

    if (this.dateFrom && this.dateFrom != '') {
      searchParams.dateFrom = this.formatDateForApi(this.dateFrom);
    }

    if (this.dateTo && this.dateTo != '') {
      searchParams.dateTo = this.formatDateForApi(this.dateTo);
    }

    if (this.selectedAccount?.id) {
      searchParams.accountId = this.selectedAccount.id;
    }

    this.moneyService.moniesViewSearchPaginate(searchParams).subscribe(
      response => {
        this.monies = response.result || [];
        this.totalItems = response.total || 0;
        this.loading = false;
      },
      error => {
        console.error('Error loading monies:', error);
        this.loading = false;
      }
    );
  }

  openCreateModal(money?: MoneyDTO): void {
    const modalRef = this.modalService.open(CreateMoneyModalComponent, { size: 'lg' });
    if (money) {
      modalRef.componentInstance.money = money;
    }
    modalRef.result.then(
      result => {
        this.loadMonies();
      },
      () => {}
    );
  }

  onSearch(event: any): void {
    this.searchModel = {
      ...this.searchModel,
      details: event.value,
    };
    this.page = 0;
    this.loadMonies();
  }

  onPageChange(page: number): void {
    this.page = page;
    this.loadMonies();
  }

  onPageSizeChange(pageSize: number): void {
    this.pageSize = pageSize;
    this.page = 0;
    this.loadMonies();
  }

  onSort(event: { field: string; direction: string }): void {
    this.searchModel = {
      ...this.searchModel,
      sortField: event.field,
      sortOrder: event.direction === 'asc' ? 'ASC' : 'DESC',
    };
    this.loadMonies();
  }

  onEdit(money: MoneyDTO): void {
    this.openCreateModal(money);
  }

  onDelete(money: MoneyDTO): void {
    if (confirm(this.translateService.instant('money.delete.confirm'))) {
      this.moneyService.deleteMoney(money.id!).subscribe(
        () => {
          this.loadMonies();
        },
        error => {
          console.error('Error deleting money:', error);
        }
      );
    }
  }

  onKindChange(kind: any): void {
    this.selectedKind = kind.target?.value;
    this.page = 0;
    this.loadMonies();
  }

  onDateFromChange(event: any): void {
    const date = event.target?.value;
    this.dateFrom = date;
    this.searchModel.dateFrom = date;
    this.page = 0;
    this.loadMonies();
  }

  onDateToChange(event: any): void {
    const date = event.target?.value;
    this.dateTo = date;
    this.searchModel.dateTo = date;
    this.page = 0;
    this.loadMonies();
  }

  onAccountSelect(account: any): void {
    this.selectedAccount = account;
    this.page = 0;
    this.loadMonies();
  }

  private formatDate(date: string): string {
    if (!date) return '';
    try {
      return formatDate(date, 'yyyy-MM-dd', 'en');
    } catch (error) {
      console.error('Error formatting date:', error);
      return date;
    }
  }

  private formatDateForApi(date: string): string {
    if (!date) return '';
    try {
      // Convert the date to ISO format with time
      const d = new Date(date);
      // Set to start of day for dateFrom
      if (this.searchModel.dateFrom === date) {
        d.setHours(0, 0, 0, 0);
      }
      // Set to end of day for dateTo
      if (this.searchModel.dateTo === date) {
        d.setHours(23, 59, 59, 999);
      }
      return d.toISOString();
    } catch (error) {
      console.error('Error formatting date for API:', error);
      return date;
    }
  }
}
