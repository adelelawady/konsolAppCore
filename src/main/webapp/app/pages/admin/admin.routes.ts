import { Routes } from '@angular/router';
import { AdminPlaystationContainerComponent } from './playstation-container/playstation-container.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SheftComponentComponent } from './sheft-component/sheft-component.component';
import { ViewSheftComponent } from './sheft-component/view-sheft/view-sheft.component';

export const adminRoutes: Routes = [
  {
    path: 'playstation-container',
    component: AdminPlaystationContainerComponent,
    canActivate: [UserRouteAccessService],
    data: {
      hasPlaystationLayout: true,
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'PlayStation Containers',
    },
  },
  {
    path: 'playstation-financial-reports',
    loadChildren: () => import('./financial-reports/financial-reports.module').then(m => m.PlayStationFinancialReportsModule),
    canActivate: [UserRouteAccessService],
    data: {
      hasPlaystationLayout: true,
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'PlayStation Containers',
    },
  },
  {
    path: 'sheft',
    component: SheftComponentComponent,
    canActivate: [UserRouteAccessService],
    data: {
      hasPlaystationLayout: true,
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'Shefts',
    },
  },
  {
    path: 'shefts/:id',
    component: ViewSheftComponent,
    title: 'View Sheft',
    data: {
      hasPlaystationLayout: true,
    },
  },
];
