import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ItemUnitFormService, ItemUnitFormGroup } from './item-unit-form.service';
import { IItemUnit } from '../item-unit.model';
import { ItemUnitService } from '../service/item-unit.service';

@Component({
  selector: 'jhi-item-unit-update',
  templateUrl: './item-unit-update.component.html',
})
export class ItemUnitUpdateComponent implements OnInit {
  isSaving = false;
  itemUnit: IItemUnit | null = null;

  editForm: ItemUnitFormGroup = this.itemUnitFormService.createItemUnitFormGroup();

  constructor(
    protected itemUnitService: ItemUnitService,
    protected itemUnitFormService: ItemUnitFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemUnit }) => {
      this.itemUnit = itemUnit;
      if (itemUnit) {
        this.updateForm(itemUnit);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemUnit = this.itemUnitFormService.getItemUnit(this.editForm);
    if (itemUnit.id !== null) {
      this.subscribeToSaveResponse(this.itemUnitService.update(itemUnit));
    } else {
      this.subscribeToSaveResponse(this.itemUnitService.create(itemUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemUnit>>): void {
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

  protected updateForm(itemUnit: IItemUnit): void {
    this.itemUnit = itemUnit;
    this.itemUnitFormService.resetForm(this.editForm, itemUnit);
  }
}
