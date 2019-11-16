import '@mdi/font/css/materialdesignicons.css';
import Vue from 'vue';
import 'vuetify/dist/vuetify.min.css';
import i18n from '@/plugins/vue-i18n';
import { VBtn, VIcon, VSnackbar } from 'vuetify/lib';

const Vuetify = require(`vuetify`);

Vue.use(Vuetify, {
  components: {
    VSnackbar,
    VBtn,
    VIcon,
  },
});

const theme = {
  primary: '#E91E63',
  secondary: '#9C27b0',
  accent: '#E91E63',
  info: '#00CAE3',
};

export default new Vuetify({
  icons: {
    iconfont: `mdi`,
    values: {},
  },
  lang: {
    t: (key: string, ...params: any) => i18n.t(key, params),
  },
  theme: {
    dark: true,
    themes: {
      dark: theme,
    },
  },
});
