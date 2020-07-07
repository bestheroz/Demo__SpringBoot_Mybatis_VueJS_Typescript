import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router';
import { TableMemberVO } from '@/common/types';
import envs from '@/constants/envs';
import { axiosInstance } from '@/utils/apis';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    logoutTime: new Date().getTime() + 2 * 3600 * 1000,
  },
  mutations: {
    saveToken(state, token: { accessToken: string; refreshToken: string }) {
      Vue.$storage.set('accessToken', token.accessToken);
      Vue.$storage.set('refreshToken', token.refreshToken);
    },
    saveUserVO(state, loginVO: TableMemberVO) {
      Vue.$storage.set('authority', loginVO.authority);
      Vue.$storage.set('timeout', loginVO.timeout);
      Vue.$storage.set('userVO', loginVO);
    },
    async logout() {
      try {
        await axiosInstance.delete(`${envs.API_HOST}api/auth/logout`);
      } catch (e) {
        console.error(e);
      }
      Vue.$storage.clear();
      await router.replace('/login');
    },
    async error(statsCode) {
      if (
        ![
          '/',
          '/login',
          '/error',
          '/error/403',
          '/error/404',
          '/error/500',
          '/error/503',
        ].includes(router.currentRoute.path)
      ) {
        await router.replace(`/error/${statsCode}`);
      }
    },
    async needLogin() {
      if (router.currentRoute.path !== '/login') {
        Vue.$storage.clear();
        await router.replace('/login?login=need');
      }
    },
    timer(state) {
      state.logoutTime =
        new Date().getTime() + Vue.$storage.get('timeout', 2 * 3600) * 1000;
    },
  },
  actions: {},
  modules: {},
});
