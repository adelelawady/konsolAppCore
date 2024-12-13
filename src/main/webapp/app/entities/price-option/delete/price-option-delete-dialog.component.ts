import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPriceOption } from '../price-option.model';
import { PriceOptionService } from '../service/price-option.service';

@Component({
  standalone: true,
  templateUrl: './price-option-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PriceOptionDeleteDialogComponent {
  priceOption?: IPriceOption;

  protected priceOptionService = inject(PriceOptionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.priceOptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
