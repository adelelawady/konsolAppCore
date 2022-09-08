import { IInvoice } from 'app/entities/invoice/invoice.model';

export interface IInvoiceItem {
  pk?: string | null;
  id: string;
  unit?: string | null;
  unitPieces?: number | null;
  userQty?: number | null;
  unitQtyIn?: number | null;
  unitQtyOut?: number | null;
  unitCost?: number | null;
  unitPrice?: number | null;
  discountPer?: number | null;
  discount?: number | null;
  totalCost?: number | null;
  totalPrice?: number | null;
  qtyIn?: number | null;
  qtyOut?: number | null;
  cost?: number | null;
  price?: number | null;
  netCost?: number | null;
  netPrice?: number | null;
  invoices?: Pick<IInvoice, 'id'>[] | null;
}

export type NewInvoiceItem = Omit<IInvoiceItem, 'id'> & { id: null };
