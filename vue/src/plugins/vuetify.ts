import '@mdi/font/css/materialdesignicons.css';
import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import ko from 'vuetify/src/locale/ko';
import colors from 'vuetify/lib/util/colors';

Vue.use(Vuetify);

const LRU = require('lru-cache');
const themeCache = new LRU({
  max: 10,
  maxAge: 12000 * 60 * 60, // 12 hours
});

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
        primary: colors.green.darken1,
        secondary: colors.purple.base,
        accent: colors.green.accent4,
        error: colors.red.accent2,
        info: colors.blue.base,
        success: colors.green.base,
        warning: colors.amber.base,
        divider: colors.green.base,
        'button-default': colors.green.base,
        'button-add': colors.teal.darken2,
        'button-edit': colors.blue.darken3,
        'button-delete': colors.pink.base,
        'button-reload': colors.cyan.darken1,
        'table-header': colors.green.darken4,
        'table-border': colors.grey.darken3,
      },
      light: {
        primary: colors.blue.darken2,
        secondary: colors.grey.darken3,
        accent: colors.blue.accent1,
        error: colors.red.accent2,
        info: colors.blue.base,
        success: colors.green.base,
        warning: colors.amber.base,
        divider: colors.blue.accent1,
        'button-default': colors.blue.accent3,
        'button-add': colors.blue.accent2,
        'button-edit': colors.teal.accent4,
        'button-delete': colors.pink.accent2,
        'button-reload': colors.cyan.darken1,
        'table-header': colors.blue.lighten5,
        'table-border': colors.lightBlue.lighten4,
      },
    },
    options: {
      themeCache,
      customProperties: true,
      minifyTheme: function (css) {
        return process.env.NODE_ENV === 'production'
          ? css.replace(/[\r\n|\r|\n]/g, '')
          : css;
      },
    },
  },
});
