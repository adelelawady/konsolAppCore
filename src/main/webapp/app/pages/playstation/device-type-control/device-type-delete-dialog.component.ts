import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IPlaystationDeviceType } from 'app/entities/playstation-device-type/playstation-device-type.model';
import { PlaystationDeviceTypeService } from 'app/entities/playstation-device-type/service/playstation-device-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './device-type-delete-dialog.component.html',
})
export class DeviceTypeDeleteDialogComponent {
  deviceType?: IPlaystationDeviceType;

  constructor(
    protected deviceTypeService: PlaystationDeviceTypeService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.deviceTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
} 