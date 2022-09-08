import { InvoiceKind } from 'app/entities/enumerations/invoice-kind.model';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 'e9811367-3777-4285-ab8f-e916c058e54f',
};

export const sampleWithPartialData: IInvoice = {
  pk: 'Creative Intelligent Berkshire',
  id: '041cf839-c78b-4c7a-94f9-872707ae10f5',
  kind: InvoiceKind['SALE'],
  totalPrice: 77441,
  discount: 68108,
  netCost: 18561,
  expenses: 26926,
};

export const sampleWithFullData: IInvoice = {
  pk: 'parse Nigeria',
  id: '2ac5036d-4106-4258-83fa-9212be220d5f',
  kind: InvoiceKind['SALEQUOTE'],
  totalCost: 7901,
  totalPrice: 19995,
  discountPer: 0,
  discount: 65052,
  additions: 189,
  additionsType: 'SSL Shirt',
  netCost: 78733,
  netPrice: 47833,
  expenses: 56992,
  expensesType: 'card Cambridgeshire',
};

export const sampleWithNewData: NewInvoice = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
