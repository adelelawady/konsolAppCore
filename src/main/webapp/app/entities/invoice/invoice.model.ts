import { IBank } from 'app/entities/bank/bank.model';
import { IItem } from 'app/entities/item/item.model';
import { IAccountUser } from 'app/entities/account-user/account-user.model';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceKind } from 'app/entities/enumerations/invoice-kind.model';

export interface IInvoice {
  pk?: string | null;
  id: string;
  kind?: InvoiceKind | null;
  totalCost?: number | null;
  totalPrice?: number | null;
  discountPer?: number | null;
  discount?: number | null;
  additions?: number | null;
  additionsType?: string | null;
  netCost?: number | null;
  netPrice?: number | null;
  expenses?: number | null;
  expensesType?: string | null;
  bank?: Pick<IBank, 'id'> | null;
  item?: Pick<IItem, 'id'> | null;
  account?: Pick<IAccountUser, 'id'> | null;
  invoiceItems?: Pick<IInvoiceItem, 'id'>[] | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
