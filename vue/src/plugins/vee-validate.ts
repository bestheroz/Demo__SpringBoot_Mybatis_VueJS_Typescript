import Vue from 'vue';
import { extend, ValidationObserver, ValidationProvider } from 'vee-validate';
import * as rules from 'vee-validate/dist/rules';
// @ts-ignore
import ko from 'vee-validate/dist/locale/ko';

// loop over all rules
for (const rule in rules) {
  extend(rule, {
    // @ts-ignore
    ...rules[rule], // add the rule
    message: ko.messages[rule], // add its message
  });
}

Vue.component('ValidationProvider', ValidationProvider);
Vue.component('ValidationObserver', ValidationObserver);
