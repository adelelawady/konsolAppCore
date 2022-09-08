import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IItem, NewItem } from '../item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItem for edit and NewItemFormGroupInput for create.
 */
type ItemFormGroupInput = IItem | PartialWithRequiredKeyOf<NewItem>;

type ItemFormDefaults = Pick<NewItem, 'id' | 'itemUnits'>;

type ItemFormGroupContent = {
  id: FormControl<IItem['id'] | NewItem['id']>;
  name: FormControl<IItem['name']>;
  barcode: FormControl<IItem['barcode']>;
  price1: FormControl<IItem['price1']>;
  price2: FormControl<IItem['price2']>;
  price3: FormControl<IItem['price3']>;
  category: FormControl<IItem['category']>;
  qty: FormControl<IItem['qty']>;
  cost: FormControl<IItem['cost']>;
  index: FormControl<IItem['index']>;
  itemUnits: FormControl<IItem['itemUnits']>;
};

export type ItemFormGroup = FormGroup<ItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ItemFormService {
  createItemFormGroup(item: ItemFormGroupInput = { id: null }): ItemFormGroup {
    const itemRawValue = {
      ...this.getFormDefaults(),
      ...item,
    };
    return new FormGroup<ItemFormGroupContent>({
      id: new FormControl(
        { value: itemRawValue.id, disabled: itemRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(itemRawValue.name, {
        validators: [Validators.required],
      }),
      barcode: new FormControl(itemRawValue.barcode),
      price1: new FormControl(itemRawValue.price1),
      price2: new FormControl(itemRawValue.price2),
      price3: new FormControl(itemRawValue.price3),
      category: new FormControl(itemRawValue.category),
      qty: new FormControl(itemRawValue.qty, {
        validators: [Validators.min(0)],
      }),
      cost: new FormControl(itemRawValue.cost, {
        validators: [Validators.min(0)],
      }),
      index: new FormControl(itemRawValue.index),
      itemUnits: new FormControl(itemRawValue.itemUnits ?? []),
    });
  }

  getItem(form: ItemFormGroup): IItem | NewItem {
    return form.getRawValue() as IItem | NewItem;
  }

  resetForm(form: ItemFormGroup, item: ItemFormGroupInput): void {
    const itemRawValue = { ...this.getFormDefaults(), ...item };
    form.reset(
      {
        ...itemRawValue,
        id: { value: itemRawValue.id, disabled: itemRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ItemFormDefaults {
    return {
      id: null,
      itemUnits: [],
    };
  }
}
