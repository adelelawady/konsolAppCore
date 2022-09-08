import { AccountKind } from 'app/entities/enumerations/account-kind.model';

export interface IAccountUser {
  id: string;
  name?: string | null;
  kind?: AccountKind | null;
  balanceIn?: number | null;
  balanceOut?: number | null;
  phone?: string | null;
  address?: string | null;
  address2?: string | null;
}

export type NewAccountUser = Omit<IAccountUser, 'id'> & { id: null };
