import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PkComponent } from './list/pk.component';
import { PkDetailComponent } from './detail/pk-detail.component';
import { PkUpdateComponent } from './update/pk-update.component';
import { PkDeleteDialogComponent } from './delete/pk-delete-dialog.component';
import { PkRoutingModule } from './route/pk-routing.module';

@NgModule({
  imports: [SharedModule, PkRoutingModule],
  declarations: [PkComponent, PkDetailComponent, PkUpdateComponent, PkDeleteDialogComponent],
})
export class PkModule {}
