import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { StoreFormService, StoreFormGroup } from './store-form.service';
import { IStore } from '../store.model';
import { StoreService } from '../service/store.service';
import { IStoreItem } from 'app/entities/store-item/store-item.model';
import { StoreItemService } from 'app/entities/store-item/service/store-item.service';

@Component({
  selector: 'jhi-store-update',
  templateUrl: './store-update.component.html',
})
export class StoreUpdateComponent implements OnInit {
  isSaving = false;
  store: IStore | null = null;

  storeItemsSharedCollection: IStoreItem[] = [];

  editForm: StoreFormGroup = this.storeFormService.createStoreFormGroup();

  constructor(
    protected storeService: StoreService,
    protected storeFormService: StoreFormService,
    protected storeItemService: StoreItemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareStoreItem = (o1: IStoreItem | null, o2: IStoreItem | null): boolean => this.storeItemService.compareStoreItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ store }) => {
      this.store = store;
      if (store) {
        this.updateForm(store);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const store = this.storeFormService.getStore(this.editForm);
    if (store.id !== null) {
      this.subscribeToSaveResponse(this.storeService.update(store));
    } else {
      this.subscribeToSaveResponse(this.storeService.create(store));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStore>>): void {
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

  protected updateForm(store: IStore): void {
    this.store = store;
    this.storeFormService.resetForm(this.editForm, store);

    this.storeItemsSharedCollection = this.storeItemService.addStoreItemToCollectionIfMissing<IStoreItem>(
      this.storeItemsSharedCollection,
      ...(store.items ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.storeItemService
      .query()
      .pipe(map((res: HttpResponse<IStoreItem[]>) => res.body ?? []))
      .pipe(
        map((storeItems: IStoreItem[]) =>
          this.storeItemService.addStoreItemToCollectionIfMissing<IStoreItem>(storeItems, ...(this.store?.items ?? []))
        )
      )
      .subscribe((storeItems: IStoreItem[]) => (this.storeItemsSharedCollection = storeItems));
  }
}
