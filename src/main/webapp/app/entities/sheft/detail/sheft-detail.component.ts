import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISheft } from '../sheft.model';

@Component({
  standalone: true,
  selector: 'jhi-sheft-detail',
  templateUrl: './sheft-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SheftDetailComponent {
  sheft = input<ISheft | null>(null);

  previousState(): void {
    window.history.back();
  }
}
