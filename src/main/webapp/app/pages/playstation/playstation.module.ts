import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { PlaystationRoutingModule } from './playstation-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PlaystationLayoutComponent } from '../../layouts/playstation/playstation-layout.component';
import { LayoutsModule } from '../../layouts/layouts.module';

@NgModule({
  declarations: [
    NavigationPageComponent,
    DashboardComponent,
    PlaystationLayoutComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    PlaystationRoutingModule,
    LayoutsModule,
  ],
})
export class PlaystationModule { }
