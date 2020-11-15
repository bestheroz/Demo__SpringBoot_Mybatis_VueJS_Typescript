import Vue from "vue";
import Vuex, { ActionContext } from "vuex";
import createPersistedState from "vuex-persistedstate";
import { DrawerItem, SelectItem, TableMemberEntity } from "@/common/types";
import { getApi } from "@/utils/apis";

Vue.use(Vuex);

const user = {
  state: {
    user: null,
  },
  getters: {
    user: (state: any) => {
      return {
        id: state.user?.id,
        name: state.user?.name,
        authority: state.user?.authority,
        theme: state.user?.theme,
      };
    },
  },
  mutations: {
    setUser(state: any, user: TableMemberEntity) {
      state.user = user;
    },
  },
  actions: {
    async setUser({ commit }: ActionContext<any, any>) {
      const response = await getApi<TableMemberEntity>(`auth/me`);
      commit("setUser", response?.data);
    },
    async getUser({
      state,
      dispatch,
      getters,
    }: ActionContext<any, any>): Promise<TableMemberEntity> {
      if (!state.user) {
        await dispatch("setUser");
      }
      return getters.user;
    },
    clearUser({ commit }: ActionContext<any, any>) {
      commit("setUser", null);
    },
  },
};

const drawer = {
  state: {
    drawers: null,
  },
  mutations: {
    setDrawers(state: any, drawers: DrawerItem[]) {
      state.drawers = drawers;
    },
  },
  actions: {
    async setDrawers({ commit }: ActionContext<any, any>) {
      const response = await getApi<DrawerItem[]>("menus/drawer");
      commit("setDrawers", response?.data);
    },
    async getDrawers({
      state,
      dispatch,
    }: ActionContext<any, any>): Promise<DrawerItem[]> {
      if (!state.drawers) {
        await dispatch("setDrawers");
      }
      return state.drawers;
    },
    clearDrawer({ commit }: ActionContext<any, any>) {
      commit("setDrawers", null);
    },
  },
};

const cache = {
  state: {
    memberCodes: null,
  },
  mutations: {
    setMemberCodes(state: any, memberCodes: SelectItem[]) {
      state.memberCodes = memberCodes;
    },
  },
  actions: {
    async setMemberCodes({ commit }: ActionContext<any, any>) {
      const response = await getApi<SelectItem[]>("members/codes");
      commit("setMemberCodes", response?.data);
    },
    async getMemberCodes({ state, dispatch }: ActionContext<any, any>) {
      if (!state.memberCodes) {
        await dispatch("setMemberCodes");
      }
      return state.memberCodes;
    },
    clearCache({ commit }: ActionContext<any, any>) {
      commit("setMemberCodes", null);
    },
  },
};

const temp = {
  state: {
    finishTextEllipsis: false,
  },
  mutations: {
    setFinishTextEllipsis(state: any, isFinishTextEllipsis: boolean) {
      state.finishTextEllipsis = isFinishTextEllipsis;
    },
  },
  actions: {
    setFinishTextEllipsis(
      { commit }: ActionContext<any, any>,
      isFinishTextEllipsis: boolean,
    ) {
      commit("setFinishTextEllipsis", isFinishTextEllipsis);
    },
  },
};

export default new Vuex.Store({
  strict: true,
  modules: {
    user,
    drawer,
    cache,
    temp,
  },
  plugins: [createPersistedState()],
});
