import { IPlaystationDeviceType, NewPlaystationDeviceType } from './playstation-device-type.model';

export const sampleWithRequiredData: IPlaystationDeviceType = {
  id: '23f8aca6-f354-42c3-a2bd-7490f8f66404',
  name: 'ew',
  defaultMainPrice: 1091.18,
  productId: 'highlight abaft versus',
};

export const sampleWithPartialData: IPlaystationDeviceType = {
  id: '7cb762ba-78b2-4b48-80b8-9e5dbc739389',
  name: 'stiffen',
  defaultMainPrice: 19205.33,
  productId: 'phooey',
};

export const sampleWithFullData: IPlaystationDeviceType = {
  id: '965d21b8-df99-4734-923e-4d2b8cf32a24',
  name: 'ornate',
  defaultMainPrice: 19242.82,
  productId: 'hence',
};

export const sampleWithNewData: NewPlaystationDeviceType = {
  name: 'zany however freight',
  defaultMainPrice: 6588.72,
  productId: 'as colorfully jealously',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
