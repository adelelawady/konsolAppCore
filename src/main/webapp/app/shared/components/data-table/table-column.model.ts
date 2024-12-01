export interface TableColumn {
  field: string;
  header: string;
  type?: 'text' | 'number' | 'currency' | 'date' | 'actions';
  width?: string;
  sortable?: boolean;
  filterable?: boolean;
  editable?: boolean;
  format?: string | ((value: any) => string);
}
