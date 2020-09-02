import Vue from 'vue';
import Vuex, { ActionContext } from 'vuex';
import { DrawerItem, SelectItem, TableMemberEntity } from '@/common/types';
import { getApi, putApi } from '@/utils/apis';

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
        theme: state.user.theme,
      };
    },
  },
  mutations: {
    resetTimer(state: any) {
      if (state.user && state.user.timeout) {
        state.logoutTimer = new Date().getTime() + state.user.timeout * 1000;
      } else {
        state.logoutTimer = new Date().getTime() + 7200 * 1000;
      }
    },
  },
  actions: {
    async setUser({ commit, state }: ActionContext<any, any>) {
      const response = await getApi<TableMemberEntity>(`auth/me`);
      state.user = response.data;
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
  mutations: {},
  actions: {
    async setDrawers({ state }: ActionContext<any, any>) {
      const response = await getApi<DrawerItem[]>('menus/drawer');
      state.drawers = response.data;
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
  mutations: {},
  actions: {
    async setMemberCodes({ state }: ActionContext<any, any>) {
      const response = await getApi<SelectItem[]>('members/lists/codes');
      state.members = response.data;
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

const moduleLayout = {
  state: {
    menuId: null,
    layouts: null,
    lockLayout: true,
    overlay: false,
  },
  mutations: {},
  actions: {
    saveLayout(
      { state, dispatch }: ActionContext<any, any>,
      layoutList: {
        x: number;
        y: number;
        w: number;
        h: number;
        i: string;
      }[],
    ) {
      try {
        putApi<{
          layoutList: {
            x: number;
            y: number;
            w: number;
            h: number;
            i: string;
          }[];
          // @ts-ignore
        }>(
          `layouts/${state.menuId}`,
          {
            layoutList: layoutList,
          },
          false,
        ).then(async () => {
          await dispatch('setLayouts');
        });
      } catch (e) {
        console.warn(e);
      }
    },
    async getLayout({ state, dispatch }: ActionContext<any, any>) {
      if (!state.layouts) {
        await dispatch('setLayouts');
      }
      const find = state.layouts.find(
        (item: { menuId: number; layoutList: string }) =>
          item.menuId === state.menuId,
      );
      if (find) {
        return JSON.parse(find.layoutList);
      } else {
        return null;
      }
    },
    async setLayouts({ state }: ActionContext<any, any>) {
      const response = await getApi<
        {
          menuId: number;
          layoutList: {
            x: number;
            y: number;
            w: number;
            h: number;
            i: string;
          }[];
        }[]
        // @ts-ignore
      >(`layouts`);
      state.layouts = response.data;
    },
    async getLayouts({ state, dispatch }: ActionContext<any, any>) {
      if (!state.layouts) {
        await dispatch('setLayouts');
      }
      return state.layouts;
    },
    setLayoutMenuId({ state }: ActionContext<any, any>, menuId: number) {
      state.menuId = menuId;
    },
    setOverlay({ state }: ActionContext<any, any>, value: boolean) {
      state.overlay = value;
    },
    clearLayout({ state }: ActionContext<any, any>) {
      state.layouts = null;
    },
  },
};

export default new Vuex.Store({
  modules: {
    user: moduleUser,
    drawer: moduleDrawer,
    cache: moduleCache,
    layout: moduleLayout,
  },
});
