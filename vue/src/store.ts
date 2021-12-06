import Vue from "vue";
import Vuex, { ActionContext } from "vuex";
import createPersistedState from "vuex-persistedstate";
import { Drawer, SelectItem } from "@/definitions/types";
// eslint-disable-next-line camelcase
import jwt_decode from "jwt-decode";
import type { AdminConfig, Role, RoleMenuMap } from "@/definitions/models";
import Vuetify from "./plugins/vuetify";
import { ROLE_AUTHORITY_TYPE } from "@/definitions/selections";
import config from "./configs";
import {
  getAccessToken,
  getAdminCodes,
  getCurrentAuthority,
  getDrawersFromRoleMenuMaps,
  getFlatRoleMenuMaps,
  signOut,
  uploadConfig,
} from "@/utils/commands";
import { defaultAdminConfig } from "@/definitions/defaults";
import { getApi } from "@/utils/apis";

Vue.use(Vuex);

/* eslint-disable @typescript-eslint/no-explicit-any */
const admin = {
  state: {
    id: 0,
    loginId: "",
    name: "",
    roleId: 0,
    accessToken: null,
    refreshToken: null,
  },
  getters: {
    roleId: (state: any) => {
      return state.roleId;
    },
    admin: (state: any) => {
      return {
        id: state.id,
        loginId: state.loginId,
        name: state.name,
        roleId: state.roleId,
      };
    },
    signedIn: (state: any): boolean => {
      return !!state.id && !!state.accessToken && !!state.refreshToken;
    },
    accessToken: (state: any): string | null => {
      return state.accessToken;
    },
    refreshToken: (state: any): string | null => {
      return state.refreshToken;
    },
  },
  mutations: {
    setAccessToken(state: any, accessToken: string): void {
      try {
        const jwt = jwt_decode<{
          exp: number;
          id: number;
          loginId: string;
          name: string;
          roleId: string;
        }>(accessToken);
        state.id = jwt.id;
        state.loginId = jwt.loginId;
        state.name = jwt.name;
        state.roleId = jwt.roleId;
        state.accessToken = accessToken;
      } catch (e: unknown) {
        signOut().then();
      }
    },
    setRefreshToken(state: any, refreshToken: string): void {
      state.refreshToken = refreshToken;
    },
    setAdmin: (
      state: any,
      admin: {
        id: number;
        loginId: string;
        name: string;
        roleId: number;
      },
    ) => {
      state.id = admin.id;
      state.loginId = admin.loginId;
      state.name = admin.name;
      state.roleId = admin.roleId;
      state.accessToken = null;
      state.refreshToken = null;
    },
  },
  actions: {
    saveToken(
      { commit }: ActionContext<any, any>,
      payload: { accessToken: string; refreshToken: string },
    ): void {
      commit("setAccessToken", payload.accessToken);
      commit("setRefreshToken", payload.refreshToken);
    },
    async reIssueAccessToken({
      commit,
      getters,
    }: ActionContext<any, any>): Promise<void> {
      await commit(
        "setAccessToken",
        await getAccessToken(getters.accessToken, getters.refreshToken),
      );
    },
  },
};

