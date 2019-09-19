// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'babel-polyfill'
import Vue from 'vue'
import vuetify from '@/plugins/vuetify'
import App from './App'
import router from './router'
import axios from 'axios'
import VueAxios from 'vue-axios'

axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest' // only when using Laravel
Vue.prototype.apiURL = 'http://localhost:8000'

Vue.use(VueAxios, axios)
Vue.use(require('vue-moment'))

new Vue({
  vuetify,
  render: h => h(App),
  router
}).$mount('#app')
