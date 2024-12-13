export interface IPlaystationDevice {
  id: string;
  pk?: number | null;
  name?: string | null;
  index?: number | null;
  active?: boolean | null;
}

export type NewPlaystationDevice = Omit<IPlaystationDevice, 'id'> & { id: null };
