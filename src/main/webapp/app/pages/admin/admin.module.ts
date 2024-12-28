import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { AdminPlaystationContainerComponent } from './playstation-container/playstation-container.component';
import { adminRoutes } from './admin.routes';
import { NgSelectModule } from '@ng-select/ng-select';
import { PlayStationFinancialReportsModule } from './financial-reports/financial-reports.module';

@NgModule({
  declarations: [AdminPlaystationContainerComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(adminRoutes),
    ReactiveFormsModule,
    FormsModule,
    SharedModule,
    NgSelectModule,
    PlayStationFinancialReportsModule,
  ],
})
export class AdminModule {}
