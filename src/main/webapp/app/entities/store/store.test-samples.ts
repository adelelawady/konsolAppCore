import { IStore, NewStore } from './store.model';

export const sampleWithRequiredData: IStore = {
  id: '156f3007-ab41-48df-ae38-6c5c063a4222',
  name: 'Parkway Gorgeous',
};

export const sampleWithPartialData: IStore = {
  id: '186fdb4f-470b-4ef6-afcb-d67cd2471ecd',
  name: 'Gloves Court',
};

export const sampleWithFullData: IStore = {
  id: '760fe8b5-f2cd-4e98-924e-2d40da0e123b',
  name: 'Division',
};

export const sampleWithNewData: NewStore = {
  name: 'Idaho',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
