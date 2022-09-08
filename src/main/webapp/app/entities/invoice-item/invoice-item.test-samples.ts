import { IInvoiceItem, NewInvoiceItem } from './invoice-item.model';

export const sampleWithRequiredData: IInvoiceItem = {
  id: '595b5cd2-e988-4138-a4f9-227e0c2abff6',
};

export const sampleWithPartialData: IInvoiceItem = {
  pk: 'markets hub extensible',
  id: '8f981066-a17d-40f2-a99a-509fbc5c3741',
  unitPieces: 69576,
  unitQtyIn: 82528,
  unitCost: 10880,
  discountPer: 34,
  discount: 93027,
  qtyIn: 97121,
  cost: 17946,
  netCost: 99101,
};

export const sampleWithFullData: IInvoiceItem = {
  pk: 'Handcrafted Pants PNG',
  id: 'ae5d4a12-17c6-48b5-9e00-2091043a973a',
  unit: 'contingency drive copy',
  unitPieces: 36215,
  userQty: 30517,
  unitQtyIn: 14399,
  unitQtyOut: 26987,
  unitCost: 86786,
  unitPrice: 92139,
  discountPer: 19,
  discount: 37776,
  totalCost: 42868,
  totalPrice: 31572,
  qtyIn: 97007,
  qtyOut: 5517,
  cost: 96642,
  price: 25679,
  netCost: 33162,
  netPrice: 71275,
};

export const sampleWithNewData: NewInvoiceItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
