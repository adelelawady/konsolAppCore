import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISheft } from '../sheft.model';
import { SheftService } from '../service/sheft.service';
import { SheftFormGroup, SheftFormService } from './sheft-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sheft-update',
  templateUrl: './sheft-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SheftUpdateComponent implements OnInit {
  isSaving = false;
  sheft: ISheft | null = null;

  protected sheftService = inject(SheftService);
  protected sheftFormService = inject(SheftFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SheftFormGroup = this.sheftFormService.createSheftFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sheft }) => {
      this.sheft = sheft;
      if (sheft) {
        this.updateForm(sheft);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sheft = this.sheftFormService.getSheft(this.editForm);
    if (sheft.id !== null) {
      this.subscribeToSaveResponse(this.sheftService.update(sheft));
    } else {
      this.subscribeToSaveResponse(this.sheftService.create(sheft));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISheft>>): void {
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

  protected updateForm(sheft: ISheft): void {
    this.sheft = sheft;
    this.sheftFormService.resetForm(this.editForm, sheft);
  }
}
