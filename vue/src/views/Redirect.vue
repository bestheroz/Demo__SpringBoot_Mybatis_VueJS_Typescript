<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { TableMemberVO, TableMenuVO } from '@/common/types';
import store from '@/store';
import { ApiDataResult, getApi } from '@/utils/apis';

@Component({
  name: 'Redirect',
})
export default class extends Vue {
  async mounted() {
    if (!Vue.$storage.has('accessToken') || !Vue.$storage.has('refreshToken')) {
      await this.$router.replace('/login?login=need');
      return;
    }
    const response = await getApi<TableMemberVO>(`auth/me`);
    if (!response.data) {
      await this.$router.replace('/login?login=need');
      return;
    }
    store.commit('saveUserVO', response.data);

    const response2 = await getApi<any>('menus/drawer');
    if (!response2.data) {
      await this.$router.replace('/login?login=need');
      return;
    }
    this.$storage.set('drawer', response2.data);

    const response3 = await getApi<TableMenuVO[]>('menus');
    if (!response3.data) {
      await this.$router.replace('/login?login=need');
      return;
    }
    this.$storage.set('menus', response3.data);

    await this.$router.push('/admin/menu');
  }
}
</script>
