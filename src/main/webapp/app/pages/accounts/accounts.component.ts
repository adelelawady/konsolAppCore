import { Component, OnInit, ViewChild } from '@angular/core';
import { AccountUserResourceService } from '../../core/konsolApi/api/accountUserResource.service';
import { AccountUserDTO } from '../../core/konsolApi/model/accountUserDTO';
import { AccountUserContainer } from '../../core/konsolApi/model/accountUserContainer';
import { AccountUserSearchModel } from '../../core/konsolApi/model/accountUserSearchModel';
import { TableColumn } from '../../shared/components/data-table/table-column.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent } from '../../shared/components/data-table/data-table.component';

@Component({
  selector: 'jhi-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.scss'],
})
export class AccountsComponent implements OnInit {
  @ViewChild('dataTable') dataTable!: DataTableComponent;

  accounts: AccountUserDTO[] = [];
  loading = false;
  totalRecords = 0;
  currentPage = 0;
  pageSize = 10;
  searchText = '';
  selectedKind?: AccountUserSearchModel['kind'];

  accountKinds: AccountUserDTO['kind'][] = ['CUSTOMER', 'SUPPLIER', 'SALEMAN'];

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
    const searchModel: AccountUserSearchModel = {
      page: event?.page ?? this.currentPage,
      size: event?.size ?? this.pageSize,
      ...(this.searchText &&
        this.searchText !== '' && {
          name: this.searchText,
          phone: this.searchText,
          address: this.searchText,
          address2: this.searchText,
        }),
      kind: this.selectedKind,
    };
    console.log(searchModel);

    this.accountUserService.searchAccountUsers(searchModel).subscribe({
      next: (response: AccountUserContainer) => {
        console.log(response);
        if (response && response.result) {
          this.accounts = [...response.result];
          this.totalRecords = response.total ?? 0;
          if (this.dataTable) {
            // this.dataTable.refresh();
          }
        } else {
          this.accounts = [];
          this.dataTable.refresh();
        }
        this.loading = false;
      },
      error: () => {
        this.showError('loadError');
        this.loading = false;
      },
    });
  }

  onSearch(event?: any): void {
    if (event && event !== '') {
      this.searchText = event;
    } else {
      this.searchText = '';
    }
    this.currentPage = 0;
    this.loadAccounts();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadAccounts({ page });
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadAccounts({ size });
  }

  onSort(event: any): void {
    this.loadAccounts({ sort: event });
  }

  onEdit(account: AccountUserDTO): void {
    // TODO: Implement edit logic
    console.log('Edit account:', account);
  }

  onDelete(account: AccountUserDTO): void {
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

  onKindChange(kind: string | undefined) {
    this.currentPage = 0; // Reset to first page
    this.loadAccounts();
  }
}
