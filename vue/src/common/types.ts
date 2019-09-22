export interface DataTableHeader {
  text: string;
  value: string;
  align?: 'start' | 'center' | 'end';
  sortable?: boolean;
  divider?: boolean;
  class?: string | string[];
  width?: string | number;
  filter?: boolean;
  sort?: number;
}

export interface SelectItem {
  code: string;
  codeNm: string | number;
}