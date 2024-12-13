import { IPriceOption, NewPriceOption } from './price-option.model';

export const sampleWithRequiredData: IPriceOption = {
  id: '85c19708-5166-4af3-8ab4-5c77327a3d1a',
  name: 'coolly anenst',
  value: 11758.7,
  productId: 'underneath able oh',
};

export const sampleWithPartialData: IPriceOption = {
  id: '67f33901-7587-4911-8987-a829de0bbf79',
  name: 'petticoat',
  value: 18315.14,
  productId: 'majestically tremendously which',
};

export const sampleWithFullData: IPriceOption = {
  id: 'f0795ec0-b173-439c-b673-e8afa2556723',
  name: 'how pantyhose defenseless',
  value: 21799.83,
  productId: 'better',
};

export const sampleWithNewData: NewPriceOption = {
  name: 'advanced soft sternly',
  value: 17474.53,
  productId: 'sunbathe',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