const config1 = {
  state: {
    globalTheme: config.theme.globalTheme as "light" | "dark",
    toolbarTheme: config.theme.toolbarTheme as "global" | "light" | "dark",
    menuTheme: config.theme.menuTheme as "global" | "light" | "dark",
    toolbarDetached: config.theme.isToolbarDetached,
    contentBoxed: config.theme.isContentBoxed,
    primaryColor: config.theme.light.primary,
  },
  getters: {
    config: (state: AdminConfig): AdminConfig => {
      return {
        ...state,
      };
    },
    globalTheme: (state: AdminConfig): "light" | "dark" => {
      Vuetify.framework.theme.dark = state.globalTheme === "dark";
      return state.globalTheme;
    },
    menuTheme: (state: AdminConfig): "global" | "light" | "dark" => {
      return state.menuTheme;
    },
    toolbarTheme: (state: AdminConfig): "global" | "light" | "dark" => {
      return state.toolbarTheme;
    },
    isToolbarDetached: (state: AdminConfig): boolean => {
      return state.toolbarDetached;
    },
    isContentBoxed: (state: AdminConfig): boolean => {
      return state.contentBoxed;
    },
    primaryColor: (state: AdminConfig): string => {
      return state.primaryColor;
    },
  },
  mutations: {
    setGlobalTheme: (state: AdminConfig, globalTheme: "light" | "dark") => {
      state.globalTheme = globalTheme;
    },
    setContentBoxed: (state: AdminConfig, isContentBoxed: boolean) => {
      state.contentBoxed = isContentBoxed;
    },
    setMenuTheme: (state: any, menuTheme: "global" | "light" | "dark") => {
      state.menuTheme = menuTheme;
    },
    setToolbarTheme: (
      state: AdminConfig,
      toolbarTheme: "global" | "light" | "dark",
    ) => {
      state.toolbarTheme = toolbarTheme;
    },
    setToolbarDetached: (state: AdminConfig, isToolbarDetached: boolean) => {
      state.toolbarDetached = isToolbarDetached;
    },
    setPrimaryColor: (state: AdminConfig, primaryColor: string) => {
      state.primaryColor = primaryColor;
    },
    setConfig: (state: AdminConfig, config: AdminConfig) => {
      state.globalTheme = config.globalTheme;
      state.contentBoxed = config.contentBoxed;
      state.menuTheme = config.menuTheme;
      state.toolbarTheme = config.toolbarTheme;
      state.toolbarDetached = config.toolbarDetached;
      state.primaryColor = config.primaryColor;
    },
  },
  actions: {
    setGlobalTheme: async (
      { commit, getters }: ActionContext<any, any>,
      globalTheme: "light" | "dark",
    ) => {
      commit("setGlobalTheme", globalTheme);
      uploadConfig(getters.config);
    },
    setContentBoxed: async (
      { commit, getters }: ActionContext<any, any>,
      isContentBoxed: boolean,
    ) => {
      commit("setContentBoxed", isContentBoxed);
      uploadConfig(getters.config);
    },
    setMenuTheme: async (
      { commit, getters }: ActionContext<any, any>,
      menuTheme: "global" | "light" | "dark",
    ) => {
      commit("setMenuTheme", menuTheme);
      uploadConfig(getters.config);
    },
    setToolbarTheme: async (
      { commit, getters }: ActionContext<any, any>,
      toolbarTheme: "global" | "light" | "dark",
    ) => {
      commit("setToolbarTheme", toolbarTheme);
      uploadConfig(getters.config);
    },
    setToolbarDetached: async (
      { commit, getters }: ActionContext<any, any>,
      isToolbarDetached: boolean,
    ) => {
      commit("setToolbarDetached", isToolbarDetached);
      uploadConfig(getters.config);
    },
    setPrimaryColor: async (
      { commit, getters }: ActionContext<any, any>,
      primaryColor: string,
    ) => {
      commit("setPrimaryColor", primaryColor);
      uploadConfig(getters.config);
    },
    reloadConfig: ({ commit, getters }: ActionContext<any, any>) => {
      commit("setConfig", defaultAdminConfig());
      if (getters.signedIn) {
        uploadConfig(getters.config);
      }
    },
  },
};
const authority = {
  state: {
    superAdminFlag: false,
    drawers: [],
    flatAuthorities: [],
    currentAuthority: null,
    writeAuthority: false,
    deleteAuthority: false,
    excelAuthority: false,
  },
  getters: {
    drawers: (state: any): Drawer[] => {
      return state.drawers;
    },
    currentAuthority: (state: any): RoleMenuMap => {
      return state.currentAuthority;
    },
    flatAuthorities: (state: any): RoleMenuMap[] => {
      return state.flatAuthorities;
    },
    isSuperAdmin: (state: any): boolean => {
      return state.superAdminFlag;
    },
    writeAuthority: (state: any): boolean => {
      return state.writeAuthority || state.superAdminFlag;
    },
    deleteAuthority: (state: any): boolean => {
      return state.deleteAuthority || state.superAdminFlag;
    },
    excelAuthority: (state: any): boolean => {
      return state.excelAuthority || state.superAdminFlag;
    },
  },
  mutations: {
    setRole(state: any, role: Role): void {
      if (role) {
        state.superAdminFlag = role?.id === 1;
        state.drawers = getDrawersFromRoleMenuMaps(role.maps);
        state.flatAuthorities = getFlatRoleMenuMaps(role.maps);
      }
    },
    reloadCurrentAuthority(state: any, path: string): void {
      state.currentAuthority = getCurrentAuthority(path);
      const authoritiesJson = state.currentAuthority?.authoritiesJson || [];
      state.writeAuthority = authoritiesJson.includes(
        ROLE_AUTHORITY_TYPE.WRITE,
      );
      state.deleteAuthority = authoritiesJson.includes(
        ROLE_AUTHORITY_TYPE.DELETE,
      );
      state.excelAuthority = authoritiesJson.includes(
        ROLE_AUTHORITY_TYPE.EXCEL,
      );
    },
  },
  actions: {
    async reloadRole({ commit }: ActionContext<any, any>): Promise<void> {
      // eslint-disable-next-line no-undef
      const response = await getApi<Role>("mine/role");
      commit("setRole", response.data);
    },
  },
};
const codes = {
  state: {
    adminCodes: null,
  },
  getters: {
    adminCodes: (state: any): SelectItem<number>[] => {
      return state.adminCodes || [];
    },
  },
  mutations: {
    setAdminCodes(state: any, adminCodes: SelectItem<number>[]): void {
      state.adminCodes = adminCodes;
    },
  },
  actions: {
    async reloadAdminCodes({ commit }: ActionContext<any, any>): Promise<void> {
      commit("setAdminCodes", await getAdminCodes());
    },
  },
};
/* eslint-enable @typescript-eslint/no-explicit-any */
export default new Vuex.Store({
  strict: true,
  modules: {
    admin,
    config1,
    authority,
    codes,
  },
  plugins: [createPersistedState()],
});
