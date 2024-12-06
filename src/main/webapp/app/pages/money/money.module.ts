import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MoneyComponent } from './money.component';
import { CreateMoneyModalComponent } from './create-money-modal/create-money-modal.component';
import { ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [MoneyComponent, CreateMoneyModalComponent],
  imports: [CommonModule, ReactiveFormsModule, SharedModule, NgbModule, TranslateModule],
})
export class MoneyModule {}
