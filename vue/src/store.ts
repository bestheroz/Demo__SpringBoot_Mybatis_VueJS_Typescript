import Vue from 'vue';
import Vuex, { ActionContext } from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import { DrawerItem, SelectItem, TableMemberEntity } from '@/common/types';
import { getApi } from '@/utils/apis';

Vue.use(Vuex);

const user = {
  state: {
    user: null,
    logoutTimer: new Date().getTime() + 7200 * 1000,
  },
  getters: {
    user: (state: any) => {
      return {
        id: state.user?.id,
        name: state.user?.name,
        timeout: state.user?.timeout,
        authority: state.user?.authority,
        theme: state.user?.theme,
      };
    },
  },
  mutations: {
    setUser(state: any, user: TableMemberEntity) {
      state.user = user;
    },
    resetTimer(state: any) {
      state.logoutTimer =
        new Date().getTime() + (state.user?.timeout || 7200) * 1000;
    },
  },
  actions: {
    async setUser({ commit }: ActionContext<any, any>) {
      const response = await getApi<TableMemberEntity>(`auth/me`);
      commit('setUser', response?.data);
      commit('resetTimer');
    },
    async getUser({
      state,
      dispatch,
      getters,
    }: ActionContext<any, any>): Promise<TableMemberEntity> {
      if (!state.user) {
        await dispatch('setUser');
      }
      return getters.user;
    },
    async resetTimer({ commit }: ActionContext<any, any>) {
      commit('resetTimer');
    },
    clearUser({ commit }: ActionContext<any, any>) {
      commit('setUser', null);
      commit('resetTimer');
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
      const response = await getApi<DrawerItem[]>('menus/drawer');
      commit('setDrawers', response?.data);
    },
    async getDrawers({
      state,
      dispatch,
    }: ActionContext<any, any>): Promise<DrawerItem[]> {
      if (!state.drawers) {
        await dispatch('setDrawers');
      }
      return state.drawers;
    },
    clearDrawer({ commit }: ActionContext<any, any>) {
      commit('setDrawers', null);
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
      const response = await getApi<SelectItem[]>('members/codes');
      commit('setMemberCodes', response?.data);
    },
    async getMemberCodes({ state, dispatch }: ActionContext<any, any>) {
      if (!state.memberCodes) {
        await dispatch('setMemberCodes');
      }
      return state.memberCodes;
    },
    clearCache({ commit }: ActionContext<any, any>) {
      commit('setMemberCodes', null);
    },
  },
};

export default new Vuex.Store({
  strict: true,
  modules: {
    user,
    drawer,
    cache,
  },
  plugins: [createPersistedState()],
});
