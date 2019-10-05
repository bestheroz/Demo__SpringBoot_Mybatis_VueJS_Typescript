import Vue from 'vue';
import Vuex from 'vuex';
import snack from '@/components/snack/snack';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    appVersions: 'ver.191005',
    host: process.env.VUE_APP_BASE_API_URL,
    language: process.env.VUE_APP_LANGUAGE,
  },
  mutations: {},
  actions: {},
  modules: {
    snack,
  },
});
