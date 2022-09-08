import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StoreItemComponent } from '../list/store-item.component';
import { StoreItemDetailComponent } from '../detail/store-item-detail.component';
import { StoreItemUpdateComponent } from '../update/store-item-update.component';
import { StoreItemRoutingResolveService } from './store-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const storeItemRoute: Routes = [
  {
    path: '',
    component: StoreItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StoreItemDetailComponent,
    resolve: {
      storeItem: StoreItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StoreItemUpdateComponent,
    resolve: {
      storeItem: StoreItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StoreItemUpdateComponent,
    resolve: {
      storeItem: StoreItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(storeItemRoute)],
  exports: [RouterModule],
})
export class StoreItemRoutingModule {}
