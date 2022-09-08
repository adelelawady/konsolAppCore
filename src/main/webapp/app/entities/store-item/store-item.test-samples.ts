import { IStoreItem, NewStoreItem } from './store-item.model';

export const sampleWithRequiredData: IStoreItem = {
  id: '911e8ccf-40cd-426c-a6a5-648b86b9a4cb',
};

export const sampleWithPartialData: IStoreItem = {
  id: 'd076e06f-1394-4cd4-9e20-d6cff0d3c0b1',
  qty: 57828,
};

export const sampleWithFullData: IStoreItem = {
  id: '0c970d1d-27a4-4042-beab-26894301dd86',
  qty: 50995,
};

export const sampleWithNewData: NewStoreItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
