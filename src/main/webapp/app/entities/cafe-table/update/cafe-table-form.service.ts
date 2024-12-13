import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICafeTable, NewCafeTable } from '../cafe-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICafeTable for edit and NewCafeTableFormGroupInput for create.
 */
type CafeTableFormGroupInput = ICafeTable | PartialWithRequiredKeyOf<NewCafeTable>;

type CafeTableFormDefaults = Pick<NewCafeTable, 'id' | 'active'>;

type CafeTableFormGroupContent = {
  id: FormControl<ICafeTable['id'] | NewCafeTable['id']>;
  pk: FormControl<ICafeTable['pk']>;
  name: FormControl<ICafeTable['name']>;
  index: FormControl<ICafeTable['index']>;
  active: FormControl<ICafeTable['active']>;
};

export type CafeTableFormGroup = FormGroup<CafeTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CafeTableFormService {
  createCafeTableFormGroup(cafeTable: CafeTableFormGroupInput = { id: null }): CafeTableFormGroup {
    const cafeTableRawValue = {
      ...this.getFormDefaults(),
      ...cafeTable,
    };
    return new FormGroup<CafeTableFormGroupContent>({
      id: new FormControl(
        { value: cafeTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pk: new FormControl(cafeTableRawValue.pk, {
        validators: [Validators.required],
      }),
      name: new FormControl(cafeTableRawValue.name, {
        validators: [Validators.required],
      }),
      index: new FormControl(cafeTableRawValue.index, {
        validators: [Validators.required],
      }),
      active: new FormControl(cafeTableRawValue.active, {
        validators: [Validators.required],
      }),
    });
  }

  getCafeTable(form: CafeTableFormGroup): ICafeTable | NewCafeTable {
    return form.getRawValue() as ICafeTable | NewCafeTable;
  }

  resetForm(form: CafeTableFormGroup, cafeTable: CafeTableFormGroupInput): void {
    const cafeTableRawValue = { ...this.getFormDefaults(), ...cafeTable };
    form.reset(
      {
        ...cafeTableRawValue,
        id: { value: cafeTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CafeTableFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
