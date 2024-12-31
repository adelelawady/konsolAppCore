import dayjs from 'dayjs/esm';

export interface ISheft {
  id: string;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  active?: boolean | null;
  assignedEmployee?: string | null;
  duration?: string | null;
  totalprice?: number | null;
  totalCost?: number | null;
  netPrice?: number | null;
  netCost?: number | null;
  netUserPrice?: number | null;
  totalItemsOut?: number | null;
  discount?: number | null;
  invoicesAdditions?: number | null;
  additions?: number | null;
  additionsNotes?: number | null;
  invoicesExpenses?: number | null;
  sheftExpenses?: number | null;
  totalinvoices?: number | null;
  totaldeletedItems?: number | null;
  totaldeletedItemsPrice?: number | null;
  notes?: string | null;
}

export type NewSheft = Omit<ISheft, 'id'> & { id: null };
