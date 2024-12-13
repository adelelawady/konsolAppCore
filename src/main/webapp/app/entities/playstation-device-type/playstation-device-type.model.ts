export interface IPlaystationDeviceType {
  id: string;
  name?: string | null;
  defaultMainPrice?: number | null;
  productId?: string | null;
}

export type NewPlaystationDeviceType = Omit<IPlaystationDeviceType, 'id'> & { id: null };
