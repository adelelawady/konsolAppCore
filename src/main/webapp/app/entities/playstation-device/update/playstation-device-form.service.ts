import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';

export type PlaystationDeviceFormGroup = FormGroup<{
  id: FormControl<string | null>;
  name: FormControl<string | null>;
  active: FormControl<string | null>;
  type: FormControl<PsDeviceType | null>;
}>;

@Injectable({ providedIn: 'root' })
export class PlaystationDeviceFormService {
  createPlaystationDeviceFormGroup(device: PsDeviceDTO = {}): PlaystationDeviceFormGroup {
    return new FormGroup<{
      id: FormControl<string | null>;
      name: FormControl<string | null>;
      active: FormControl<string | null>;
      type: FormControl<PsDeviceType | null>;
    }>({
      id: new FormControl<string | null>({ value: device.id || null, disabled: true }),
      name: new FormControl<string | null>(device.name || null, {
        validators: [Validators.required],
      }),
      active: new FormControl<string | null>(device.active || 'false'),
      type: new FormControl<PsDeviceType | null>(null, {
        validators: [Validators.required],
      }),
    });
  }

  getPlaystationDevice(form: PlaystationDeviceFormGroup): PsDeviceDTO {
    return {
      id: form.get(['id'])?.value || undefined,
      name: form.get(['name'])?.value || undefined,
      active: form.get(['active'])?.value || 'false',
      type: form.get(['type'])?.value || undefined,
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
      name: undefined,
      active: 'false',
      type: undefined
    };
  }
}
