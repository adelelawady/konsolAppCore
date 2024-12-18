export interface IPlaystationContainer {
  id: string;
  name?: string | null;
  category?: string | null;
  defaultIcon?: string | null;
  hasTimeManagement?: boolean | null;
  showType?: boolean | null;
  showTime?: boolean | null;
  canMoveDevice?: boolean | null;
  canHaveMultiTimeManagement?: boolean | null;
  acceptedOrderCategories?: string | null;
  orderSelectedPriceCategory?: string | null;
}

export type NewPlaystationContainer = Omit<IPlaystationContainer, 'id'> & { id: null };
