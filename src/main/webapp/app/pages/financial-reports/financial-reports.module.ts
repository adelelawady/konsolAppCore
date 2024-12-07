import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { FinancialReportsComponent } from './financial-reports.component';

import { NgxEchartsModule, NGX_ECHARTS_CONFIG } from 'ngx-echarts';

import * as echarts from 'echarts/core';
import { BarChart } from 'echarts/charts';
import { GridComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
echarts.use([BarChart, GridComponent, CanvasRenderer]);

@NgModule({
  declarations: [FinancialReportsComponent],
  providers: [
    {
      provide: NGX_ECHARTS_CONFIG,
      useFactory: () => ({ echarts: () => import('echarts') }),
    },
  ],
  imports: [
    CommonModule,
    FormsModule,
    NgxEchartsModule.forChild(),

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
