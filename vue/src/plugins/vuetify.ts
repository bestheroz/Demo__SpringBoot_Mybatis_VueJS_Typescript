import "@mdi/font/css/materialdesignicons.css";
import Vue from "vue";
import Vuetify from "vuetify/lib";
import ko from "vuetify/src/locale/ko";
import colors from "vuetify/lib/util/colors";

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: "mdi",
  },
  lang: {
    locales: { ko },
    current: "ko",
  },
  theme: {
    dark: false,
    themes: {
      light: {
        primary: colors.blue.lighten1,
        secondary: colors.grey.lighten2,
        accent: colors.blue.accent1,
        error: colors.red.accent2,
        info: colors.blue.lighten1,
        success: colors.green.lighten1,
        warning: colors.amber.lighten1,
        background: colors.grey.lighten5,
        text: colors.grey.darken4,
      },
      dark: {
        primary: colors.blue.darken1,
        secondary: colors.grey.darken3,
        accent: colors.blue.accent1,
        error: colors.red.accent3,
        info: colors.blue.darken1,
        success: colors.green.darken1,
        warning: colors.amber.darken3,
        background: colors.grey.darken4,
        text: colors.grey.lighten5,
      },
    },
    options: {
      customProperties: true,
      minifyTheme: function (css) {
        return process.env.NODE_ENV === "production"
          ? css.replace(/[\r|\n]/g, "")
          : css;
      },
    },
  },
});
