import { inject, Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';
import { PlaystationContainerStateService } from 'app/pages/playstation/services/playstation-container.service';

export type PlaystationDeviceFormGroup = FormGroup<{
  id: FormControl<string | null>;
  name: FormControl<string | null>;
  active: FormControl<boolean | null>;
  type: FormControl<PsDeviceType | null>;
  category: FormControl<string | null>;
  timeManagement: FormControl<boolean | null>;
}>;

@Injectable({ providedIn: 'root' })
export class PlaystationDeviceFormService {
  private containerStateService = inject(PlaystationContainerStateService);
  createPlaystationDeviceFormGroup(device: PsDeviceDTO = {}): PlaystationDeviceFormGroup {
    return new FormGroup<{
      id: FormControl<string | null>;
      name: FormControl<string | null>;
      active: FormControl<boolean | null>;
      type: FormControl<PsDeviceType | null>;
      category: FormControl<string | null>;
      timeManagement: FormControl<boolean | null>;
    }>({
      id: new FormControl<string | null>({ value: device.id ?? null, disabled: true }),
      name: new FormControl<string | null>(device.name ?? null, {
        validators: [Validators.required],
      }),
      active: new FormControl<boolean | null>(device.active ?? false),
      type: new FormControl<PsDeviceType | null>(null, {
        validators: [Validators.required],
      }),
      category: new FormControl<string | null>(device.category ?? null),
      timeManagement: new FormControl<boolean | null>(device.timeManagement ?? false),
    });
  }

  getPlaystationDevice(form: PlaystationDeviceFormGroup): PsDeviceDTO {
    return {
      id: form.get(['id'])?.value || undefined,
      name: form.get(['name'])?.value || undefined,
      active: form.get(['active'])?.value || false,
      type: form.get(['type'])?.value || undefined,
      category: this.containerStateService.getCurrentContainer()?.category || undefined,
      timeManagement: form.get(['timeManagement'])?.value || false,
    };
  }

  resetForm(form: PlaystationDeviceFormGroup, device: PsDeviceDTO): void {
    const deviceRawValue = { ...this.getFormDefaults(), ...device };
    form.reset({
      ...deviceRawValue,
      id: { value: deviceRawValue.id, disabled: true },
    } as any);
  }

  private getFormDefaults(): PsDeviceDTO {
    return {
      id: undefined,
      name: undefined,
      active: false,
      type: undefined,
      category: this.containerStateService.getCurrentContainer()?.category || undefined,
      timeManagement: false,
    };
  }
}
