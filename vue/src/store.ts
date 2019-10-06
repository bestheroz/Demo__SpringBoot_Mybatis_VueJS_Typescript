import Vue from 'vue';
import Vuex from 'vuex';
import { postDataApi } from '@/utils/api';
import _ from 'lodash';
import router from '@/router';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    appVersions: 'ver.191005',
    accessToken: null,
    host: process.env.VUE_APP_BASE_API_URL,
    language: process.env.VUE_APP_LANGUAGE,
  },
  mutations: {
    loginToken(state, { accessToken }) {
      state.accessToken = accessToken;
      // 토큰을 로컬 스토리지에 저장
      localStorage.accessToken = accessToken;
    },
    logout(state) {
      state.accessToken = null;
      // 토큰을 로컬 스토리지에서 제거
      delete localStorage.accessToken;
    },
    async loginCheck(state) {
      const response = await postDataApi(`${state.host}sample/auth/verify`, {});
      if (_.startsWith(response.code, `F`)) {
        await router.push('/');
      }
    },
  },
  actions: {},
  modules: {},
});
