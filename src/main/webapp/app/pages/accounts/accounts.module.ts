import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountsComponent } from './accounts.component';
import { ToastrModule } from 'ngx-toastr';

@NgModule({
  declarations: [AccountsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([
      {
        path: '',
        component: AccountsComponent,
      },
    ]),
    TranslateModule,
    SharedModule,
    ToastrModule,
  ],
})
export class AccountsModule {}
