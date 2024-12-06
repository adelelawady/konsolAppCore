import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { BanksComponent } from './list/banks.component';
import { BankCreateModalComponent } from './create/bank-create-modal.component';
import { banksRoute } from './banks.route';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BankFormComponent } from './list/bank-form.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(banksRoute), ReactiveFormsModule, NgbModule],
  declarations: [BanksComponent, BankCreateModalComponent, BankFormComponent],
})
export class BanksModule {}
