import {
  Admin,
  AdminConfig,
  Code,
  Menu,
  Role,
  RoleMenuMap,
} from "@/definitions/models";
import dayjs from "dayjs";
import { MENU_TYPE, ROLE_AUTHORITY_TYPE } from "@/definitions/selections";
import config from "../configs";

export function defaultSignedAdmin(): {
  id: number;
  loginId: string;
  name: string;
  roleId: number;
} {
  return {
    id: 0,
    loginId: "",
    name: "",
    roleId: 0,
  };
}

export function defaultAdminConfig(): AdminConfig {
  return {
    globalTheme: config.theme.globalTheme as "light" | "dark",
    toolbarTheme: config.theme.toolbarTheme as "global" | "light" | "dark",
    menuTheme: config.theme.menuTheme as "global" | "light" | "dark",
    toolbarDetached: config.theme.isToolbarDetached,
    contentBoxed: config.theme.isContentBoxed,
    primaryColor: config.theme.light.primary,
  };
}

export function defaultAdmin(): Admin {
  return {
    loginId: "",
    name: "",
    expired: dayjs().add(1, "years").endOf("day"),
    available: false,
    role: defaultRole(),
  };
}

export function defaultMenu(): Menu {
  return {
    name: "",
    type: MENU_TYPE.GROUP,
    icon: null,
    url: null,
    children: [],
  };
}

export function defaultCode(): Code {
  return {
    type: "",
    value: "",
    text: "",
    available: false,
    displayOrder: null,
  };
}

export function defaultRole(): Role {
  return {
    name: "",
    available: false,
    children: [],
    maps: [],
  };
}

export function defaultRoleMenuMap(): RoleMenuMap {
  return {
    menu: defaultMenu(),
    authoritiesJson: [ROLE_AUTHORITY_TYPE.VIEW],
    children: [],
  };
}
