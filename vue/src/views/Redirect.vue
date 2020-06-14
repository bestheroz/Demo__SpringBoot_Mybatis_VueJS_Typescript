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
    const response = await getApi<ApiDataResult<TableMemberVO>>(`auth/me`);
    store.commit('saveUserVO', response.data);

    const response2 = await getApi('menu');
    this.$storage.set('drawer', response2.data);

    const response3 = await getApi('admin/menus');
    this.$storage.set('menus', response3.data);

    await this.$router.push('/admin/menu');
  }
}
</script>
