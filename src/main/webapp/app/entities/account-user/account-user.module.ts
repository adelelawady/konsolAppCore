import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountUserComponent } from './list/account-user.component';
import { AccountUserDetailComponent } from './detail/account-user-detail.component';
import { AccountUserUpdateComponent } from './update/account-user-update.component';
import { AccountUserDeleteDialogComponent } from './delete/account-user-delete-dialog.component';
import { AccountUserRoutingModule } from './route/account-user-routing.module';

@NgModule({
  imports: [SharedModule, AccountUserRoutingModule],
  declarations: [AccountUserComponent, AccountUserDetailComponent, AccountUserUpdateComponent, AccountUserDeleteDialogComponent],
})
export class AccountUserModule {}
