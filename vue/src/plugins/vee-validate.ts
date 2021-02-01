import Vue from "vue";
import {
  extend,
  localize,
  ValidationObserver,
  ValidationProvider,
} from "vee-validate";
import * as rules from "vee-validate/dist/rules";
import ko from "vee-validate/dist/locale/ko.json";
import { ValidationRule } from "vee-validate/dist/types/types";

// install
localize({
  ko,
});

// active
localize("ko");

// loop over all rules
Object.entries(rules).forEach(([rule, validation]) => {
  extend(rule, {
    ...validation,
  } as ValidationRule);
});

Vue.component("ValidationProvider", ValidationProvider);
Vue.component("ValidationObserver", ValidationObserver);
