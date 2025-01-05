import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountsEmployeeComponent } from './accounts.component';
import { AddEmployeeAccountComponent } from './add-account/add-account.component';
import { AccountEmployeeDetailsComponent } from './account-details/account-details.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrModule } from 'ngx-toastr';

@NgModule({
  declarations: [AccountsEmployeeComponent, AddEmployeeAccountComponent, AccountEmployeeDetailsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        component: AccountsEmployeeComponent,
        data: {
          pageTitle: 'accounts.title',
        },
      },
    ]),
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    ToastrModule,
  ],
})
export class AccountsEmployeeModule {}
