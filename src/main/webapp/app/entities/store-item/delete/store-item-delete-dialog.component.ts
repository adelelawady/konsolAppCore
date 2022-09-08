import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStoreItem } from '../store-item.model';
import { StoreItemService } from '../service/store-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './store-item-delete-dialog.component.html',
})
export class StoreItemDeleteDialogComponent {
  storeItem?: IStoreItem;

  constructor(protected storeItemService: StoreItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.storeItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
