import { IItem } from 'app/entities/item/item.model';

export interface IItemUnit {
  id: string;
  name?: string | null;
  pieces?: number | null;
  price?: number | null;
  items?: Pick<IItem, 'id'>[] | null;
}

export type NewItemUnit = Omit<IItemUnit, 'id'> & { id: null };
