import { ICafeTable, NewCafeTable } from './cafe-table.model';

export const sampleWithRequiredData: ICafeTable = {
  id: 'fe68633c-6f81-4d22-aa79-852582eeb387',
  pk: 27175,
  name: 'accomplished from pearl',
  index: 6046,
  active: true,
};

export const sampleWithPartialData: ICafeTable = {
  id: 'f7e69da5-c0d0-42b0-a134-7bf4134d78b0',
  pk: 21417,
  name: 'how reconstitute',
  index: 26649,
  active: false,
};

export const sampleWithFullData: ICafeTable = {
  id: '3430e066-51fa-4bfa-bbea-4718631bc572',
  pk: 21434,
  name: 'hammock but why',
  index: 24061,
  active: true,
};

export const sampleWithNewData: NewCafeTable = {
  pk: 14411,
  name: 'hm dazzling rear',
  index: 13028,
  active: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
