import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountsComponent } from './accounts.component';
import { AddAccountComponent } from './add-account/add-account.component';
import { AccountDetailsComponent } from './account-details/account-details.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { ToastrModule } from 'ngx-toastr';

@NgModule({
  declarations: [AccountsComponent, AddAccountComponent, AccountDetailsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        component: AccountsComponent,
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
export class AccountsModule {}
