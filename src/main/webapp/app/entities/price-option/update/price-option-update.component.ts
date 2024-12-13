import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPriceOption } from '../price-option.model';
import { PriceOptionService } from '../service/price-option.service';
import { PriceOptionFormGroup, PriceOptionFormService } from './price-option-form.service';

@Component({
  standalone: true,
  selector: 'jhi-price-option-update',
  templateUrl: './price-option-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PriceOptionUpdateComponent implements OnInit {
  isSaving = false;
  priceOption: IPriceOption | null = null;

  protected priceOptionService = inject(PriceOptionService);
  protected priceOptionFormService = inject(PriceOptionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PriceOptionFormGroup = this.priceOptionFormService.createPriceOptionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceOption }) => {
      this.priceOption = priceOption;
      if (priceOption) {
        this.updateForm(priceOption);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priceOption = this.priceOptionFormService.getPriceOption(this.editForm);
    if (priceOption.id !== null) {
      this.subscribeToSaveResponse(this.priceOptionService.update(priceOption));
    } else {
      this.subscribeToSaveResponse(this.priceOptionService.create(priceOption));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriceOption>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(priceOption: IPriceOption): void {
    this.priceOption = priceOption;
    this.priceOptionFormService.resetForm(this.editForm, priceOption);
  }
}
