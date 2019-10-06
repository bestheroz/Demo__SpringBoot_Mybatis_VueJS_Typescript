import { configure, extend } from 'vee-validate';
import { email, min, required } from 'vee-validate/dist/rules';
import i18n from '@/plugins/vue-i18n';

configure({
  // @ts-ignore
  defaultMessage: (field: any, values: any) => {
    // override the field name.
    values._field_ = i18n.t(`${field}`);

    return i18n.t(`validation.${values._rule_}`, values);
  },
});

extend('required', required);
extend('email', email);
extend('min', min);
