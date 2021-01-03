import dayjs from "dayjs";

export type DateType = dayjs.ConfigType;

export interface SelectItem {
  value: string;
  text: string;
}
/* eslint-disable @typescript-eslint/no-explicit-any */
export interface DataTableHeader {
  text: string;
  value: string;
  align?: "start" | "center" | "end";
  sortable?: boolean | null;
  filterable?: boolean | null;
  divider?: boolean | null;
  class?: string | string[];
  width?: string | number;
  filter?: (value: any, search: string, item: any) => boolean;
  sort?: (a: any, b: any) => number;
  // 아래는 filter 를 위한 property 추가
  filterType?: "input" | "select" | "switch";
  filterSelectItem?: SelectItem[] | null;
  filterDefaultValue?: string | null;
}
/* eslint-enable @typescript-eslint/no-explicit-any */

export interface Pagination {
  page: number;
  sortBy: string[];
  sortDesc: boolean[];
  itemsPerPage: number; // -1 for All
}

export interface TableMemberEntity {
  id?: string | null;
  password?: string | null;
  name?: string | null;
  loginFailCnt?: number | null;
  expired?: Date | string | null;
  available?: boolean | null;
  theme?: string | null;
  authority?: number | null;
  token?: string | null;
  created?: Date | string | null;
  createdBy?: string | null;
  updated?: Date | string | null;
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
  created?: Date | string | null;
  createdBy?: string | null;
  updated?: Date | string | null;
  updatedBy?: string | null;
}

export interface DrawerItem extends TableMenuEntity {
  children?: DrawerItem[];
}

export interface TableCodeGroupEntity {
  codeGroup?: string | null;
  name?: string | null;
  created?: Date | string | null;
  createdBy?: string | null;
  updated?: Date | string | null;
  updatedBy?: string | null;
}

export interface TableCodeEntity {
  codeGroup?: string | null;
  code?: string | null;
  name?: string | null;
  available?: boolean | null;
  displayOrder?: number | null;
  authority?: number | null;
  created?: Date | string | null;
  createdBy?: string | null;
  updated?: Date | string | null;
  updatedBy?: string | null;
}
export interface TableMemberMenuEntity {
  authority?: number | null;
  id?: number | null;
  name?: string | null;
  type?: string | null;
  parentId?: number | null;
  displayOrder?: number | null;
  icon?: string | null;
  url?: string | null;
  createdBy?: string | null;
  created?: Date | string | null;
  updatedBy?: string | null;
  updated?: Date | string | null;
}
