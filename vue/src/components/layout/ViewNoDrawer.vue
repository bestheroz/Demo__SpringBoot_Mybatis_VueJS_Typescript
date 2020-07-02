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
    this.items = this.$storage.get('drawer');
    const items: TableMenuVO[] = this.$storage.get('menus');
    if (items && items.length > 0) {
      const result = items.find((item) => item.url === this.$route.fullPath);
      if (
        !result &&
        ![
          '/',
          '/login',
          '/login?login=need',
          '/error/403',
          '/error/404',
          '/error/500',
          '/error/503',
        ].includes(this.$route.fullPath)
      ) {
        this.$router.replace('/error/404');
      }
    }
  }
}
</script>
