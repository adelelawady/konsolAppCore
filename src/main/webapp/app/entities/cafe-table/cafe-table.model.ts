export interface ICafeTable {
  id: string;
  pk?: number | null;
  name?: string | null;
  index?: number | null;
  active?: boolean | null;
}

export type NewCafeTable = Omit<ICafeTable, 'id'> & { id: null };
