import Vue from 'vue';
import VueI18n from 'vue-i18n';
// @ts-ignore
import veeKo from 'vee-validate/dist/locale/ko.json';
// @ts-ignore
import veeEn from 'vee-validate/dist/locale/en.json';
import en from 'vuetify/src/locale/en';
import ko from 'vuetify/src/locale/ko';

Vue.use(VueI18n);

const messages = {
  ko: {
    ...require(`@/locales/en.json`),
    $vuetify: ko,
    validation: veeKo.messages,
  },
  en: {
    ...require(`@/locales/en.json`),
    $vuetify: en,
    validation: veeEn.messages,
  },
};

export default new VueI18n({
  locale: process.env.VUE_APP_I18N_LOCALE || `ko`,
  fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || `ko`,
  silentTranslationWarn: true,
  messages,
});
