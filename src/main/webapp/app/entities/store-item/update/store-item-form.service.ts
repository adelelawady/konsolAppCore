import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStoreItem, NewStoreItem } from '../store-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStoreItem for edit and NewStoreItemFormGroupInput for create.
 */
type StoreItemFormGroupInput = IStoreItem | PartialWithRequiredKeyOf<NewStoreItem>;

type StoreItemFormDefaults = Pick<NewStoreItem, 'id' | 'storeIds'>;

type StoreItemFormGroupContent = {
  id: FormControl<IStoreItem['id'] | NewStoreItem['id']>;
  qty: FormControl<IStoreItem['qty']>;
  item: FormControl<IStoreItem['item']>;
  storeIds: FormControl<IStoreItem['storeIds']>;
};

export type StoreItemFormGroup = FormGroup<StoreItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StoreItemFormService {
  createStoreItemFormGroup(storeItem: StoreItemFormGroupInput = { id: null }): StoreItemFormGroup {
    const storeItemRawValue = {
      ...this.getFormDefaults(),
      ...storeItem,
    };
    return new FormGroup<StoreItemFormGroupContent>({
      id: new FormControl(
        { value: storeItemRawValue.id, disabled: storeItemRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      qty: new FormControl(storeItemRawValue.qty, {
        validators: [Validators.min(0)],
      }),
      item: new FormControl(storeItemRawValue.item),
      storeIds: new FormControl(storeItemRawValue.storeIds ?? []),
    });
  }

  getStoreItem(form: StoreItemFormGroup): IStoreItem | NewStoreItem {
    return form.getRawValue() as IStoreItem | NewStoreItem;
  }

  resetForm(form: StoreItemFormGroup, storeItem: StoreItemFormGroupInput): void {
    const storeItemRawValue = { ...this.getFormDefaults(), ...storeItem };
    form.reset(
      {
        ...storeItemRawValue,
        id: { value: storeItemRawValue.id, disabled: storeItemRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StoreItemFormDefaults {
    return {
      id: null,
      storeIds: [],
    };
  }
}
