import { AccountKind } from 'app/entities/enumerations/account-kind.model';

import { IAccountUser, NewAccountUser } from './account-user.model';

export const sampleWithRequiredData: IAccountUser = {
  id: '9f37109e-77ed-4459-8362-589a39ed3c60',
  name: 'card COM',
};

export const sampleWithPartialData: IAccountUser = {
  id: '121e0fb0-bfea-4e76-8b81-b72dd0cb9866',
  name: 'Dynamic Handmade redefine',
  balanceOut: 19314,
  address: 'vertical Balanced SDD',
  address2: 'Texas Towels',
};

export const sampleWithFullData: IAccountUser = {
  id: 'b5add554-f8e1-4665-b925-a9a6824cb2cc',
  name: 'Ports bypass envisioneer',
  kind: AccountKind['CUSTOMER'],
  balanceIn: 15603,
  balanceOut: 63985,
  phone: '1-540-408-9851 x61890',
  address: 'blue',
  address2: 'Loan Optimization',
};

export const sampleWithNewData: NewAccountUser = {
  name: 'Credit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
