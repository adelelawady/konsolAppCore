import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 'e875fcbf-77a9-4cfe-90c6-67913aae4305',
  name: 'proactive Peru harness',
};

export const sampleWithPartialData: IItem = {
  id: '4905ca6a-fb64-4ab9-919e-28ef64f72271',
  name: 'override Estate',
  price3: 'Ethiopia',
  category: '24 Dynamic',
  qty: 37490,
  cost: 22220,
};

export const sampleWithFullData: IItem = {
  id: 'd936396c-2534-4b35-9d2d-eaefdcefe5ef',
  name: 'system Planner Villages',
  barcode: 'Arkansas',
  price1: 'Handmade',
  price2: 'Island payment',
  price3: 'orchestration THX Brand',
  category: 'unleash Strategist copying',
  qty: 87225,
  cost: 44941,
  index: 1008,
};

export const sampleWithNewData: NewItem = {
  name: 'motivating violet Designer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
