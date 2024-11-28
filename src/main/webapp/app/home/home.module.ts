import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { PagesModule } from '../pages/pages.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]), PagesModule],
  declarations: [HomeComponent],
})
export class HomeModule {}
