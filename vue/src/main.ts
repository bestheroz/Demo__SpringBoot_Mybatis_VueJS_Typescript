import Vue from 'vue';
import vuetify from '@/plugins/vuetify';
import App from './App.vue';
import router from './router';
import store from './store';
import './registerServiceWorker';
import './plugins/vue-toasted';
import './plugins/vee-validate';
import './plugins/vue-filter';
import './plugins/vue-grid-layout';
import './plugins/sentry';

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount(`#app`);

require(`dotenv`).config();
