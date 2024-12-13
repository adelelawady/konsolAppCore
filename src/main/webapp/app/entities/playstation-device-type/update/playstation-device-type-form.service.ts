import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlaystationDeviceType, NewPlaystationDeviceType } from '../playstation-device-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlaystationDeviceType for edit and NewPlaystationDeviceTypeFormGroupInput for create.
 */
type PlaystationDeviceTypeFormGroupInput = IPlaystationDeviceType | PartialWithRequiredKeyOf<NewPlaystationDeviceType>;

type PlaystationDeviceTypeFormDefaults = Pick<NewPlaystationDeviceType, 'id'>;

type PlaystationDeviceTypeFormGroupContent = {
  id: FormControl<IPlaystationDeviceType['id'] | NewPlaystationDeviceType['id']>;
  name: FormControl<IPlaystationDeviceType['name']>;
  defaultMainPrice: FormControl<IPlaystationDeviceType['defaultMainPrice']>;
  productId: FormControl<IPlaystationDeviceType['productId']>;
};

export type PlaystationDeviceTypeFormGroup = FormGroup<PlaystationDeviceTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlaystationDeviceTypeFormService {
  createPlaystationDeviceTypeFormGroup(
    playstationDeviceType: PlaystationDeviceTypeFormGroupInput = { id: null },
  ): PlaystationDeviceTypeFormGroup {
    const playstationDeviceTypeRawValue = {
      ...this.getFormDefaults(),
      ...playstationDeviceType,
    };
    return new FormGroup<PlaystationDeviceTypeFormGroupContent>({
      id: new FormControl(
        { value: playstationDeviceTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(playstationDeviceTypeRawValue.name, {
        validators: [Validators.required],
      }),
      defaultMainPrice: new FormControl(playstationDeviceTypeRawValue.defaultMainPrice, {
        validators: [Validators.required],
      }),
      productId: new FormControl(playstationDeviceTypeRawValue.productId, {
        validators: [Validators.required],
      }),
    });
  }

  getPlaystationDeviceType(form: PlaystationDeviceTypeFormGroup): IPlaystationDeviceType | NewPlaystationDeviceType {
    return form.getRawValue() as IPlaystationDeviceType | NewPlaystationDeviceType;
  }

  resetForm(form: PlaystationDeviceTypeFormGroup, playstationDeviceType: PlaystationDeviceTypeFormGroupInput): void {
    const playstationDeviceTypeRawValue = { ...this.getFormDefaults(), ...playstationDeviceType };
    form.reset(
      {
        ...playstationDeviceTypeRawValue,
        id: { value: playstationDeviceTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlaystationDeviceTypeFormDefaults {
    return {
      id: null,
    };
  }
}
