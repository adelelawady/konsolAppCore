import { Routes } from '@angular/router';
import { AdminPlaystationContainerComponent } from './playstation-container/playstation-container.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SheftComponentComponent } from './sheft-component/sheft-component.component';
import { ViewSheftComponent } from './sheft-component/view-sheft/view-sheft.component';
import { SheftManagementComponent } from './sheft-management/sheft-management.component';

export const adminRoutes: Routes = [
  {
    path: 'playstation-container',
    component: AdminPlaystationContainerComponent,
    canActivate: [UserRouteAccessService],
    data: {
      hasPlaystationLayout: true,
      authorities: ['ROLE_ADMIN','ROLE_MANAGE_CONTAINERS'],
      pageTitle: 'konsolCoreApp.playstationContainer.home.title',
    },
  },
  {
    path: 'playstation-financial-reports',
    loadChildren: () => import('./financial-reports/financial-reports.module').then(m => m.PlayStationFinancialReportsModule),
    canActivate: [UserRouteAccessService],
    data: {
      hasPlaystationLayout: true,
      authorities: ['ROLE_ADMIN','ROLE_MANAGE_FINANCE'],
      pageTitle: 'financialReports.playstation.title',
    },
  },
  {
    path: 'sheft',
    component: SheftComponentComponent,
    canActivate: [UserRouteAccessService],
    data: {
      hasPlaystationLayout: true,
      authorities: [ 'ROLE_ADMIN','ROLE_MANAGE_SHEFTS'],
      pageTitle: 'konsolCoreApp.sheft.list.title',
    },
  },
  {
    path: 'shefts/:id',
    component: ViewSheftComponent,
    title: 'konsolCoreApp.sheft.view.title',
    data: {
      hasPlaystationLayout: true,
      authorities: ['ROLE_ADMIN','ROLE_MANAGE_SHEFTS'],
    },
  },
  {
    path: 'sheft-management',
    component: SheftManagementComponent,
    canActivate: [UserRouteAccessService],
    data: {
      hasPlaystationLayout: true,
      authorities: [ 'ROLE_ADMIN','ROLE_VIEW_ACTIVE_SHEFT'],
      pageTitle: 'konsolCoreApp.sheft.management.title',
    },
  },
];
