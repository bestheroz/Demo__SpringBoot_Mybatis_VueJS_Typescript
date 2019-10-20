import Vue from 'vue';
import vuetify from '@/plugins/vuetify';
import App from './App.vue';
import router from './router';
import store from './store';
import './registerServiceWorker';
import './plugins/vue-toasted';
import './plugins/vue-moment';
import './plugins/vee-validate';
import i18n from './plugins/vue-i18n';

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  // @ts-ignore
  i18n,
  vuetify,
  render: h => h(App),
}).$mount(`#app`);

require(`dotenv`).config();

// Component.registerHooks([`validations`]);
