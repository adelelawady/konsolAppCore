import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlaystationContainer } from '../playstation-container.model';
import { PlaystationContainerService } from '../service/playstation-container.service';

@Component({
  standalone: true,
  templateUrl: './playstation-container-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlaystationContainerDeleteDialogComponent {
  playstationContainer?: IPlaystationContainer;

  protected playstationContainerService = inject(PlaystationContainerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.playstationContainerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
