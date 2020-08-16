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
      <v-icon class="pb-1">mdi-file-document-outline</v-icon>
      {{ title }}
    </v-alert>
    <v-container fluid>
      <router-view />
    </v-container>
  </v-main>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { DrawerItem, TableMenuEntity } from '@/common/types';

@Component({ name: 'Viewer' })
export default class extends Vue {
  mounted() {
    const items: TableMenuEntity[] = JSON.parse(
      window.localStorage.getItem('menus')!,
    );
    if (items && items.length > 0) {
      const result = items.find((item) => item.url === this.$route.fullPath);
      !result && this.$store.commit('error', 404);
    }
  }

  get title() {
    let result: string = '';
    const items: DrawerItem[] = JSON.parse(
      window.localStorage.getItem('drawer')!,
    );
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
      !result && this.$store.commit('error', 403);
    }
    return result.split('(팝업)').join('');
  }
}
</script>
