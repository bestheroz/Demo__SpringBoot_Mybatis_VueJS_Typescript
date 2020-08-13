<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {
  DrawerItem,
  SelectItem,
  TableMemberVO,
  TableMenuVO,
} from '@/common/types';
import store from '@/store';
import { getApi } from '@/utils/apis';

@Component({
  name: 'Redirect',
})
export default class extends Vue {
  async mounted() {
    if (
      !window.localStorage.getItem('accessToken') ||
      !window.localStorage.getItem('refreshToken')
    ) {
      store.commit('needLogin');
      return;
    }
    const response = await getApi<TableMemberVO>(`auth/me`);
    if (!response.data) {
      store.commit('needLogin');
      return;
    }
    store.commit('saveUserVO', response.data);

    const response2 = await getApi<DrawerItem[]>('menus/drawer');
    if (!response2.data) {
      store.commit('needLogin');
      return;
    }
    window.localStorage.setItem('drawer', JSON.stringify(response2.data));

    const response3 = await getApi<TableMenuVO[]>('menus');
    if (!response3.data) {
      store.commit('needLogin');
      return;
    }
    window.localStorage.setItem('menus', JSON.stringify(response3.data));
    await this.$router.push('/admin/menu');

    const response4 = await getApi<SelectItem[]>('admin/members/memberList');
    if (!response4.data) {
      store.commit('needLogin');
      return;
    }
    window.localStorage.setItem('memberList', JSON.stringify(response4.data));
    await this.$router.push('/admin/menu');
  }
}
</script>
