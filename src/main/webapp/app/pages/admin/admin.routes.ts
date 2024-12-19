import { Routes } from '@angular/router';
import { AdminPlaystationContainerComponent } from './playstation-container/playstation-container.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

export const adminRoutes: Routes = [
  {
    path: 'playstation-container',
    component: AdminPlaystationContainerComponent,
    canActivate: [UserRouteAccessService],
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'PlayStation Containers'
    }
  }
]; 