import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'invoiceItems'>;

type InvoiceFormGroupContent = {
  pk: FormControl<IInvoice['pk']>;
  id: FormControl<IInvoice['id'] | NewInvoice['id']>;
  kind: FormControl<IInvoice['kind']>;
  totalCost: FormControl<IInvoice['totalCost']>;
  totalPrice: FormControl<IInvoice['totalPrice']>;
  discountPer: FormControl<IInvoice['discountPer']>;
  discount: FormControl<IInvoice['discount']>;
  additions: FormControl<IInvoice['additions']>;
  additionsType: FormControl<IInvoice['additionsType']>;
  netCost: FormControl<IInvoice['netCost']>;
  netPrice: FormControl<IInvoice['netPrice']>;
  expenses: FormControl<IInvoice['expenses']>;
  expensesType: FormControl<IInvoice['expensesType']>;
  bank: FormControl<IInvoice['bank']>;
  item: FormControl<IInvoice['item']>;
  account: FormControl<IInvoice['account']>;
  invoiceItems: FormControl<IInvoice['invoiceItems']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = {
      ...this.getFormDefaults(),
      ...invoice,
    };
    return new FormGroup<InvoiceFormGroupContent>({
      pk: new FormControl(invoiceRawValue.pk),
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: invoiceRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      kind: new FormControl(invoiceRawValue.kind),
      totalCost: new FormControl(invoiceRawValue.totalCost, {
        validators: [Validators.min(0)],
      }),
      totalPrice: new FormControl(invoiceRawValue.totalPrice, {
        validators: [Validators.min(0)],
      }),
      discountPer: new FormControl(invoiceRawValue.discountPer, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      discount: new FormControl(invoiceRawValue.discount, {
        validators: [Validators.min(0)],
      }),
      additions: new FormControl(invoiceRawValue.additions, {
        validators: [Validators.min(0)],
      }),
      additionsType: new FormControl(invoiceRawValue.additionsType),
      netCost: new FormControl(invoiceRawValue.netCost, {
        validators: [Validators.min(0)],
      }),
      netPrice: new FormControl(invoiceRawValue.netPrice, {
        validators: [Validators.min(0)],
      }),
      expenses: new FormControl(invoiceRawValue.expenses, {
        validators: [Validators.min(0)],
      }),
      expensesType: new FormControl(invoiceRawValue.expensesType),
      bank: new FormControl(invoiceRawValue.bank),
      item: new FormControl(invoiceRawValue.item),
      account: new FormControl(invoiceRawValue.account),
      invoiceItems: new FormControl(invoiceRawValue.invoiceItems ?? []),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return form.getRawValue() as IInvoice | NewInvoice;
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = { ...this.getFormDefaults(), ...invoice };
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: invoiceRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    return {
      id: null,
      invoiceItems: [],
    };
  }
}
