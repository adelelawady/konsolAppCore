import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICafeTable } from '../cafe-table.model';

@Component({
  standalone: true,
  selector: 'jhi-cafe-table-detail',
  templateUrl: './cafe-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CafeTableDetailComponent {
  cafeTable = input<ICafeTable | null>(null);

  previousState(): void {
    window.history.back();
  }
}
