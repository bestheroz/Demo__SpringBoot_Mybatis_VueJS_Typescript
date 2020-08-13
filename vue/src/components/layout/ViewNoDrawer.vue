<template>
  <v-main :title="null">
    <router-view />
  </v-main>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { DrawerItem, TableMenuVO } from '@/common/types';

@Component({ name: 'ViewNoDrawer' })
export default class extends Vue {
  items: DrawerItem[] | null = null;

  mounted() {
    this.items = JSON.parse(window.localStorage.getItem('drawer')!);
    const items: TableMenuVO[] = JSON.parse(
      window.localStorage.getItem('menus')!,
    );
    if (items && items.length > 0) {
      const result = items.find((item) => item.url === this.$route.fullPath);
      !result && this.$store.commit('error', 404);
    }
  }
}
</script>
