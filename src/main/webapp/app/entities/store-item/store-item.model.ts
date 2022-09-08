import { IItem } from 'app/entities/item/item.model';
import { IStore } from 'app/entities/store/store.model';

export interface IStoreItem {
  id: string;
  qty?: number | null;
  item?: Pick<IItem, 'id'> | null;
  storeIds?: Pick<IStore, 'id'>[] | null;
}

export type NewStoreItem = Omit<IStoreItem, 'id'> & { id: null };
