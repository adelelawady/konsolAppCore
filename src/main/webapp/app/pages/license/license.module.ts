import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { LicenseComponent } from './license.component';

@NgModule({
  declarations: [LicenseComponent],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    RouterModule.forChild([
      {
        path: '',
        component: LicenseComponent,
        data: {
          pageTitle: 'license.title'
        }
      }
    ])
  ]
})
export class LicenseModule {} 