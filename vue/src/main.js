// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'babel-polyfill'
import Vue from 'vue'
import vuetify from '@/plugins/vuetify'
import App from "./App";
import router from './router'
import axios from 'axios';

Vue.prototype.$http = axios;
window.axios = axios;
axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'; //only when using Laravel
window.apiURL = 'http://localhost:8000';

new Vue({
  vuetify,
  render: h => h(App),
  router,
}).$mount('#app');
