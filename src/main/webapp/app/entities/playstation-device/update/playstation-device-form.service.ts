import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';

type PlaystationDeviceFormGroupContent = {
  id: FormControl<string | null>;
  name: FormControl<string | null>;
  active: FormControl<string | null>;
  type: FormControl<PsDeviceType | null>;
};

export type PlaystationDeviceFormGroup = FormGroup<PlaystationDeviceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlaystationDeviceFormService {
  createPlaystationDeviceFormGroup(device: PsDeviceDTO = {}): PlaystationDeviceFormGroup {
    return new FormGroup<PlaystationDeviceFormGroupContent>({
      id: new FormControl<string | null>(device.id || null),
      name: new FormControl<string | null>(device.name || null, {
        validators: [Validators.required],
      }),
      active: new FormControl<string | null>(device.active || null),
      type: new FormControl<PsDeviceType | null>(device.type || null),
    });
  }

  getPlaystationDevice(form: PlaystationDeviceFormGroup): PsDeviceDTO {
    const formValue = form.getRawValue();
    return {
      id: formValue.id || undefined,
      name: formValue.name || undefined,
      active: formValue.active || undefined,
      type: formValue.type || undefined,
    };
  }

  resetForm(form: PlaystationDeviceFormGroup, device: PsDeviceDTO): void {
    const deviceRawValue = { ...this.getFormDefaults(), ...device };
    form.reset(
      {
        ...deviceRawValue,
        id: { value: deviceRawValue.id, disabled: true },
      } as any
    );
  }

  private getFormDefaults(): PsDeviceDTO {
    return {
      id: undefined,
      active: 'false',
      name: '',
      type: undefined
    };
  }
}
