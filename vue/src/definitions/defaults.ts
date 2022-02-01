import {
  Admin,
  AdminConfig,
  App,
  AppVersion,
  Code,
  Faq,
  Menu,
  Notice,
  Popup,
  Role,
  RoleMenuMap,
  Terms,
  TermsAdminMap,
} from "@/definitions/models";
import dayjs from "dayjs";
import {
  APP_TYPE,
  MENU_TYPE,
  ROLE_AUTHORITY_TYPE,
} from "@/definitions/selections";
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

export function defaultApp(): App {
  return {
    name: "",
    platform: APP_TYPE.APP_ANDROID,
    description: null,
    available: false,
  };
}

export function defaultAppVersion(): AppVersion {
  return {
    app: defaultApp(),
    version: "",
    forceUpdate: false,
    releaseNote: "",
    available: false,
  };
}

export function defaultNotice(): Notice {
  return {
    title: "",
    maps: [],
    displayOrder: null,
    content: "",
    viewCount: 0,
    available: false,
    displayEndDate: null,
    displayStartDate: null,
  };
}

export function defaultFaq(): Faq {
  return {
    title: "",
    tags: [],
    maps: [],
    viewCount: 0,
    displayOrder: null,
    answer: null,
    available: false,
  };
}

export function defaultTerms(): Terms {
  return {
    name: "",
    type: "",
    content: "",
    forceAgreeFlag: false,
    revisionDate: null,
    service: "",
    availableFlag: false,
    termsAdminMaps: [],
  };
}

export function defaultTermsAdminMap(): TermsAdminMap {
  return {
    agreeFlag: false,
  };
}

export function defaultPopup(): Popup {
  return {
    title: "",
    maps: [],
    displayOrder: null,
    content: "",
    availableFlag: false,
    width: 0,
    height: 0,
    locationX: 0,
    locationY: 0,
    displayOption: 0,
    displayStartDate: null,
    displayEndDate: null,
  };
}
