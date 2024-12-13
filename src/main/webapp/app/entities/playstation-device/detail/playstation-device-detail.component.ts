import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPlaystationDevice } from '../playstation-device.model';

@Component({
  standalone: true,
  selector: 'jhi-playstation-device-detail',
  templateUrl: './playstation-device-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlaystationDeviceDetailComponent {
  playstationDevice = input<IPlaystationDevice | null>(null);

  previousState(): void {
    window.history.back();
  }
}
