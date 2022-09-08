export interface IBank {
  id: string;
  name?: string | null;
}

export type NewBank = Omit<IBank, 'id'> & { id: null };
