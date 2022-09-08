import { MoneyKind } from 'app/entities/enumerations/money-kind.model';

import { IMoney, NewMoney } from './money.model';

export const sampleWithRequiredData: IMoney = {
  id: '91aaa92a-7d62-4eec-b1e3-e67325e110ff',
};

export const sampleWithPartialData: IMoney = {
  pk: 'Practical digital multi-byte',
  id: 'beaaa142-3edf-4163-887b-6492b07a4180',
  moneyIn: 13851,
};

export const sampleWithFullData: IMoney = {
  pk: 'Accounts',
  id: '7878999f-ef2d-4736-a02a-6cca6361cf6d',
  kind: MoneyKind['PAYMENT'],
  moneyIn: 84237,
  moneyOut: 70695,
};

export const sampleWithNewData: NewMoney = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
