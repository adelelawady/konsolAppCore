import { IItemUnit, NewItemUnit } from './item-unit.model';

export const sampleWithRequiredData: IItemUnit = {
  id: '8dd09fb9-6cc8-4c5d-ad5a-4e5c8f201383',
  name: 'even-keeled',
};

export const sampleWithPartialData: IItemUnit = {
  id: '06de0b50-fa80-45e3-8bc0-e7c8ffa5de3c',
  name: 'Human withdrawal',
  price: 47208,
};

export const sampleWithFullData: IItemUnit = {
  id: '2dfdc0ba-215c-4665-99d9-7adc73488b27',
  name: 'Facilitator Gorgeous Cambridgeshire',
  pieces: 57433,
  price: 34558,
};

export const sampleWithNewData: NewItemUnit = {
  name: 'services Chicken Fresh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
