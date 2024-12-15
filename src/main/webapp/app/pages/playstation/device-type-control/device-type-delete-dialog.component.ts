import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './device-type-delete-dialog.component.html',
})
export class DeviceTypeDeleteDialogComponent {
  deviceType?: PsDeviceType;

  constructor(
    protected playstationResourceService: PlaystationResourceService,
    protected activeModal: NgbActiveModal
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string | undefined): void {
    if (id) {
      this.playstationResourceService.deleteDeviceType(id).subscribe(() => {
        this.activeModal.close(ITEM_DELETED_EVENT);
      });
    }
  }
} 