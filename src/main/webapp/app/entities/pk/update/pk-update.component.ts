import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PkFormService, PkFormGroup } from './pk-form.service';
import { IPk } from '../pk.model';
import { PkService } from '../service/pk.service';
import { PkKind } from 'app/entities/enumerations/pk-kind.model';

@Component({
  selector: 'jhi-pk-update',
  templateUrl: './pk-update.component.html',
})
export class PkUpdateComponent implements OnInit {
  isSaving = false;
  pk: IPk | null = null;
  pkKindValues = Object.keys(PkKind);

  editForm: PkFormGroup = this.pkFormService.createPkFormGroup();

  constructor(protected pkService: PkService, protected pkFormService: PkFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pk }) => {
      this.pk = pk;
      if (pk) {
        this.updateForm(pk);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pk = this.pkFormService.getPk(this.editForm);
    if (pk.id !== null) {
      this.subscribeToSaveResponse(this.pkService.update(pk));
    } else {
      this.subscribeToSaveResponse(this.pkService.create(pk));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPk>>): void {
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

  protected updateForm(pk: IPk): void {
    this.pk = pk;
    this.pkFormService.resetForm(this.editForm, pk);
  }
}
