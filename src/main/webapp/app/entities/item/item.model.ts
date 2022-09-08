import { IItemUnit } from 'app/entities/item-unit/item-unit.model';

export interface IItem {
  id: string;
  name?: string | null;
  barcode?: string | null;
  price1?: string | null;
  price2?: string | null;
  price3?: string | null;
  category?: string | null;
  qty?: number | null;
  cost?: number | null;
  index?: number | null;
  itemUnits?: Pick<IItemUnit, 'id'>[] | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
