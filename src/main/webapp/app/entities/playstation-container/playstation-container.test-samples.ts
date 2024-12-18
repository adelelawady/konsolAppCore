import { IPlaystationContainer, NewPlaystationContainer } from './playstation-container.model';

export const sampleWithRequiredData: IPlaystationContainer = {
  id: '6d55b742-6deb-4177-9fe2-49456a8b3f0a',
  name: 'powerfully husk',
  category: 'mmm tame',
  hasTimeManagement: false,
  showType: false,
  showTime: false,
  canMoveDevice: false,
  canHaveMultiTimeManagement: true,
  acceptedOrderCategories: 'furiously drat',
  orderSelectedPriceCategory: 'bonnet without',
};

export const sampleWithPartialData: IPlaystationContainer = {
  id: '446be23e-0cc5-4506-b56e-26ffe65417e8',
  name: 'beautifully',
  category: 'pish',
  defaultIcon: 'geez because prickly',
  hasTimeManagement: false,
  showType: false,
  showTime: false,
  canMoveDevice: true,
  canHaveMultiTimeManagement: false,
  acceptedOrderCategories: 'yet',
  orderSelectedPriceCategory: 'because freely delightfully',
};

export const sampleWithFullData: IPlaystationContainer = {
  id: '89416342-0e63-4a6f-b66f-9f06dabeaf51',
  name: 'for what',
  category: 'gadzooks',
  defaultIcon: 'aside vulgarise zowie',
  hasTimeManagement: false,
  showType: false,
  showTime: true,
  canMoveDevice: true,
  canHaveMultiTimeManagement: true,
  acceptedOrderCategories: 'whereas pepper drowse',
  orderSelectedPriceCategory: 'daddy militate frozen',
};

export const sampleWithNewData: NewPlaystationContainer = {
  name: 'sheepishly',
  category: 'futon',
  hasTimeManagement: false,
  showType: true,
  showTime: true,
  canMoveDevice: true,
  canHaveMultiTimeManagement: false,
  acceptedOrderCategories: 'standard gut whoever',
  orderSelectedPriceCategory: 'why',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
