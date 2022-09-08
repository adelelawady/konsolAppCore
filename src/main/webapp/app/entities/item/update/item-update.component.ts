import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ItemFormService, ItemFormGroup } from './item-form.service';
import { IItem } from '../item.model';
import { ItemService } from '../service/item.service';
import { IItemUnit } from 'app/entities/item-unit/item-unit.model';
import { ItemUnitService } from 'app/entities/item-unit/service/item-unit.service';

@Component({
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  item: IItem | null = null;

  itemUnitsSharedCollection: IItemUnit[] = [];

  editForm: ItemFormGroup = this.itemFormService.createItemFormGroup();

  constructor(
    protected itemService: ItemService,
    protected itemFormService: ItemFormService,
    protected itemUnitService: ItemUnitService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareItemUnit = (o1: IItemUnit | null, o2: IItemUnit | null): boolean => this.itemUnitService.compareItemUnit(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
      if (item) {
        this.updateForm(item);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.itemFormService.getItem(this.editForm);
    if (item.id !== null) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
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

  protected updateForm(item: IItem): void {
    this.item = item;
    this.itemFormService.resetForm(this.editForm, item);

    this.itemUnitsSharedCollection = this.itemUnitService.addItemUnitToCollectionIfMissing<IItemUnit>(
      this.itemUnitsSharedCollection,
      ...(item.itemUnits ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemUnitService
      .query()
      .pipe(map((res: HttpResponse<IItemUnit[]>) => res.body ?? []))
      .pipe(
        map((itemUnits: IItemUnit[]) =>
          this.itemUnitService.addItemUnitToCollectionIfMissing<IItemUnit>(itemUnits, ...(this.item?.itemUnits ?? []))
        )
      )
      .subscribe((itemUnits: IItemUnit[]) => (this.itemUnitsSharedCollection = itemUnits));
  }
}
