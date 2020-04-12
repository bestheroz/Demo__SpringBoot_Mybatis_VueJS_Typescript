<template>
  <v-content :title="null">
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
  </v-content>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { DrawerItem, SelectItem } from '@/common/types';
import { getListApi } from '@/utils/apis';

@Component({ name: 'Viewer' })
export default class extends Vue {
  items: DrawerItem[] | null = null;
  async beforeCreate() {
    if (!this.$storage.has('drawer')) {
      const response = await this.$store.state.axiosInstance.get('/menu');
      this.$storage.set('drawer', response.data.data);
      this.items = response.data.data;
    }
    if (!Vue.$storage.has('empList')) {
      const response = await getListApi<SelectItem[]>(`/admin/user/empList`);
      Vue.$storage.set('empList', response.data);
    }
  }

  async updated() {
    if (!this.$storage.has('drawer')) {
      const response = await this.$store.state.axiosInstance.get('/menu');
      this.$storage.set('drawer', response.data.data);
      this.items = response.data.data;
    }
    if (!Vue.$storage.has('empList')) {
      const response = await getListApi<SelectItem[]>(`/admin/user/empList`);
      Vue.$storage.set('empList', response.data);
    }
  }

  get title() {
    let result: string = '';
    if (this.items && this.items.length > 0) {
      this.items.forEach((item) => {
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
      if (
        !result &&
        this.$route.fullPath !== '/' &&
        this.$route.fullPath !== '/tester/grafana'
      ) {
        this.$router.push('/Code403');
      }
    }
    return result.split('(팝업)').join('');
  }
}
</script>
