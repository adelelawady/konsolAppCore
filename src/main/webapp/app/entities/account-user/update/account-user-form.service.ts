import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccountUser, NewAccountUser } from '../account-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountUser for edit and NewAccountUserFormGroupInput for create.
 */
type AccountUserFormGroupInput = IAccountUser | PartialWithRequiredKeyOf<NewAccountUser>;

type AccountUserFormDefaults = Pick<NewAccountUser, 'id'>;

type AccountUserFormGroupContent = {
  id: FormControl<IAccountUser['id'] | NewAccountUser['id']>;
  name: FormControl<IAccountUser['name']>;
  kind: FormControl<IAccountUser['kind']>;
  balanceIn: FormControl<IAccountUser['balanceIn']>;
  balanceOut: FormControl<IAccountUser['balanceOut']>;
  phone: FormControl<IAccountUser['phone']>;
  address: FormControl<IAccountUser['address']>;
  address2: FormControl<IAccountUser['address2']>;
};

export type AccountUserFormGroup = FormGroup<AccountUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountUserFormService {
  createAccountUserFormGroup(accountUser: AccountUserFormGroupInput = { id: null }): AccountUserFormGroup {
    const accountUserRawValue = {
      ...this.getFormDefaults(),
      ...accountUser,
    };
    return new FormGroup<AccountUserFormGroupContent>({
      id: new FormControl(
        { value: accountUserRawValue.id, disabled: accountUserRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(accountUserRawValue.name, {
        validators: [Validators.required],
      }),
      kind: new FormControl(accountUserRawValue.kind),
      balanceIn: new FormControl(accountUserRawValue.balanceIn, {
        validators: [Validators.min(0)],
      }),
      balanceOut: new FormControl(accountUserRawValue.balanceOut, {
        validators: [Validators.min(0)],
      }),
      phone: new FormControl(accountUserRawValue.phone),
      address: new FormControl(accountUserRawValue.address),
      address2: new FormControl(accountUserRawValue.address2),
    });
  }

  getAccountUser(form: AccountUserFormGroup): IAccountUser | NewAccountUser {
    return form.getRawValue() as IAccountUser | NewAccountUser;
  }

  resetForm(form: AccountUserFormGroup, accountUser: AccountUserFormGroupInput): void {
    const accountUserRawValue = { ...this.getFormDefaults(), ...accountUser };
    form.reset(
      {
        ...accountUserRawValue,
        id: { value: accountUserRawValue.id, disabled: accountUserRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AccountUserFormDefaults {
    return {
      id: null,
    };
  }
}
