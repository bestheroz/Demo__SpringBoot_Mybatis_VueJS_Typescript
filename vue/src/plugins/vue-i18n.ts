import Vue from "vue";
import VueI18n from "vue-i18n";
import msg from "@/constants/translations/msg";
import { en, ko } from "vuetify/src/locale";

Vue.use(VueI18n);

const i18n = new VueI18n({
  // locale: store.getters.lang,
  locale: "ko",
  fallbackLocale: "ko",
  messages: {
    en: {
      msg: msg.en,
      $vuetify: en,
    },
    ko: {
      msg: msg.ko,
      $vuetify: ko,
    },
  },
});
export default i18n;
