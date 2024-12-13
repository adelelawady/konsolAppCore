import { IProduct, NewProduct } from './product.model';

export const sampleWithRequiredData: IProduct = {
  id: 'a5d96ec5-b1ee-437a-91ae-10a7691f7d4a',
  productId: 'at',
  defaultMainPrice: 25774.96,
};

export const sampleWithPartialData: IProduct = {
  id: '1b641ff9-30e8-4f18-b687-7a2227714b09',
  productId: 'delightfully',
  defaultMainPrice: 11942,
};

export const sampleWithFullData: IProduct = {
  id: '0c1dffb5-6509-491e-a1f1-26f2e8812afc',
  productId: 'excitedly oddly',
  defaultMainPrice: 22685.68,
};

export const sampleWithNewData: NewProduct = {
  productId: 'yearningly gosh',
  defaultMainPrice: 16250.65,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
