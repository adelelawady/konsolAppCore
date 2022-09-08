import { IStoreItem } from 'app/entities/store-item/store-item.model';

export interface IStore {
  id: string;
  name?: string | null;
  items?: Pick<IStoreItem, 'id'>[] | null;
}

export type NewStore = Omit<IStore, 'id'> & { id: null };
