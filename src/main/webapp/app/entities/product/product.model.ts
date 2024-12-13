export interface IProduct {
  id: string;
  productId?: string | null;
  defaultMainPrice?: number | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
