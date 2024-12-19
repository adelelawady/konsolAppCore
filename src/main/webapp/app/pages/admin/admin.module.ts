import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { AdminPlaystationContainerComponent } from './playstation-container/playstation-container.component';
import { adminRoutes } from './admin.routes';

@NgModule({
  declarations: [
    AdminPlaystationContainerComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(adminRoutes),
    ReactiveFormsModule,
    SharedModule
  ]
})
export class AdminModule { } 