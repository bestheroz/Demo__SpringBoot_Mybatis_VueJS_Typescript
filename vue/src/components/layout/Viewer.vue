<template>
  <v-main :title="null">
    <v-alert
      id="pageTitle"
      border="bottom"
      colored-border
      color="divider"
      dense
      class="mx-3 mt-3 mb-0 pl-6 elevation-1"
      v-if="title"
    >
      <v-icon class="pb-1">mdi-file-document-outline</v-icon>
      {{ title }}
    </v-alert>
    <v-container fluid class="pt-0 elevation-1">
      <router-view />
    </v-container>
  </v-main>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { DrawerItem } from '@/common/types';
import { errorPage } from '@/utils/errors';

@Component({ name: 'Viewer' })
export default class extends Vue {
  drawers: DrawerItem[] = [];

  get title(): string {
    if (this.$route.fullPath === '/index') {
      return '';
    }
    let result: string = '';
    if (this.drawers && this.drawers.length > 0) {
      result = this.findThisPage().title;
    }
    return result.split('(팝업)').join('');
  }

  findThisPage(): DrawerItem {
    let result: DrawerItem = { id: 0, title: '' };
    if (this.drawers && this.drawers.length > 0) {
      this.drawers.forEach((drawer) => {
        if (this.$route.name) {
          return { title: null };
        }
        if (drawer.children && drawer.children.length > 0) {
          const find = drawer.children.find((child: DrawerItem) => {
            if (child.to) {
              this.$store.dispatch('setLayoutMenuId', child.id);
              return child.to === this.$route.fullPath;
            }
          });
          find && (result = find);
        }
      });
    }
    if (!result) {
      errorPage(403);
    }
    this.$store.dispatch('setLayoutMenuId', result.id);
    return result;
  }

  @Watch('$store.state.drawer.drawers', { immediate: true })
  async watchDrawers() {
    this.drawers = await this.$store.dispatch('getDrawers');
  }
}
</script>
