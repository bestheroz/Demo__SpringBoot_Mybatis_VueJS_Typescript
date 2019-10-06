import Vue from 'vue';
import vuetify from '@/plugins/vuetify';
import App from './App.vue';
import router from './router';
import store from './store';
import './registerServiceWorker';
// eslint-disable-next-line import/no-duplicates
// eslint-disable-next-line import/no-duplicates
import axios, { AxiosInstance } from 'axios';
import i18n from './plugins/vue-i18n';
import { Component } from 'vue-property-decorator';
import '@/plugins/vee-validate';

Vue.config.productionTip = false;

export const axiosInstance: AxiosInstance = axios.create({
  baseURL: process.env.VUE_APP_BASE_API_URL,
  headers: {
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'X-Requested-With': `XMLHttpRequest`,
    'x-access-token': localStorage.accessToken,
  },
});

Vue.use(require(`vue-moment`) as any);

new Vue({
  router,
  store,
  // @ts-ignore
  i18n,
  vuetify,
  render: h => h(App),
}).$mount(`#app`);

require(`dotenv`).config();

Component.registerHooks([`validations`]);
