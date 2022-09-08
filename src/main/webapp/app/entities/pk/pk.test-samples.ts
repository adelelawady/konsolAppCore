import { PkKind } from 'app/entities/enumerations/pk-kind.model';

import { IPk, NewPk } from './pk.model';

export const sampleWithRequiredData: IPk = {
  id: '8ab3f040-5c17-4df2-8b2f-8925cd635bce',
};

export const sampleWithPartialData: IPk = {
  id: 'cf6edef9-7257-49c8-bbd8-0858604738cf',
  kind: PkKind['INVOICE'],
  value: 36374,
};

export const sampleWithFullData: IPk = {
  id: '5621b331-9fba-4825-8d54-2551ceead69c',
  kind: PkKind['Money'],
  value: 12329,
};

export const sampleWithNewData: NewPk = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
