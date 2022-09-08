import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPk, NewPk } from '../pk.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPk for edit and NewPkFormGroupInput for create.
 */
type PkFormGroupInput = IPk | PartialWithRequiredKeyOf<NewPk>;

type PkFormDefaults = Pick<NewPk, 'id'>;

type PkFormGroupContent = {
  id: FormControl<IPk['id'] | NewPk['id']>;
  kind: FormControl<IPk['kind']>;
  value: FormControl<IPk['value']>;
};

export type PkFormGroup = FormGroup<PkFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PkFormService {
  createPkFormGroup(pk: PkFormGroupInput = { id: null }): PkFormGroup {
    const pkRawValue = {
      ...this.getFormDefaults(),
      ...pk,
    };
    return new FormGroup<PkFormGroupContent>({
      id: new FormControl(
        { value: pkRawValue.id, disabled: pkRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      kind: new FormControl(pkRawValue.kind),
      value: new FormControl(pkRawValue.value, {
        validators: [Validators.min(0)],
      }),
    });
  }

  getPk(form: PkFormGroup): IPk | NewPk {
    return form.getRawValue() as IPk | NewPk;
  }

  resetForm(form: PkFormGroup, pk: PkFormGroupInput): void {
    const pkRawValue = { ...this.getFormDefaults(), ...pk };
    form.reset(
      {
        ...pkRawValue,
        id: { value: pkRawValue.id, disabled: pkRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PkFormDefaults {
    return {
      id: null,
    };
  }
}
