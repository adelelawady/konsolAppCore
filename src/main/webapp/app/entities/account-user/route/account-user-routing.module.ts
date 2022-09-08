import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountUserComponent } from '../list/account-user.component';
import { AccountUserDetailComponent } from '../detail/account-user-detail.component';
import { AccountUserUpdateComponent } from '../update/account-user-update.component';
import { AccountUserRoutingResolveService } from './account-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const accountUserRoute: Routes = [
  {
    path: '',
    component: AccountUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountUserDetailComponent,
    resolve: {
      accountUser: AccountUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountUserUpdateComponent,
    resolve: {
      accountUser: AccountUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountUserUpdateComponent,
    resolve: {
      accountUser: AccountUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountUserRoute)],
  exports: [RouterModule],
})
export class AccountUserRoutingModule {}
