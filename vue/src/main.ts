import Vue from 'vue';
import vuetify from '@/plugins/vuetify';
import App from './App.vue';
import router from './router';
import store from './store';
import './registerServiceWorker';
// eslint-disable-next-line import/no-duplicates
// eslint-disable-next-line import/no-duplicates
import i18n from './plugins/vue-i18n';
import { Component } from 'vue-property-decorator';
import Vuelidate from 'vuelidate';

Vue.config.productionTip = false;

Vue.use(Vuelidate);
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
