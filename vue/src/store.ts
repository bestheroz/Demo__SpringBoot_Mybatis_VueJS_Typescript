import Vue from 'vue';
import Vuex from 'vuex';
import { postDataApi } from '@/utils/api';
import _ from 'lodash';
import router from '@/router';
import axios from 'axios';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    appVersions: 'ver.191024',
    accessToken: null,
    host: process.env.VUE_APP_BASE_API_URL || 'http://localhost:8080/',
    language: process.env.VUE_APP_LANGUAGE,
    timezone: process.env.VUE_APP_TIMEZONE,
    axiosInstance: axios.create({
      baseURL: process.env.VUE_APP_BASE_API_URL || 'http://localhost:8080/',
      headers: {
        'Content-Type': 'application/json',
        'X-Requested-With': `XMLHttpRequest`,
        Authorization: localStorage.accessToken,
      },
    }),
  },
  mutations: {
    loginToken(state, accessToken) {
      state.accessToken = accessToken;
      // 토큰을 로컬 스토리지에 저장
      localStorage.accessToken = accessToken;
      console.info(accessToken);
      state.axiosInstance.defaults.headers['Authorization'] = accessToken;
    },
    async logout(state) {
      state.accessToken = null;
      // 토큰을 로컬 스토리지에서 제거
      delete localStorage.accessToken;
      delete state.axiosInstance.defaults.headers['Authorization'];
      await router.push('/');
    },
    async loginCheck(state): Promise<boolean> {
      const response = await postDataApi(`${state.host}sample/auth/verify`, {});
      return _.startsWith(response.code, `S`);
    },
  },
  actions: {},
  modules: {},
});
