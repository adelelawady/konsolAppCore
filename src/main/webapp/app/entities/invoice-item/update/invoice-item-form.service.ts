import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInvoiceItem, NewInvoiceItem } from '../invoice-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceItem for edit and NewInvoiceItemFormGroupInput for create.
 */
type InvoiceItemFormGroupInput = IInvoiceItem | PartialWithRequiredKeyOf<NewInvoiceItem>;

type InvoiceItemFormDefaults = Pick<NewInvoiceItem, 'id' | 'invoices'>;

type InvoiceItemFormGroupContent = {
  pk: FormControl<IInvoiceItem['pk']>;
  id: FormControl<IInvoiceItem['id'] | NewInvoiceItem['id']>;
  unit: FormControl<IInvoiceItem['unit']>;
  unitPieces: FormControl<IInvoiceItem['unitPieces']>;
  userQty: FormControl<IInvoiceItem['userQty']>;
  unitQtyIn: FormControl<IInvoiceItem['unitQtyIn']>;
  unitQtyOut: FormControl<IInvoiceItem['unitQtyOut']>;
  unitCost: FormControl<IInvoiceItem['unitCost']>;
  unitPrice: FormControl<IInvoiceItem['unitPrice']>;
  discountPer: FormControl<IInvoiceItem['discountPer']>;
  discount: FormControl<IInvoiceItem['discount']>;
  totalCost: FormControl<IInvoiceItem['totalCost']>;
  totalPrice: FormControl<IInvoiceItem['totalPrice']>;
  qtyIn: FormControl<IInvoiceItem['qtyIn']>;
  qtyOut: FormControl<IInvoiceItem['qtyOut']>;
  cost: FormControl<IInvoiceItem['cost']>;
  price: FormControl<IInvoiceItem['price']>;
  netCost: FormControl<IInvoiceItem['netCost']>;
  netPrice: FormControl<IInvoiceItem['netPrice']>;
  invoices: FormControl<IInvoiceItem['invoices']>;
};

export type InvoiceItemFormGroup = FormGroup<InvoiceItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceItemFormService {
  createInvoiceItemFormGroup(invoiceItem: InvoiceItemFormGroupInput = { id: null }): InvoiceItemFormGroup {
    const invoiceItemRawValue = {
      ...this.getFormDefaults(),
      ...invoiceItem,
    };
    return new FormGroup<InvoiceItemFormGroupContent>({
      pk: new FormControl(invoiceItemRawValue.pk),
      id: new FormControl(
        { value: invoiceItemRawValue.id, disabled: invoiceItemRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      unit: new FormControl(invoiceItemRawValue.unit),
      unitPieces: new FormControl(invoiceItemRawValue.unitPieces, {
        validators: [Validators.min(0)],
      }),
      userQty: new FormControl(invoiceItemRawValue.userQty, {
        validators: [Validators.min(0)],
      }),
      unitQtyIn: new FormControl(invoiceItemRawValue.unitQtyIn, {
        validators: [Validators.min(0)],
      }),
      unitQtyOut: new FormControl(invoiceItemRawValue.unitQtyOut, {
        validators: [Validators.min(0)],
      }),
      unitCost: new FormControl(invoiceItemRawValue.unitCost, {
        validators: [Validators.min(0)],
      }),
      unitPrice: new FormControl(invoiceItemRawValue.unitPrice, {
        validators: [Validators.min(0)],
      }),
      discountPer: new FormControl(invoiceItemRawValue.discountPer, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      discount: new FormControl(invoiceItemRawValue.discount, {
        validators: [Validators.min(0)],
      }),
      totalCost: new FormControl(invoiceItemRawValue.totalCost, {
        validators: [Validators.min(0)],
      }),
      totalPrice: new FormControl(invoiceItemRawValue.totalPrice, {
        validators: [Validators.min(0)],
      }),
      qtyIn: new FormControl(invoiceItemRawValue.qtyIn, {
        validators: [Validators.min(0)],
      }),
      qtyOut: new FormControl(invoiceItemRawValue.qtyOut, {
        validators: [Validators.min(0)],
      }),
      cost: new FormControl(invoiceItemRawValue.cost, {
        validators: [Validators.min(0)],
      }),
      price: new FormControl(invoiceItemRawValue.price, {
        validators: [Validators.min(0)],
      }),
      netCost: new FormControl(invoiceItemRawValue.netCost, {
        validators: [Validators.min(0)],
      }),
      netPrice: new FormControl(invoiceItemRawValue.netPrice, {
        validators: [Validators.min(0)],
      }),
      invoices: new FormControl(invoiceItemRawValue.invoices ?? []),
    });
  }

  getInvoiceItem(form: InvoiceItemFormGroup): IInvoiceItem | NewInvoiceItem {
    return form.getRawValue() as IInvoiceItem | NewInvoiceItem;
  }

  resetForm(form: InvoiceItemFormGroup, invoiceItem: InvoiceItemFormGroupInput): void {
    const invoiceItemRawValue = { ...this.getFormDefaults(), ...invoiceItem };
    form.reset(
      {
        ...invoiceItemRawValue,
        id: { value: invoiceItemRawValue.id, disabled: invoiceItemRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InvoiceItemFormDefaults {
    return {
      id: null,
      invoices: [],
    };
  }
}
