import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { FinancialReportsComponent } from './financial-reports.component';

@NgModule({
  imports: [
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
  declarations: [FinancialReportsComponent],
})
export class FinancialReportsModule {}
