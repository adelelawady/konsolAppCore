import { IPlaystationDevice, NewPlaystationDevice } from './playstation-device.model';

export const sampleWithRequiredData: IPlaystationDevice = {
  id: 'efaf8919-0700-4951-a16a-e5e5fc97fb3b',
  pk: 4363,
  name: 'lone the',
  index: 20256,
  active: true,
};

export const sampleWithPartialData: IPlaystationDevice = {
  id: 'b6751fc5-0c61-41c5-8de3-53075b67b5c7',
  pk: 10389,
  name: 'what retrospectivity softly',
  index: 28942,
  active: false,
};

export const sampleWithFullData: IPlaystationDevice = {
  id: 'a9792cc4-e478-4aaf-9e8b-43af1de69157',
  pk: 1853,
  name: 'ski reproachfully',
  index: 4574,
  active: false,
};

export const sampleWithNewData: NewPlaystationDevice = {
  pk: 32697,
  name: 'preheat',
  index: 779,
  active: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
