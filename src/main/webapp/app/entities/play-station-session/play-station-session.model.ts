import dayjs from 'dayjs/esm';

export interface IPlayStationSession {
  id: number;
  active?: boolean | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  invoiceId?: string | null;
}

export type NewPlayStationSession = Omit<IPlayStationSession, 'id'> & { id: null };
