import Vue from "vue";
import Vuex, { ActionContext } from "vuex";
import createPersistedState from "vuex-persistedstate";
import { DrawerItem, SelectItem, TableMemberEntity } from "@/common/types";
import { axiosInstance, getApi, postApi } from "@/utils/apis";
/* eslint-disable camelcase */
import jwt_decode from "jwt-decode";
/* eslint-enable camelcase */
import axios from "axios";
import envs from "@/constants/envs";
import router from "@/router";
import { defaultUser } from "@/common/values";

Vue.use(Vuex);

/* eslint-disable @typescript-eslint/no-explicit-any */
const user = {
  state: {
    user: defaultUser(),
    accessToken: null,
    refreshToken: null,
  },
  getters: {
    user: (state: any) => {
      return {
        id: state.user.id,
        name: state.user.name,
        authority: state.user.authority,
        theme: state.user.theme,
      };
    },
    loggedIn: (state: any): boolean => {
      return !!state.user.id && !!state.accessToken && !!state.refreshToken;
    },
    theme: (state: any): string => {
      return state.user.theme || "light";
    },
    accessToken: (state: any): string | null => {
      return state.accessToken;
    },
    refreshToken: (state: any): string | null => {
      return state.refreshToken;
    },
  },
  mutations: {
    setUser(state: any, user: TableMemberEntity): void {
      state.user = user;
    },
    setTheme(state: any, theme: string): void {
      state.user.theme = theme;
    },
    setAccessToken(state: any, accessToken: string): void {
      const jwt = jwt_decode<{
        exp: number;
        userPk: string;
        userVO: string;
      }>(accessToken);
      state.user = JSON.parse(jwt.userVO);
      state.accessToken = accessToken;
    },
    setRefreshToken(state: any, refreshToken: string): void {
      state.refreshToken = refreshToken;
    },
  },
  actions: {
    toggleTheme: async function ({
      commit,
      getters,
    }: ActionContext<any, any>): Promise<void> {
      let theme;
      if (getters.theme === "dark") {
        theme = "light";
      } else {
        theme = "dark";
      }
      await postApi<{
        theme: string;
      }>(
        "members/mine/changeTheme/",
        {
          theme: theme,
        },
        false,
      );
      commit("setTheme", theme);
    },
    clearUser({ commit }: ActionContext<any, any>): void {
      commit("setUser", defaultUser());
    },
    saveToken(
      { commit }: ActionContext<any, any>,
      payload: { accessToken: string; refreshToken: string },
    ): void {
      commit("setAccessToken", payload.accessToken);
      commit("setRefreshToken", payload.refreshToken);
    },
    async reissueAccessToken({
      commit,
      getters,
    }: ActionContext<any, any>): Promise<void> {
      const response = await axios
        .create({
          baseURL: envs.API_HOST,
          headers: {
            contentType: "application/json",
            Authorization: getters.accessToken,
            AuthorizationR: getters.refreshToken,
          },
        })
        .get("api/auth/refreshToken");
      await commit("setAccessToken", response?.data?.data);
    },
  },
};

const drawer = {
  state: {
    drawers: null,
    selected: null,
  },
  getters: {
    drawers: (state: any): DrawerItem[] => {
      return state.drawers || [];
    },
    selected: (state: any): number | null => {
      return state.selected || null;
    },
  },
  mutations: {
    setDrawers(state: any, drawers: DrawerItem[]): void {
      state.drawers = drawers;
    },
    setSelected(state: any, selected: number): void {
      state.selected = selected;
    },
  },
  actions: {
    async initDrawers({ commit }: ActionContext<any, any>): Promise<void> {
      const response = await getApi<DrawerItem[]>("menus/drawer");
      commit("setDrawers", response?.data);
    },
    clearDrawer({ commit }: ActionContext<any, any>): void {
      commit("setDrawers", null);
    },
    setMenuSelected(
      { commit }: ActionContext<any, any>,
      selected: number,
    ): void {
      commit("setSelected", selected);
    },
    clearMenuSelected({ commit }: ActionContext<any, any>): void {
      commit("setSelected", null);
    },
  },
};

const codes = {
  state: {
    memberCodes: null,
  },
  getters: {
    memberCodes: (state: any): SelectItem[] => {
      return state.memberCodes || [];
    },
  },
  mutations: {
    setMemberCodes(state: any, memberCodes: SelectItem[]): void {
      state.memberCodes = memberCodes;
    },
  },
  actions: {
    async initMemberCodes({ commit }: ActionContext<any, any>): Promise<void> {
      const response = await getApi<SelectItem[]>("members/codes");
      commit("setMemberCodes", response?.data);
    },
    clearCache({ commit }: ActionContext<any, any>): void {
      commit("setMemberCodes", null);
    },
  },
};
const command = {
  state: {},
  mutations: {},
  actions: {
    async needLogin(): Promise<void> {
      if (router.currentRoute.path !== "/login") {
        await router.push("/login?login=need");
      }
    },
    async logout(): Promise<void> {
      try {
        axiosInstance.delete(`${envs.API_HOST}api/auth/logout`).then();
      } catch (e) {
        console.error(e);
      }
      await router.push("/login").then();
    },
  },
};
/* eslint-enable @typescript-eslint/no-explicit-any */
export default new Vuex.Store({
  strict: true,
  modules: {
    user,
    drawer,
    codes,
    command,
  },
  plugins: [createPersistedState()],
});
