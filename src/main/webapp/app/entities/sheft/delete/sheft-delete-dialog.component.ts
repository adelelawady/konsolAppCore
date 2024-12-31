import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISheft } from '../sheft.model';
import { SheftService } from '../service/sheft.service';

@Component({
  standalone: true,
  templateUrl: './sheft-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SheftDeleteDialogComponent {
  sheft?: ISheft;

  protected sheftService = inject(SheftService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.sheftService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
