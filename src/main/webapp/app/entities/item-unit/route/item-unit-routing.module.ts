import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemUnitComponent } from '../list/item-unit.component';
import { ItemUnitDetailComponent } from '../detail/item-unit-detail.component';
import { ItemUnitUpdateComponent } from '../update/item-unit-update.component';
import { ItemUnitRoutingResolveService } from './item-unit-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const itemUnitRoute: Routes = [
  {
    path: '',
    component: ItemUnitComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemUnitDetailComponent,
    resolve: {
      itemUnit: ItemUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemUnitUpdateComponent,
    resolve: {
      itemUnit: ItemUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemUnitUpdateComponent,
    resolve: {
      itemUnit: ItemUnitRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemUnitRoute)],
  exports: [RouterModule],
})
export class ItemUnitRoutingModule {}
