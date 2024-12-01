export interface TableColumn {
  field: string;
  header: string;
  sortable?: boolean;
  type?: 'text' | 'number' | 'currency' | 'date' | 'actions';
  width?: string;
  format?: string | ((value: any) => string);
}
