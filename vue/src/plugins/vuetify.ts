import "@mdi/font/css/materialdesignicons.css";
import Vue from "vue";
import Vuetify from "vuetify/lib";
import ko from "vuetify/src/locale/ko";
import config from "../configs";

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: "mdi",
  },
  lang: {
    locales: { ko },
    current: "ko",
  },
  rtl: config.theme.isRTL,
  theme: {
    dark: config.theme.globalTheme === "dark",
    themes: {
      dark: config.theme.dark,
      light: config.theme.light,
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
