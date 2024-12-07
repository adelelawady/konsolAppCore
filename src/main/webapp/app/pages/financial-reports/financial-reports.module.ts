import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { FinancialReportsComponent } from './financial-reports.component';

@NgModule({
  declarations: [FinancialReportsComponent],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    RouterModule.forChild([
      {
        path: '',
        component: FinancialReportsComponent,
        data: {
          pageTitle: 'global.navigation.pages.financial-reports.title',
        },
      },
    ]),
  ],
})
export class FinancialReportsModule {}
