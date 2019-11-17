import Vue from 'vue';
import VueI18n from 'vue-i18n';
import en from 'vuetify/src/locale/en';
import ko from 'vuetify/src/locale/ko';
import i18nKo from '@/locales/i18n-ko';
import i18nEn from '@/locales/i18n-en';

Vue.use(VueI18n);

const messages = {
  ko: {
    ...i18nKo,
    $vuetify: ko,
  },
  en: {
    ...i18nEn,
    $vuetify: en,
  },
};

export default new VueI18n({
  locale: process.env.VUE_APP_I18N_LOCALE || `ko`,
  fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || `ko`,
  silentTranslationWarn: true,
  messages,
});
