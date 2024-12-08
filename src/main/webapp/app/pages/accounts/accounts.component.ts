import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AccountUserDTO } from '../../core/konsolApi/model/accountUserDTO';
import { AccountUserContainer } from '../../core/konsolApi/model/accountUserContainer';
import { AccountUserSearchModel } from '../../core/konsolApi/model/accountUserSearchModel';
import { TableColumn } from '../../shared/components/data-table/table-column.model';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DataTableComponent } from '../../shared/components/data-table/data-table.component';
import { AccountUserResourceService } from '../../core/konsolApi/api/accountUserResource.service';

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
  selectedKind: any = 'ALL';

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

  showAddEditModal = false;
  selectedAccount?: AccountUserDTO;

  constructor(
    private accountUserService: AccountUserResourceService,
    private toastr: ToastrService,
    private translate: TranslateService,
    private router: Router
  ) {}

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
    };

    if (this.selectedKind && this.selectedKind.toString() !== '' && this.selectedKind.toString() !== 'ALL') {
      searchModel.kind = this.selectedKind;
    }
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

  onSearch(searchText: string): void {
    this.searchText = searchText;
    this.currentPage = 0; // Reset to first page when searching
    this.loadAccounts();
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadAccounts();
  }

  onRowClick(event: any): void {
    console.log(event.id);
    if (event) {
      this.router.navigate(['/accounts', event.id]);
    }
  }

  onEdit(account: AccountUserDTO): void {
    this.selectedAccount = { ...account }; // Create a copy to avoid direct reference
    this.showAddEditModal = true;
  }

  onDelete(account: AccountUserDTO): void {
    if (confirm(this.translate.instant('accounts.messages.deleteConfirm'))) {
      this.loading = true;
      if (!account.id) {
        return;
      }
      this.accountUserService.deleteAccountUser(account.id).subscribe({
        next: () => {
          this.toastr.success(this.translate.instant('accounts.messages.deleteSuccess'));
          this.loadAccounts();
        },
        error: error => {
          console.error('Error deleting account:', error);
          this.toastr.error(this.translate.instant('accounts.messages.deleteError'));
          this.loading = false;
        },
      });
    }
  }

  onAdd(): void {
    this.createAccount();
  }

  createAccount(): void {
    this.selectedAccount = undefined;
    this.showAddEditModal = true;
  }

  onModalClose(): void {
    this.showAddEditModal = false;
    this.selectedAccount = undefined;
  }

  onAccountSaved(account: any): void {
    this.toastr.success(
      this.translate.instant(this.selectedAccount ? 'accounts.messages.updateSuccess' : 'accounts.messages.createSuccess')
    );
    this.loadAccounts();
    this.showAddEditModal = false;
    this.selectedAccount = undefined;
  }
  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadAccounts({ size });
  }

  private showError(key: string): void {
    this.toastr.error(this.translate.instant(key));
  }

  private showSuccess(key: string): void {
    this.toastr.success(this.translate.instant(`accounts.messages.${key}`));
  }

  onKindChange(kind: string | undefined) {
    this.currentPage = 0; // Reset to first page
    this.loadAccounts();
  }
}
