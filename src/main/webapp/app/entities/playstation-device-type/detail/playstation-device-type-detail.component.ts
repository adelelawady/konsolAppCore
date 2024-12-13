import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPlaystationDeviceType } from '../playstation-device-type.model';

@Component({
  standalone: true,
  selector: 'jhi-playstation-device-type-detail',
  templateUrl: './playstation-device-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PlaystationDeviceTypeDetailComponent {
  playstationDeviceType = input<IPlaystationDeviceType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
