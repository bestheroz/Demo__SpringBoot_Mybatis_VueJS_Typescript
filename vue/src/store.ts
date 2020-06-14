import Vue from 'vue';
import Vuex from 'vuex';
import router from '@/router';
import axios from 'axios';
import { TableMemberVO } from '@/common/types';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    logoutTime: new Date().getTime() + 2 * 3600 * 1000,
  },
  mutations: {
    async accessToken(state, token: string) {
      console.log(token);
      Vue.$storage.set('accessToken', token);
    },
    async loginToken(state, loginVO: TableMemberVO) {
      Vue.$storage.clear();
      Vue.$storage.set('authority', loginVO.authority);
      Vue.$storage.set('userVO', loginVO);
    },
    async logout(state) {
      try {
        await axios.delete('/api/auth/logout');
      } catch (e) {
        console.error(e);
      }
      Vue.$storage.clear();
      delete axios.defaults.headers.Authorization;
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
