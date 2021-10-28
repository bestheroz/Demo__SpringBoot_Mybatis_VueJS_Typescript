import { DateTime } from "@/definitions/types";

export interface IdCreatedUpdated {
  id?: number;
  createdBy?: number;
  created?: DateTime;
  updatedBy?: number;
  updated?: DateTime;
}

export interface AdminConfig {
  globalTheme: "light" | "dark";
  toolbarTheme: "global" | "light" | "dark";
  menuTheme: "global" | "light" | "dark";
  toolbarDetached: boolean;
  contentBoxed: boolean;
  primaryColor: string;
}

export interface Menu extends IdCreatedUpdated {
  name: string;
  type: string;
  icon: string | null;
  url: string | null;
  children: Menu[];
}

export interface Code extends IdCreatedUpdated {
  type: string;
  value: string;
  text: string;
  available: boolean;
  displayOrder: number | null;
}

export interface RoleMenuMap {
  id?: number;
  menu: Menu;
  authoritiesJson: string[];
  children: RoleMenuMap[];
}
export interface Role extends IdCreatedUpdated {
  name: string;
  available: boolean;
  children: Role[];
  maps: RoleMenuMap[];
}

export interface Admin extends IdCreatedUpdated {
  adminId: string;
  name: string;
  expired: DateTime;
  available: boolean;
  role: Role;
}
