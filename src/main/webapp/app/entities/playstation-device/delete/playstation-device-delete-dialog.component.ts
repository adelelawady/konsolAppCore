import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlaystationDevice } from '../playstation-device.model';
import { PlaystationDeviceService } from '../service/playstation-device.service';

@Component({
  standalone: true,
  templateUrl: './playstation-device-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlaystationDeviceDeleteDialogComponent {
  playstationDevice?: IPlaystationDevice;

  protected playstationDeviceService = inject(PlaystationDeviceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.playstationDeviceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
