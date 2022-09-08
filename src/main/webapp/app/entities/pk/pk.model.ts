import { PkKind } from 'app/entities/enumerations/pk-kind.model';

export interface IPk {
  id: string;
  kind?: PkKind | null;
  value?: number | null;
}

export type NewPk = Omit<IPk, 'id'> & { id: null };
