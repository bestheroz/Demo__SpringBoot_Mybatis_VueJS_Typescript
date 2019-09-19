import Vue from "vue";
import vuetify from "@/plugins/vuetify";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./registerServiceWorker";
import axios from "axios";
import { AxiosInstance } from "axios";

Vue.config.productionTip = false;

export const axiosInstance: AxiosInstance = axios.create({
  baseURL: process.env.VUE_APP_BASE_API_URL,
  headers: { "X-Requested-With": "XMLHttpRequest" }
});

Vue.use(require("vue-moment"));

new Vue({
  router,
  store,
  // @ts-ignore
  vuetify,
  render: h => h(App)
}).$mount("#app");

require('dotenv').config()
console.log(process.env.NODE_ENV)

// TODO: eslint 설정 다시 추가