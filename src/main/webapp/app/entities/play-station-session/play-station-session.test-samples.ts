import dayjs from 'dayjs/esm';

import { IPlayStationSession, NewPlayStationSession } from './play-station-session.model';

export const sampleWithRequiredData: IPlayStationSession = {
  id: 31608,
  active: true,
  startTime: dayjs('2024-12-13T11:46'),
  invoiceId: 'what',
};

export const sampleWithPartialData: IPlayStationSession = {
  id: 15489,
  active: false,
  startTime: dayjs('2024-12-13T02:28'),
  endTime: dayjs('2024-12-13T16:48'),
  invoiceId: 'questionably against',
};

export const sampleWithFullData: IPlayStationSession = {
  id: 27041,
  active: false,
  startTime: dayjs('2024-12-13T01:16'),
  endTime: dayjs('2024-12-13T14:36'),
  invoiceId: 'design',
};

export const sampleWithNewData: NewPlayStationSession = {
  active: false,
  startTime: dayjs('2024-12-12T23:42'),
  invoiceId: 'motivate firsthand cinema',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
