import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StoreItemComponent } from './list/store-item.component';
import { StoreItemDetailComponent } from './detail/store-item-detail.component';
import { StoreItemUpdateComponent } from './update/store-item-update.component';
import { StoreItemDeleteDialogComponent } from './delete/store-item-delete-dialog.component';
import { StoreItemRoutingModule } from './route/store-item-routing.module';

@NgModule({
  imports: [SharedModule, StoreItemRoutingModule],
  declarations: [StoreItemComponent, StoreItemDetailComponent, StoreItemUpdateComponent, StoreItemDeleteDialogComponent],
})
export class StoreItemModule {}
