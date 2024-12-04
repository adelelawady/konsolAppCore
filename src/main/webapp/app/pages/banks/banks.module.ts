import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { BanksComponent } from './list/banks.component';
import { BankCreateModalComponent } from './create/bank-create-modal.component';
import { banksRoute } from './banks.route';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(banksRoute)],
  declarations: [BanksComponent, BankCreateModalComponent],
})
export class BanksModule {}
