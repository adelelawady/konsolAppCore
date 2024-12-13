import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlaystationDevice, NewPlaystationDevice } from '../playstation-device.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlaystationDevice for edit and NewPlaystationDeviceFormGroupInput for create.
 */
type PlaystationDeviceFormGroupInput = IPlaystationDevice | PartialWithRequiredKeyOf<NewPlaystationDevice>;

type PlaystationDeviceFormDefaults = Pick<NewPlaystationDevice, 'id' | 'active'>;

type PlaystationDeviceFormGroupContent = {
  id: FormControl<IPlaystationDevice['id'] | NewPlaystationDevice['id']>;
  pk: FormControl<IPlaystationDevice['pk']>;
  name: FormControl<IPlaystationDevice['name']>;
  index: FormControl<IPlaystationDevice['index']>;
  active: FormControl<IPlaystationDevice['active']>;
};

export type PlaystationDeviceFormGroup = FormGroup<PlaystationDeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlaystationDeviceFormService {
  createPlaystationDeviceFormGroup(playstationDevice: PlaystationDeviceFormGroupInput = { id: null }): PlaystationDeviceFormGroup {
    const playstationDeviceRawValue = {
      ...this.getFormDefaults(),
      ...playstationDevice,
    };
    return new FormGroup<PlaystationDeviceFormGroupContent>({
      id: new FormControl(
        { value: playstationDeviceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pk: new FormControl(playstationDeviceRawValue.pk, {
        validators: [Validators.required],
      }),
      name: new FormControl(playstationDeviceRawValue.name, {
        validators: [Validators.required],
      }),
      index: new FormControl(playstationDeviceRawValue.index, {
        validators: [Validators.required],
      }),
      active: new FormControl(playstationDeviceRawValue.active, {
        validators: [Validators.required],
      }),
    });
  }

  getPlaystationDevice(form: PlaystationDeviceFormGroup): IPlaystationDevice | NewPlaystationDevice {
    return form.getRawValue() as IPlaystationDevice | NewPlaystationDevice;
  }

  resetForm(form: PlaystationDeviceFormGroup, playstationDevice: PlaystationDeviceFormGroupInput): void {
    const playstationDeviceRawValue = { ...this.getFormDefaults(), ...playstationDevice };
    form.reset(
      {
        ...playstationDeviceRawValue,
        id: { value: playstationDeviceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlaystationDeviceFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
