import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { StoreItemFormService, StoreItemFormGroup } from './store-item-form.service';
import { IStoreItem } from '../store-item.model';
import { StoreItemService } from '../service/store-item.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

@Component({
  selector: 'jhi-store-item-update',
  templateUrl: './store-item-update.component.html',
})
export class StoreItemUpdateComponent implements OnInit {
  isSaving = false;
  storeItem: IStoreItem | null = null;

  itemsSharedCollection: IItem[] = [];

  editForm: StoreItemFormGroup = this.storeItemFormService.createStoreItemFormGroup();

  constructor(
    protected storeItemService: StoreItemService,
    protected storeItemFormService: StoreItemFormService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storeItem }) => {
      this.storeItem = storeItem;
      if (storeItem) {
        this.updateForm(storeItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const storeItem = this.storeItemFormService.getStoreItem(this.editForm);
    if (storeItem.id !== null) {
      this.subscribeToSaveResponse(this.storeItemService.update(storeItem));
    } else {
      this.subscribeToSaveResponse(this.storeItemService.create(storeItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStoreItem>>): void {
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

  protected updateForm(storeItem: IStoreItem): void {
    this.storeItem = storeItem;
    this.storeItemFormService.resetForm(this.editForm, storeItem);

    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, storeItem.item);
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.storeItem?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));
  }
}
