import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPriceOption } from '../price-option.model';

@Component({
  standalone: true,
  selector: 'jhi-price-option-detail',
  templateUrl: './price-option-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PriceOptionDetailComponent {
  priceOption = input<IPriceOption | null>(null);

  previousState(): void {
    window.history.back();
  }
}
