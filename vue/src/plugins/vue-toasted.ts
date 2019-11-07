import Vue from 'vue';
// @ts-ignore
import Toasted from 'vue-toasted';

// you can also pass options, check options reference below
Vue.use(Toasted, {
  theme: 'outline',
  keepOnHover: true,
  className: 'large--toast',
});
