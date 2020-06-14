<template>
  <v-navigation-drawer v-model="syncedDrawer" app clipped fixed v-if="!isPopup">
    <v-list>
      <template v-for="item in items">
        <v-list-group
          v-if="item.children"
          :key="item.title"
          :prepend-icon="item.icon"
          :value="false"
        >
          <template v-slot:activator>
            <v-list-item-content>
              <v-list-item-title> {{ item.title }} </v-list-item-title>
            </v-list-item-content>
          </template>
          <v-list-item
            v-for="(child, i) in item.children"
            :key="i"
            @click="child.type === 'W' ? popupWindow(`${child.to}`) : undefined"
            :to="
              child.type !== 'W' ? `${!!child.to ? child.to : ''}` : undefined
            "
            :link="!!item.to"
          >
            <v-list-item-action>
              <v-icon v-if="child.icon"> {{ child.icon }} </v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>
                {{ child.title }}
                <v-icon v-if="child.type === 'W'">mdi-dock-window</v-icon>
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-group>

        <v-list-item
          v-else
          :to="!!item.to ? item.to : ''"
          :key="item.title"
          :link="!!item.to"
        >
          <v-list-item-action>
            <v-icon> {{ item.icon }} </v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title> {{ item.title }} </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
    </v-list>

    <template v-slot:append>
      <div class="pa-2">
        <v-btn block @click="$store.commit('logout')">
          <v-icon>mdi-logout</v-icon>
          Logout
        </v-btn>
      </div>
    </template>
  </v-navigation-drawer>
</template>

<script lang="ts">
import { Component, PropSync, Vue } from 'vue-property-decorator';
import { DrawerItem } from '@/common/types';

@Component({
  name: 'Drawer',
})
export default class extends Vue {
  @PropSync('drawer', { required: true, default: true })
  readonly syncedDrawer!: boolean;

  items: DrawerItem[] | null = null;

  get isPopup(): boolean {
    return !window.toolbar.visible;
  }

  async created() {
    if (!this.$storage.has('drawer')) {
      const response = await this.$axios.get('api/menu');
      this.$storage.set('drawer', response.data.data);
    }
    this.items = this.$storage.get('drawer');
  }

  popupWindow(url: string) {
    window.open(
      `${url.indexOf('http') === 0 ? '' : '#'}${url}`,
      '_blank',
      // 'location=false,menubar=false,scrollbars=true,status=false,toolbar=false',
    );
  }
}
</script>
