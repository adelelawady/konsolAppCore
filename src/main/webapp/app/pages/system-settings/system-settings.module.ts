import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { SystemSettingsComponent } from './system-settings.component';

@NgModule({
  declarations: [SystemSettingsComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    RouterModule.forChild([
      {
        path: '',
        component: SystemSettingsComponent,
        data: { pageTitle: 'System Settings' },
      },
    ]),
  ],
})
export class SystemSettingsModule {}
