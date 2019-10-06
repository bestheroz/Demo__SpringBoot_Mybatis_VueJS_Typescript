import Vue from 'vue';
import Vuex from 'vuex';
import { postDataApi } from '@/utils/api';
import _ from 'lodash';
import router from '@/router';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    appVersions: 'ver.191006',
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
      router.push('/');
    },
    async loginCheck(state): Promise<boolean> {
      const response = await postDataApi(`${state.host}sample/auth/verify`, {});
      return _.startsWith(response.code, `S`);
    },
  },
  actions: {},
  modules: {},
});
