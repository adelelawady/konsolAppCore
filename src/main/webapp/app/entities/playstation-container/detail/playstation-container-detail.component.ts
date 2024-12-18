import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPlaystationContainer } from '../playstation-container.model';

@Component({
  standalone: true,
  selector: 'jhi-playstation-container-detail',
  templateUrl: './playstation-container-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlaystationContainerDetailComponent {
  playstationContainer = input<IPlaystationContainer | null>(null);

  previousState(): void {
    window.history.back();
  }
}
