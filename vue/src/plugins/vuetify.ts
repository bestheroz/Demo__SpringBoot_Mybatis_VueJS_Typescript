import '@mdi/font/css/materialdesignicons.css';
import Vue from 'vue';
import 'vuetify/dist/vuetify.min.css';
import VuetifyToast from 'vuetify-toast-snackbar';
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

Vue.use(VuetifyToast, {
  x: 'center', // default
  y: 'top', // default
  color: 'info', // default
  icon: 'mdi-information-outline',
  iconColor: '', // default
  classes: ['body-2'],
  timeout: 5000, // default
  dismissable: true, // default
  multiLine: true, // default
  vertical: false, // default
  queueable: true, // default
  showClose: true, // default
  closeText: '', // default
  closeIcon: '', // default
  closeColor: '', // default
  slot: [], // default
  shorts: {
    success: {
      color: 'success',
    },
    warning: {
      color: 'warning',
    },
    error: {
      color: 'error',
    },
  },
  property: '$toast', // default
});

export default new Vuetify({
  icons: {
    iconfont: `mdi`,
    values: {},
  },
  lang: {
    t: (key: string, ...params: any) => i18n.t(key, params),
  },
});
