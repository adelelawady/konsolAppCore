import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICafeTable } from '../cafe-table.model';
import { CafeTableService } from '../service/cafe-table.service';

@Component({
  standalone: true,
  templateUrl: './cafe-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CafeTableDeleteDialogComponent {
  cafeTable?: ICafeTable;

  protected cafeTableService = inject(CafeTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.cafeTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
