import { Component, OnInit } from '@angular/core';
import { AccountUserResourceService } from 'app/core/konsolApi/api/accountUserResource.service';
import { AccountUserDTO } from 'app/core/konsolApi/model/accountUserDTO';
import { AccountUserContainer } from 'app/core/konsolApi/model/accountUserContainer';
import { TableColumn } from 'app/shared/components/data-table/table-column.model';
import { ToastrService } from 'ngx-toastr';
import { finalize } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss'],
})
export class AccountsComponent implements OnInit {
  accounts: AccountUserDTO[] = [];
  loading = false;
  totalRecords = 0;
  currentPage = 0;
  pageSize = 10;
  searchText = '';

  columns: TableColumn[] = [
    { field: 'name', header: 'accounts.fields.name', sortable: true },
    {
      field: 'kind',
      header: 'accounts.fields.kind',
      sortable: true,
      format: (value: AccountUserDTO['kind']) => this.translate.instant(`accounts.kind.${value}`),
    },
    { field: 'phone', header: 'accounts.fields.phone', sortable: true },
    { field: 'address', header: 'accounts.fields.address', sortable: true },
    { field: 'balanceIn', header: 'accounts.fields.balanceIn', type: 'currency', sortable: true },
    { field: 'balanceOut', header: 'accounts.fields.balanceOut', type: 'currency', sortable: true },
    { field: 'actions', header: '', type: 'actions', width: '120px' },
  ];

  constructor(private accountUserService: AccountUserResourceService, private toastr: ToastrService, private translate: TranslateService) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(event?: any): void {
    this.loading = true;
    const searchModel = {
      page: event?.page ?? this.currentPage,
      size: event?.size ?? this.pageSize,
      sort: event?.sort ? [event.sort.field + ',' + event.sort.direction] : [],
      query: this.searchText,
    };

    this.accountUserService
      .searchAccountUsers(searchModel)
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (response: AccountUserContainer) => {
          if (response && response.result) {
            this.accounts = response.result;
            this.totalRecords = response.total ?? 0;
          }
        },
        error: () => {
          this.showError('loadError');
        },
      });
  }

  onSearch(term: any): void {
    this.searchText = term;
    this.currentPage = 0;
    this.loadAccounts();
  }

  onPageChange(page: any): void {
    this.currentPage = page;
    this.loadAccounts({ page });
  }

  onPageSizeChange(size: any): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadAccounts({ size });
  }

  onSort(event: any): void {
    this.loadAccounts({ sort: event });
  }

  onEdit(account: any): void {
    // TODO: Implement edit logic
    console.log('Edit account:', account);
  }

  onDelete(account: any): void {
    if (confirm(this.translate.instant('accounts.messages.deleteConfirm'))) {
      // TODO: Implement delete logic
      console.log('Delete account:', account);
    }
  }

  onAdd(): void {
    // TODO: Implement add logic
    console.log('Add new account');
  }

  private showError(key: string): void {
    this.toastr.error(this.translate.instant(`accounts.messages.${key}`));
  }

  private showSuccess(key: string): void {
    this.toastr.success(this.translate.instant(`accounts.messages.${key}`));
  }
}
