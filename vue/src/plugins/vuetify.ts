import "@mdi/font/css/materialdesignicons.css";
import Vue from "vue";
import Vuetify from "vuetify/lib";
import ko from "vuetify/src/locale/ko";
import colors from "vuetify/lib/util/colors";

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: `mdi`,
  },
  lang: {
    locales: { ko },
    current: "ko",
  },
  theme: {
    dark: false,
    themes: {
      dark: {
        primary: colors.green.darken1,
        secondary: colors.brown.darken1,
        accent: colors.green.accent4,
        error: colors.red.accent2,
        info: colors.blue.base,
        success: colors.green.base,
        warning: colors.amber.base,
        divider: colors.green.base,
        "button-default": colors.green.base,
        "button-add": colors.teal.darken2,
        "button-edit": colors.blue.darken3,
        "button-delete": colors.pink.base,
        "button-reload": colors.cyan.darken1,
        "button-disabled": colors.blueGrey.lighten2,
        "table-header": colors.green.darken2,
        "table-border": colors.grey.darken3,
        "alert-background": colors.grey.darken4,
        "alert-text": colors.grey.lighten5,
        "modal-background": colors.grey.darken4,
        "modal-header": colors.green.darken2,
        "modal-border": colors.lightGreen.darken4,
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
        "button-default": colors.blue.accent3,
        "button-add": colors.blue.accent2,
        "button-edit": colors.teal.accent4,
        "button-delete": colors.pink.accent2,
        "button-reload": colors.cyan.darken1,
        "button-disabled": colors.blueGrey.lighten2,
        "table-header": colors.blue.lighten5,
        "table-border": colors.lightBlue.lighten4,
        "alert-background": colors.grey.lighten5,
        "alert-text": colors.grey.darken4,
        "modal-background": colors.blue.lighten1,
        "modal-header": colors.blue.lighten5,
        "modal-border": colors.blue.lighten3,
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
