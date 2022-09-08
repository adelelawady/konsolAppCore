import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMoney, NewMoney } from '../money.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMoney for edit and NewMoneyFormGroupInput for create.
 */
type MoneyFormGroupInput = IMoney | PartialWithRequiredKeyOf<NewMoney>;

type MoneyFormDefaults = Pick<NewMoney, 'id'>;

type MoneyFormGroupContent = {
  pk: FormControl<IMoney['pk']>;
  id: FormControl<IMoney['id'] | NewMoney['id']>;
  kind: FormControl<IMoney['kind']>;
  moneyIn: FormControl<IMoney['moneyIn']>;
  moneyOut: FormControl<IMoney['moneyOut']>;
  bank: FormControl<IMoney['bank']>;
  item: FormControl<IMoney['item']>;
  account: FormControl<IMoney['account']>;
};

export type MoneyFormGroup = FormGroup<MoneyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoneyFormService {
  createMoneyFormGroup(money: MoneyFormGroupInput = { id: null }): MoneyFormGroup {
    const moneyRawValue = {
      ...this.getFormDefaults(),
      ...money,
    };
    return new FormGroup<MoneyFormGroupContent>({
      pk: new FormControl(moneyRawValue.pk),
      id: new FormControl(
        { value: moneyRawValue.id, disabled: moneyRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      kind: new FormControl(moneyRawValue.kind),
      moneyIn: new FormControl(moneyRawValue.moneyIn, {
        validators: [Validators.min(0)],
      }),
      moneyOut: new FormControl(moneyRawValue.moneyOut, {
        validators: [Validators.min(0)],
      }),
      bank: new FormControl(moneyRawValue.bank),
      item: new FormControl(moneyRawValue.item),
      account: new FormControl(moneyRawValue.account),
    });
  }

  getMoney(form: MoneyFormGroup): IMoney | NewMoney {
    return form.getRawValue() as IMoney | NewMoney;
  }

  resetForm(form: MoneyFormGroup, money: MoneyFormGroupInput): void {
    const moneyRawValue = { ...this.getFormDefaults(), ...money };
    form.reset(
      {
        ...moneyRawValue,
        id: { value: moneyRawValue.id, disabled: moneyRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MoneyFormDefaults {
    return {
      id: null,
    };
  }
}
