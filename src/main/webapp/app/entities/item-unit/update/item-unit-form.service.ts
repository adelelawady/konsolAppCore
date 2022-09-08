import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IItemUnit, NewItemUnit } from '../item-unit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItemUnit for edit and NewItemUnitFormGroupInput for create.
 */
type ItemUnitFormGroupInput = IItemUnit | PartialWithRequiredKeyOf<NewItemUnit>;

type ItemUnitFormDefaults = Pick<NewItemUnit, 'id' | 'items'>;

type ItemUnitFormGroupContent = {
  id: FormControl<IItemUnit['id'] | NewItemUnit['id']>;
  name: FormControl<IItemUnit['name']>;
  pieces: FormControl<IItemUnit['pieces']>;
  price: FormControl<IItemUnit['price']>;
  items: FormControl<IItemUnit['items']>;
};

export type ItemUnitFormGroup = FormGroup<ItemUnitFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ItemUnitFormService {
  createItemUnitFormGroup(itemUnit: ItemUnitFormGroupInput = { id: null }): ItemUnitFormGroup {
    const itemUnitRawValue = {
      ...this.getFormDefaults(),
      ...itemUnit,
    };
    return new FormGroup<ItemUnitFormGroupContent>({
      id: new FormControl(
        { value: itemUnitRawValue.id, disabled: itemUnitRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(itemUnitRawValue.name, {
        validators: [Validators.required],
      }),
      pieces: new FormControl(itemUnitRawValue.pieces, {
        validators: [Validators.min(0)],
      }),
      price: new FormControl(itemUnitRawValue.price, {
        validators: [Validators.min(0)],
      }),
      items: new FormControl(itemUnitRawValue.items ?? []),
    });
  }

  getItemUnit(form: ItemUnitFormGroup): IItemUnit | NewItemUnit {
    return form.getRawValue() as IItemUnit | NewItemUnit;
  }

  resetForm(form: ItemUnitFormGroup, itemUnit: ItemUnitFormGroupInput): void {
    const itemUnitRawValue = { ...this.getFormDefaults(), ...itemUnit };
    form.reset(
      {
        ...itemUnitRawValue,
        id: { value: itemUnitRawValue.id, disabled: itemUnitRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ItemUnitFormDefaults {
    return {
      id: null,
      items: [],
    };
  }
}
