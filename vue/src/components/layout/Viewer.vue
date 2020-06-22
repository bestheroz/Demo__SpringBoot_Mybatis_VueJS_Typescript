<template>
  <v-main :title="null">
    <v-alert
      id="pageTitle"
      border="bottom"
      colored-border
      color="success"
      dense
      class="mx-3 mt-3 mb-0 pl-6"
      v-if="title"
    >
      <v-icon class="pb-1">mdi-circle-edit-outline</v-icon>
      {{ title }}
    </v-alert>
    <v-container fluid>
      <router-view />
    </v-container>
  </v-main>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { DrawerItem, TableMenuVO } from '@/common/types';

@Component({ name: 'Viewer' })
export default class extends Vue {
  mounted() {
    const items: TableMenuVO[] = this.$storage.get('menus');
    if (items && items.length > 0) {
      const result = items.find((item) => item.url === this.$route.fullPath);
      if (!result && this.$route.fullPath !== '/error/404') {
        this.$router.push('/error/404');
      }
    }
  }

  get title() {
    let result: string = '';
    const items: DrawerItem[] = this.$storage.get('drawer');
    console.log(items);
    if (items && items.length > 0) {
      items.forEach((item) => {
        if (this.$route.name) {
          return '';
        }
        if (item.children && item.children.length > 0) {
          const find = item.children.find((child: any) => {
            if (child.to) {
              return child.to === this.$route.fullPath;
            }
          });
          if (find) {
            result = find.title;
          }
        }
      });
      if (!result && this.$route.fullPath !== '/') {
        this.$router.push('/error/403');
      }
    }
    return result.split('(팝업)').join('');
  }
}
</script>
