import '@mdi/font/css/materialdesignicons.css';
import Vue from 'vue';
import 'vuetify/dist/vuetify.min.css';

const Vuetify = require(`vuetify`);

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: `mdi`,
    values: {},
  },
});
