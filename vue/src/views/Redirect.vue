<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getMyData } from '@/utils/apis';

const pbkdf2 = require('pbkdf2');
const queryString = require('query-string');

@Component({
  name: 'Redirect',
})
export default class extends Vue {
  async mounted() {
    if (!Vue.$storage.has('accessToken')) {
      await this.$router.push('/login');
    }
    if (!Vue.$storage.has('userVO') || !Vue.$storage.has('authority')) {
      await getMyData();
    }
    console.log(Vue.$storage.get('accessToken'));
    await this.$router.push('/admin/menu');
  }
}
</script>
