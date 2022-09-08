import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemUnitComponent } from './list/item-unit.component';
import { ItemUnitDetailComponent } from './detail/item-unit-detail.component';
import { ItemUnitUpdateComponent } from './update/item-unit-update.component';
import { ItemUnitDeleteDialogComponent } from './delete/item-unit-delete-dialog.component';
import { ItemUnitRoutingModule } from './route/item-unit-routing.module';

@NgModule({
  imports: [SharedModule, ItemUnitRoutingModule],
  declarations: [ItemUnitComponent, ItemUnitDetailComponent, ItemUnitUpdateComponent, ItemUnitDeleteDialogComponent],
})
export class ItemUnitModule {}
