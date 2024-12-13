import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPlayStationSession } from '../play-station-session.model';
import { PlayStationSessionService } from '../service/play-station-session.service';

@Component({
  standalone: true,
  templateUrl: './play-station-session-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PlayStationSessionDeleteDialogComponent {
  playStationSession?: IPlayStationSession;

  protected playStationSessionService = inject(PlayStationSessionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playStationSessionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
