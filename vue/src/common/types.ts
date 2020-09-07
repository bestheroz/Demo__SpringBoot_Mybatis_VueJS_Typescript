export interface SelectItem {
  value: string;
  text: string;
}

export interface DataTableHeader {
  text: string;
  value: string;
  align?: 'start' | 'center' | 'end';
  sortable?: boolean | null;
  filterable?: boolean | null;
  divider?: boolean | null;
  class?: string | string[];
  width?: string | number;
  filter?: (value: any, search: string, item: any) => boolean;
  sort?: (a: any, b: any) => number;
  // 아래는 filter 를 위한 property 추가
  filterType?: 'input' | 'select' | 'switch';
  filterSelectItem?: SelectItem[] | null;
  filterDefaultValue?: string | null;
}

// export interface Pagination {
//   page: number;
//   sortBy: string[];
//   sortDesc: boolean[];
//   itemsPerPage: number; // -1 for All
// }

export interface DrawerItem {
  id: number;
  title: string;
  icon?: string | null;
  to?: string | null;
  type?: string | null;
  group?: string | null;
  checked?: boolean | null;
  depth?: number | null;
  children?: DrawerItem[];
}

export interface TableMemberEntity {
  id?: string | null;
  password?: string | null;
  name?: string | null;
  loginFailCnt?: number | null;
  expired?: Date | null;
  available?: boolean | null;
  theme?: string | null;
  authority?: number | null;
  timeout?: number | null;
  token?: string | null;
  created?: Date | null;
  createdBy?: string | null;
  updated?: Date | null;
  updatedBy?: string | null;
}

export interface TableMenuEntity {
  id?: number | null;
  name?: string | null;
  type?: string | null;
  parentId?: number | null;
  authority?: number | null;
  displayOrder?: number | null;
  icon?: string | null;
  url?: string | null;
  created?: Date | null;
  createdBy?: string | null;
  updated?: Date | null;
  updatedBy?: string | null;
}

// export interface TableMenuAuthorityVO {
//   authority?: number | null;
//   menuIdList?: string | null;
//   created?: Date | null;
//   createdBy?: string | null;
//   updated?: Date | null;
//   updatedBy?: string | null;
// }

export interface TableCodeGroupEntity {
  codeGroup?: string | null;
  name?: string | null;
  created?: Date | null;
  createdBy?: string | null;
  updated?: Date | null;
  updatedBy?: string | null;
}

export interface TableCodeEntity {
  codeGroup?: string | null;
  code?: string | null;
  name?: string | null;
  available?: boolean | null;
  displayOrder?: number | null;
  authority?: number | null;
  created?: Date | null;
  createdBy?: string | null;
  updated?: Date | null;
  updatedBy?: string | null;
}
