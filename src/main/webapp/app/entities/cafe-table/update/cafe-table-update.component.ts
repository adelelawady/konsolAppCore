import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICafeTable } from '../cafe-table.model';
import { CafeTableService } from '../service/cafe-table.service';
import { CafeTableFormGroup, CafeTableFormService } from './cafe-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-cafe-table-update',
  templateUrl: './cafe-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CafeTableUpdateComponent implements OnInit {
  isSaving = false;
  cafeTable: ICafeTable | null = null;

  protected cafeTableService = inject(CafeTableService);
  protected cafeTableFormService = inject(CafeTableFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CafeTableFormGroup = this.cafeTableFormService.createCafeTableFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cafeTable }) => {
      this.cafeTable = cafeTable;
      if (cafeTable) {
        this.updateForm(cafeTable);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cafeTable = this.cafeTableFormService.getCafeTable(this.editForm);
    if (cafeTable.id !== null) {
      this.subscribeToSaveResponse(this.cafeTableService.update(cafeTable));
    } else {
      this.subscribeToSaveResponse(this.cafeTableService.create(cafeTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICafeTable>>): void {
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

  protected updateForm(cafeTable: ICafeTable): void {
    this.cafeTable = cafeTable;
    this.cafeTableFormService.resetForm(this.editForm, cafeTable);
  }
}
