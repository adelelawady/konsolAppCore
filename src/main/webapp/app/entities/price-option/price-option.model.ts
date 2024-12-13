export interface IPriceOption {
  id: string;
  name?: string | null;
  value?: number | null;
  productId?: string | null;
}

export type NewPriceOption = Omit<IPriceOption, 'id'> & { id: null };
