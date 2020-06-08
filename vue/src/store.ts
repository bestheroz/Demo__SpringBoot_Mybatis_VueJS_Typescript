import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router';
import axios from 'axios';
import { TableMemberVO } from '@/common/types';
import envs from '@/constants/envs';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    axiosInstance: axios.create({
      baseURL: envs.API_HOST,
      headers: {
        'Content-Type': 'application/json',
        'X-Requested-With': `XMLHttpRequest`,
        Authorization: Vue.$storage.get('accessToken'),
      },
    }),
    logoutTime: new Date().getTime() + 2 * 3600 * 1000,
  },
  mutations: {
    async loginToken(state, userVO: TableMemberVO) {
      Vue.$storage.clear();
      Vue.$storage.set('accessToken', userVO.token);
      Vue.$storage.set('authority', userVO.authority);
      Vue.$storage.set('userVO', userVO);
      state.axiosInstance.defaults.headers.Authorization = userVO.token;
    },
    async logout(state) {
      try {
        await state.axiosInstance.delete('/api/auth/logout');
      } catch (e) {
        console.error(e);
      }
      Vue.$storage.clear();
      delete state.axiosInstance.defaults.headers.Authorization;
      await router.push('/login');
    },
    timer(state) {
      state.logoutTime =
        new Date().getTime() + Vue.$storage.get('timeout', 2 * 3600) * 1000;
    },
  },
  actions: {},
  modules: {},
});
