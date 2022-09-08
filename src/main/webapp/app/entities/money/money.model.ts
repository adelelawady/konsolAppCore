import { IBank } from 'app/entities/bank/bank.model';
import { IItem } from 'app/entities/item/item.model';
import { IAccountUser } from 'app/entities/account-user/account-user.model';
import { MoneyKind } from 'app/entities/enumerations/money-kind.model';

export interface IMoney {
  pk?: string | null;
  id: string;
  kind?: MoneyKind | null;
  moneyIn?: number | null;
  moneyOut?: number | null;
  bank?: Pick<IBank, 'id'> | null;
  item?: Pick<IItem, 'id'> | null;
  account?: Pick<IAccountUser, 'id'> | null;
}

export type NewMoney = Omit<IMoney, 'id'> & { id: null };
