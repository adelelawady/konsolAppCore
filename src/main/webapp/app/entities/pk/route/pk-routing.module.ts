import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PkComponent } from '../list/pk.component';
import { PkDetailComponent } from '../detail/pk-detail.component';
import { PkUpdateComponent } from '../update/pk-update.component';
import { PkRoutingResolveService } from './pk-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pkRoute: Routes = [
  {
    path: '',
    component: PkComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PkDetailComponent,
    resolve: {
      pk: PkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PkUpdateComponent,
    resolve: {
      pk: PkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PkUpdateComponent,
    resolve: {
      pk: PkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pkRoute)],
  exports: [RouterModule],
})
export class PkRoutingModule {}
