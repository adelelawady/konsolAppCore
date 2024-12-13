import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPriceOption, NewPriceOption } from '../price-option.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPriceOption for edit and NewPriceOptionFormGroupInput for create.
 */
type PriceOptionFormGroupInput = IPriceOption | PartialWithRequiredKeyOf<NewPriceOption>;

type PriceOptionFormDefaults = Pick<NewPriceOption, 'id'>;

type PriceOptionFormGroupContent = {
  id: FormControl<IPriceOption['id'] | NewPriceOption['id']>;
  name: FormControl<IPriceOption['name']>;
  value: FormControl<IPriceOption['value']>;
  productId: FormControl<IPriceOption['productId']>;
};

export type PriceOptionFormGroup = FormGroup<PriceOptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PriceOptionFormService {
  createPriceOptionFormGroup(priceOption: PriceOptionFormGroupInput = { id: null }): PriceOptionFormGroup {
    const priceOptionRawValue = {
      ...this.getFormDefaults(),
      ...priceOption,
    };
    return new FormGroup<PriceOptionFormGroupContent>({
      id: new FormControl(
        { value: priceOptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(priceOptionRawValue.name, {
        validators: [Validators.required],
      }),
      value: new FormControl(priceOptionRawValue.value, {
        validators: [Validators.required],
      }),
      productId: new FormControl(priceOptionRawValue.productId, {
        validators: [Validators.required],
      }),
    });
  }

  getPriceOption(form: PriceOptionFormGroup): IPriceOption | NewPriceOption {
    return form.getRawValue() as IPriceOption | NewPriceOption;
  }

  resetForm(form: PriceOptionFormGroup, priceOption: PriceOptionFormGroupInput): void {
    const priceOptionRawValue = { ...this.getFormDefaults(), ...priceOption };
    form.reset(
      {
        ...priceOptionRawValue,
        id: { value: priceOptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PriceOptionFormDefaults {
    return {
      id: null,
    };
  }
}
