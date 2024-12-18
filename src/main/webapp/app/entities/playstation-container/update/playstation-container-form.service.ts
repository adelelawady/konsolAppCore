import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlaystationContainer, NewPlaystationContainer } from '../playstation-container.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlaystationContainer for edit and NewPlaystationContainerFormGroupInput for create.
 */
type PlaystationContainerFormGroupInput = IPlaystationContainer | PartialWithRequiredKeyOf<NewPlaystationContainer>;

type PlaystationContainerFormDefaults = Pick<
  NewPlaystationContainer,
  'id' | 'hasTimeManagement' | 'showType' | 'showTime' | 'canMoveDevice' | 'canHaveMultiTimeManagement'
>;

type PlaystationContainerFormGroupContent = {
  id: FormControl<IPlaystationContainer['id'] | NewPlaystationContainer['id']>;
  name: FormControl<IPlaystationContainer['name']>;
  category: FormControl<IPlaystationContainer['category']>;
  defaultIcon: FormControl<IPlaystationContainer['defaultIcon']>;
  hasTimeManagement: FormControl<IPlaystationContainer['hasTimeManagement']>;
  showType: FormControl<IPlaystationContainer['showType']>;
  showTime: FormControl<IPlaystationContainer['showTime']>;
  canMoveDevice: FormControl<IPlaystationContainer['canMoveDevice']>;
  canHaveMultiTimeManagement: FormControl<IPlaystationContainer['canHaveMultiTimeManagement']>;
  acceptedOrderCategories: FormControl<IPlaystationContainer['acceptedOrderCategories']>;
  orderSelectedPriceCategory: FormControl<IPlaystationContainer['orderSelectedPriceCategory']>;
};

export type PlaystationContainerFormGroup = FormGroup<PlaystationContainerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlaystationContainerFormService {
  createPlaystationContainerFormGroup(
    playstationContainer: PlaystationContainerFormGroupInput = { id: null }
  ): PlaystationContainerFormGroup {
    const playstationContainerRawValue = {
      ...this.getFormDefaults(),
      ...playstationContainer,
    };
    return new FormGroup<PlaystationContainerFormGroupContent>({
      id: new FormControl(
        { value: playstationContainerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(playstationContainerRawValue.name, {
        validators: [Validators.required],
      }),
      category: new FormControl(playstationContainerRawValue.category, {
        validators: [Validators.required],
      }),
      defaultIcon: new FormControl(playstationContainerRawValue.defaultIcon),
      hasTimeManagement: new FormControl(playstationContainerRawValue.hasTimeManagement, {
        validators: [Validators.required],
      }),
      showType: new FormControl(playstationContainerRawValue.showType, {
        validators: [Validators.required],
      }),
      showTime: new FormControl(playstationContainerRawValue.showTime, {
        validators: [Validators.required],
      }),
      canMoveDevice: new FormControl(playstationContainerRawValue.canMoveDevice, {
        validators: [Validators.required],
      }),
      canHaveMultiTimeManagement: new FormControl(playstationContainerRawValue.canHaveMultiTimeManagement, {
        validators: [Validators.required],
      }),
      acceptedOrderCategories: new FormControl(playstationContainerRawValue.acceptedOrderCategories, {
        validators: [Validators.required],
      }),
      orderSelectedPriceCategory: new FormControl(playstationContainerRawValue.orderSelectedPriceCategory, {
        validators: [Validators.required],
      }),
    });
  }

  getPlaystationContainer(form: PlaystationContainerFormGroup): IPlaystationContainer | NewPlaystationContainer {
    return form.getRawValue() as IPlaystationContainer | NewPlaystationContainer;
  }

  resetForm(form: PlaystationContainerFormGroup, playstationContainer: PlaystationContainerFormGroupInput): void {
    const playstationContainerRawValue = { ...this.getFormDefaults(), ...playstationContainer };
    form.reset(
      {
        ...playstationContainerRawValue,
        id: { value: playstationContainerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PlaystationContainerFormDefaults {
    return {
      id: null,
      hasTimeManagement: false,
      showType: false,
      showTime: false,
      canMoveDevice: false,
      canHaveMultiTimeManagement: false,
    };
  }
}
