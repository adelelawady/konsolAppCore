import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemUnit } from '../item-unit.model';
import { ItemUnitService } from '../service/item-unit.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './item-unit-delete-dialog.component.html',
})
export class ItemUnitDeleteDialogComponent {
  itemUnit?: IItemUnit;

  constructor(protected itemUnitService: ItemUnitService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.itemUnitService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
