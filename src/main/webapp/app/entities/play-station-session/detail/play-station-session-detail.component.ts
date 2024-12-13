import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPlayStationSession } from '../play-station-session.model';

@Component({
  standalone: true,
  selector: 'jhi-play-station-session-detail',
  templateUrl: './play-station-session-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlayStationSessionDetailComponent {
  playStationSession = input<IPlayStationSession | null>(null);

  previousState(): void {
    window.history.back();
  }
}
