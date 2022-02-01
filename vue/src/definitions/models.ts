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
  loginId: string;
  name: string;
  expired: DateTime;
  available: boolean;
  role: Role;
}

export interface App extends IdCreatedUpdated {
  name: string;
  platform: string;
  description: string | null;
  available: boolean;
}

export interface AppVersion extends IdCreatedUpdated {
  app: App;
  version: string;
  forceUpdate: boolean;
  releaseNote: string;
  available: boolean;
}

export interface AppMap {
  id?: number;
  app: App;
}

export interface Faq extends IdCreatedUpdated {
  title: string;
  maps: AppMap[];
  tags: string[];
  viewCount: number;
  displayOrder: number | null;
  answer: string | null;
  available: boolean;
}

export interface Notice extends IdCreatedUpdated {
  title: string;
  maps: AppMap[];
  displayOrder: number | null;
  content: string;
  viewCount: number;
  available: boolean;
  displayStartDate: DateTime;
  displayEndDate: DateTime;
}

export interface Popup extends IdCreatedUpdated {
  title: string;
  maps: AppMap[];
  displayOrder: number | null;
  content: string;
  availableFlag: boolean;
  width: number;
  height: number;
  locationX: number;
  locationY: number;
  displayOption: number;
  displayStartDate: DateTime;
  displayEndDate: DateTime;
}

export interface TermsAdminMap {
  id?: number;
  agreeFlag: boolean;
}

export interface Terms extends IdCreatedUpdated {
  name: string;
  type: string;
  content: string;
  forceAgreeFlag: boolean;
  revisionDate: DateTime;
  service: string;
  availableFlag: boolean;
  termsAdminMaps: TermsAdminMap[];
}
