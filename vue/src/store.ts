import Vue from 'vue';
import Vuex, { ActionContext } from 'vuex';
import { DrawerItem, SelectItem, TableMemberEntity } from '@/common/types';
import { getApi } from '@/utils/apis';

Vue.use(Vuex);

const moduleUser = {
  state: {
    user: null,
    logoutTimer: new Date().getTime() + 7200 * 1000,
  },
  getters: {
    user: (state: any) => {
      return {
        id: state.user.id,
        name: state.user.name,
        timeout: state.user.timeout,
        authority: state.user.authority,
      };
    },
  },
  mutations: {
    setUser(state: any, data: TableMemberEntity) {
      state.user = data;
    },
    resetTimer(state: any) {
      if (state.user && state.user.timeout) {
        state.logoutTimer = new Date().getTime() + state.user.timeout * 1000;
      } else {
        state.logoutTimer = new Date().getTime() + 7200 * 1000;
      }
    },
  },
  actions: {
    async setUser({ commit }: ActionContext<any, any>) {
      const response = await getApi<TableMemberEntity>(`auth/me`);
      commit('setUser', response.data);
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
    clearUser({ state }: ActionContext<any, any>) {
      state.user = null;
      state.logoutTimer = new Date().getTime() + 7200 * 1000;
    },
  },
};

const moduleDrawer = {
  state: {
    drawers: null,
  },
  mutations: {
    setDrawers(state: any, data: DrawerItem[]) {
      state.drawers = data;
    },
  },
  actions: {
    async setDrawers({ commit }: ActionContext<any, any>) {
      const response = await getApi<DrawerItem[]>('menus/drawer');
      commit('setDrawers', response.data);
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
    clearDrawer({ state }: ActionContext<any, any>) {
      state.drawers = null;
      state.menus = null;
    },
  },
};

const moduleCache = {
  state: {
    members: null,
  },
  mutations: {
    setMemberCodes(state: any, data: SelectItem[]) {
      state.members = data;
    },
  },
  actions: {
    async setMemberCodes({ commit }: ActionContext<any, any>) {
      const response = await getApi<SelectItem[]>('members/lists/codes');
      commit('setMemberCodes', response.data);
    },
    async getMemberCodes({ state, dispatch }: ActionContext<any, any>) {
      if (!state.members) {
        await dispatch('setMemberCodes');
      }
      return state.members;
    },
    clearCache({ state }: ActionContext<any, any>) {
      state.members = null;
    },
  },
};

export default new Vuex.Store({
  modules: {
    user: moduleUser,
    drawer: moduleDrawer,
    cache: moduleCache,
  },
});
