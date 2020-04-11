import '@mdi/font/css/materialdesignicons.css';
import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import ko from 'vuetify/src/locale/ko';

Vue.use(Vuetify);

const LRU = require('lru-cache');
const options = {
  max: 10,
  maxAge: 12000 * 60 * 60, // 12 hours
};
const themeCache = new LRU(options);

export default new Vuetify({
  icons: {
    iconfont: `mdi`,
  },
  lang: {
    locales: { ko },
    current: 'ko',
  },
  theme: {
    dark: true,
    themes: {
      dark: {
        primary: '#4CAF50',
        secondary: '#9C27b0',
        // accent: '#E91E63',
        accent: '#4caf50',
        error: '#FF5252',
        info: '#00CAE3',
        success: '#4CAF50',
        warning: '#FFC107',
        'button-default': '#4caf50',
        'button-add': '#00796B',
        'button-edit': '#1565C0',
        'button-delete': '#E91E63',
        'button-reload': '#00ACC1',
      },
    },
    options: {
      themeCache,
      minifyTheme: function (css) {
        return process.env.NODE_ENV === 'production'
          ? css.replace(/[\r\n|\r|\n]/g, '')
          : css;
      },
    },
  },
});
