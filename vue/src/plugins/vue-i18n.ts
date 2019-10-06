import Vue from 'vue';
import VueI18n from 'vue-i18n';
import en from 'vuetify/src/locale/en';
import ko from 'vuetify/src/locale/ko';

Vue.use(VueI18n);

const messages = {
  ko: {
    ...require(`@/locales/en.json`),
    $vuetify: ko,
  },
  en: {
    ...require(`@/locales/en.json`),
    $vuetify: en,
  },
};

export default new VueI18n({
  locale: process.env.VUE_APP_I18N_LOCALE || `ko`,
  fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || `ko`,
  silentTranslationWarn: true,
  messages,
});
