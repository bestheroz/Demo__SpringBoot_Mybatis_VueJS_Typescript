import Vue from "vue";
import vuetify from "@/plugins/vuetify";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./plugins/vee-validate";
import "./plugins/vue-filter";
import "./plugins/sentry";
import dotenv from "dotenv";
import "./assets/scss/theme.scss";
import VueCompositionApi from "@vue/composition-api";

Vue.config.productionTip = false;

Vue.use(VueCompositionApi);

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");

dotenv.config();
