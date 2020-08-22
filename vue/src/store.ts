import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router';
import { TableMemberEntity } from '@/common/types';
import envs from '@/constants/envs';
import { axiosInstance } from '@/utils/apis';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    logoutTime: new Date().getTime() + 2 * 3600 * 1000,
  },
  mutations: {
    // @ts-ignore
    saveToken(state, token: { accessToken: string; refreshToken: string }) {
      window.localStorage.setItem('accessToken', token.accessToken);
      window.localStorage.setItem('refreshToken', token.refreshToken);
    },
    // @ts-ignore
    refreshToken(state, accessToken: string) {
      console.log(accessToken);
      window.localStorage.setItem('accessToken', accessToken);
    },
    // @ts-ignore
    saveUserVO(state, loginVO: TableMemberEntity) {
      window.localStorage.setItem('authority', loginVO.authority!.toString());
      window.localStorage.setItem('timeout', loginVO.timeout!.toString());
      window.localStorage.setItem('userVO', JSON.stringify(loginVO));
    },
    async logout() {
      try {
        await axiosInstance.delete(`${envs.API_HOST}api/auth/logout`);
      } catch (e) {
        console.error(e);
      }
      window.localStorage.clear();
      await router.replace('/login');
    },
    // @ts-ignore
    async error(state, statsCode) {
      if (
        !['/', '/login', '/error', '/error/403', '/error/404'].includes(
          router.currentRoute.path,
        )
      ) {
        await router.replace(`/error/${statsCode}`);
      }
    },
    async needLogin() {
      if (router.currentRoute.path !== '/login') {
        window.localStorage.clear();
        await router.replace('/login?login=need');
      }
    },
    timer(state) {
      state.logoutTime =
        new Date().getTime() +
        +(window.localStorage.getItem('timeout') || 2 * 3600) * 1000;
    },
  },
  actions: {},
  modules: {},
});
