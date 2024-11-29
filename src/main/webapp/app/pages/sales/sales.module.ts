import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { SalesComponent } from './sales.component';
import { RouterModule } from '@angular/router';
import { AppCurrencyPipe } from 'app/shared/pipes/app-currency.pipe';

@NgModule({
  declarations: [SalesComponent],
  imports: [CommonModule, FormsModule, SharedModule, RouterModule],
  exports: [SalesComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SalesModule {}
