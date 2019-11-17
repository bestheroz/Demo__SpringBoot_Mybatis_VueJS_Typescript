import Vue from 'vue';
import Vuex from 'vuex';
import { postDataApi } from '@/utils/apis';
import _ from 'lodash';
import router from '@/router';
import axios from 'axios';
import envs from '@/constants/envs';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    accessToken: null,
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
      const response = await postDataApi(`${envs.HOST}sample/auth/verify`, {});
      return _.startsWith(response.responseCode, `S`);
    },
  },
  actions: {},
  modules: {},
});
