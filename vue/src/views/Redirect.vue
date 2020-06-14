<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { TableMemberVO } from '@/common/types';
import store from '@/store';
import { ApiDataResult, getApi } from '@/utils/apis';

@Component({
  name: 'Redirect',
})
export default class extends Vue {
  async mounted() {
    if (!Vue.$storage.has('accessToken')) {
      await this.$router.push('/login');
    }
    if (!Vue.$storage.has('userVO') || !Vue.$storage.has('authority')) {
      const response = await getApi<ApiDataResult<TableMemberVO>>(`auth/me`);
      store.commit('saveUserVO', response.data);
    }

    if (!Vue.$storage.has('drawer')) {
      const response = await getApi('menu');
      this.$storage.set('drawer', response.data);
    }

    await this.$router.push('/admin/menu');
  }
}
</script>
