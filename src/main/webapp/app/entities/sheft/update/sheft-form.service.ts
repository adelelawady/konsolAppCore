import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISheft, NewSheft } from '../sheft.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISheft for edit and NewSheftFormGroupInput for create.
 */
type SheftFormGroupInput = ISheft | PartialWithRequiredKeyOf<NewSheft>;

type SheftFormDefaults = Pick<NewSheft, 'id' | 'active'>;

type SheftFormGroupContent = {
  id: FormControl<ISheft['id'] | NewSheft['id']>;
  startTime: FormControl<ISheft['startTime']>;
  endTime: FormControl<ISheft['endTime']>;
  active: FormControl<ISheft['active']>;
  assignedEmployee: FormControl<ISheft['assignedEmployee']>;
  duration: FormControl<ISheft['duration']>;
  totalprice: FormControl<ISheft['totalprice']>;
  totalCost: FormControl<ISheft['totalCost']>;
  netPrice: FormControl<ISheft['netPrice']>;
  netCost: FormControl<ISheft['netCost']>;
  netUserPrice: FormControl<ISheft['netUserPrice']>;
  totalItemsOut: FormControl<ISheft['totalItemsOut']>;
  discount: FormControl<ISheft['discount']>;
  invoicesAdditions: FormControl<ISheft['invoicesAdditions']>;
  additions: FormControl<ISheft['additions']>;
  additionsNotes: FormControl<ISheft['additionsNotes']>;
  invoicesExpenses: FormControl<ISheft['invoicesExpenses']>;
  sheftExpenses: FormControl<ISheft['sheftExpenses']>;
  totalinvoices: FormControl<ISheft['totalinvoices']>;
  totaldeletedItems: FormControl<ISheft['totaldeletedItems']>;
  totaldeletedItemsPrice: FormControl<ISheft['totaldeletedItemsPrice']>;
  notes: FormControl<ISheft['notes']>;
};

export type SheftFormGroup = FormGroup<SheftFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SheftFormService {
  createSheftFormGroup(sheft: SheftFormGroupInput = { id: null }): SheftFormGroup {
    const sheftRawValue = {
      ...this.getFormDefaults(),
      ...sheft,
    };
    return new FormGroup<SheftFormGroupContent>({
      id: new FormControl(
        { value: sheftRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startTime: new FormControl(sheftRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(sheftRawValue.endTime, {
        validators: [Validators.required],
      }),
      active: new FormControl(sheftRawValue.active, {
        validators: [Validators.required],
      }),
      assignedEmployee: new FormControl(sheftRawValue.assignedEmployee, {
        validators: [Validators.required],
      }),
      duration: new FormControl(sheftRawValue.duration, {
        validators: [Validators.required],
      }),
      totalprice: new FormControl(sheftRawValue.totalprice, {
        validators: [Validators.required],
      }),
      totalCost: new FormControl(sheftRawValue.totalCost, {
        validators: [Validators.required],
      }),
      netPrice: new FormControl(sheftRawValue.netPrice, {
        validators: [Validators.required],
      }),
      netCost: new FormControl(sheftRawValue.netCost, {
        validators: [Validators.required],
      }),
      netUserPrice: new FormControl(sheftRawValue.netUserPrice, {
        validators: [Validators.required],
      }),
      totalItemsOut: new FormControl(sheftRawValue.totalItemsOut, {
        validators: [Validators.required],
      }),
      discount: new FormControl(sheftRawValue.discount, {
        validators: [Validators.required],
      }),
      invoicesAdditions: new FormControl(sheftRawValue.invoicesAdditions, {
        validators: [Validators.required],
      }),
      additions: new FormControl(sheftRawValue.additions, {
        validators: [Validators.required],
      }),
      additionsNotes: new FormControl(sheftRawValue.additionsNotes, {
        validators: [Validators.required],
      }),
      invoicesExpenses: new FormControl(sheftRawValue.invoicesExpenses, {
        validators: [Validators.required],
      }),
      sheftExpenses: new FormControl(sheftRawValue.sheftExpenses, {
        validators: [Validators.required],
      }),
      totalinvoices: new FormControl(sheftRawValue.totalinvoices, {
        validators: [Validators.required],
      }),
      totaldeletedItems: new FormControl(sheftRawValue.totaldeletedItems, {
        validators: [Validators.required],
      }),
      totaldeletedItemsPrice: new FormControl(sheftRawValue.totaldeletedItemsPrice, {
        validators: [Validators.required],
      }),
      notes: new FormControl(sheftRawValue.notes),
    });
  }

  getSheft(form: SheftFormGroup): ISheft | NewSheft {
    return form.getRawValue() as ISheft | NewSheft;
  }

  resetForm(form: SheftFormGroup, sheft: SheftFormGroupInput): void {
    const sheftRawValue = { ...this.getFormDefaults(), ...sheft };
    form.reset(
      {
        ...sheftRawValue,
        id: { value: sheftRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SheftFormDefaults {
    return {
      id: null,
      active: false,
    };
  }
}
