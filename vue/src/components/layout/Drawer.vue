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
              <v-list-item-title> {{ item.title }}</v-list-item-title>
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
              <v-icon>mdi-menu-right</v-icon>
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
            <v-icon> {{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title> {{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
    </v-list>

    <template v-slot:append>
      <div class="ma-2">
        <v-row no-gutters>
          <v-col cols="8">
            <v-btn outlined @click="logout">
              <v-icon>mdi-logout</v-icon>
              Logout
            </v-btn>
          </v-col>
          <v-col cols="4" class="text-right">
            <v-btn
              icon
              outlined
              @click="
                $store.state.layout.lockLayout = !$store.state.layout.lockLayout
              "
            >
              <v-icon v-if="$store.state.layout.lockLayout">
                mdi-arrow-expand-all
              </v-icon>
              <v-icon v-else> mdi-arrow-vertical-lock</v-icon>
            </v-btn>
            <v-btn icon outlined @click="changeTheme">
              <v-icon v-if="$vuetify.theme.dark">mdi-weather-sunny</v-icon>
              <v-icon v-else>mdi-weather-night</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </div>
    </template>
  </v-navigation-drawer>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from 'vue-property-decorator';
import { DrawerItem } from '@/common/types';
import { logout } from '@/utils/authentications';
import { postApi } from '@/utils/apis';

@Component({
  name: 'Drawer',
})
export default class extends Vue {
  @PropSync('drawer', { required: true, default: true })
  readonly syncedDrawer!: boolean;

  readonly logout: typeof logout = logout;

  items: DrawerItem[] | null = null;

  get isPopup(): boolean {
    return !window.toolbar.visible;
  }

  @Watch('$store.state.drawer.drawers', { immediate: true })
  async watchDrawers() {
    this.items = await this.$store.dispatch('getDrawers');
  }

  popupWindow(url: string) {
    window.open(
      url,
      '_blank',
      // 'location=false,menubar=false,scrollbars=true,status=false,toolbar=false',
    );
  }

  changeTheme() {
    this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
    window.localStorage.setItem(
      'theme',
      this.$vuetify.theme.dark ? 'dark' : 'light',
    );
    try {
      postApi<{
        theme: string;
      }>(
        `members/${this.$store.state.user.user.id}/changeTheme/`,
        {
          theme: this.$vuetify.theme.dark ? 'dark' : 'light',
        },
        false,
      );
    } catch (e) {
      console.warn(e);
    }
  }
}
</script>
