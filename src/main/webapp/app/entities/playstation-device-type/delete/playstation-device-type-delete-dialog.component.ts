import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlaystationDeviceType } from '../playstation-device-type.model';
import { PlaystationDeviceTypeService } from '../service/playstation-device-type.service';

@Component({
  standalone: true,
  templateUrl: './playstation-device-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlaystationDeviceTypeDeleteDialogComponent {
  playstationDeviceType?: IPlaystationDeviceType;

  protected playstationDeviceTypeService = inject(PlaystationDeviceTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.playstationDeviceTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
