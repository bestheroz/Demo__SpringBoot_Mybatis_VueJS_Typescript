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
          <template #activator>
            <v-list-item-content>
              <v-list-item-title> {{ item.title }}</v-list-item-title>
            </v-list-item-content>
          </template>
          <v-list-item
            :key="child.title"
            :to="child.type === 'W' || !child.to ? undefined : child.to"
            @click="child.type === 'W' ? popupWindow(child.to) : undefined"
            v-for="child in item.children"
            link
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
          :key="item.title"
          :link="!!item.to"
          @click="movePage(item)"
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

    <template #append>
      <div class="ma-2">
        <v-row no-gutters>
          <v-col cols="10">
            <v-btn outlined @click="logout" class="px-10">
              <v-icon>mdi-logout</v-icon>
              Logout
            </v-btn>
          </v-col>
          <v-col cols="2" class="text-right">
            <v-btn icon outlined @click="changeTheme">
              <v-icon v-if="$vuetify.theme.dark"> mdi-weather-night</v-icon>
              <v-icon v-else> mdi-weather-sunny</v-icon>
            </v-btn>
          </v-col>
        </v-row>
      </div>
    </template>
  </v-navigation-drawer>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from "vue-property-decorator";
import type { DrawerItem } from "@/common/types";
import { logout } from "@/utils/authentications";
import { postApi } from "@/utils/apis";

@Component({
  name: "Drawer",
})
export default class extends Vue {
  @PropSync("drawer", { required: true, default: true })
  readonly syncedDrawer!: boolean;

  readonly logout: typeof logout = logout;

  items: DrawerItem[] | null = null;

  get isPopup(): boolean {
    return !window.toolbar.visible;
  }

  @Watch("$store.state.drawer.drawers", { immediate: true })
  async watchDrawers(): Promise<void> {
    this.items = await this.$store.dispatch("getDrawers");
  }

  popupWindow(url: string) {
    window.open(
      url,
      "_blank",
      // 'location=false,menubar=false,scrollbars=true,status=false,toolbar=false',
    );
  }

  async changeTheme(): Promise<void> {
    this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
    window.localStorage.setItem(
      "theme",
      this.$vuetify.theme.dark ? "dark" : "light",
    );
    try {
      await postApi<{
        theme: string;
      }>(
        "members/mine/changeTheme/",
        {
          theme: this.$vuetify.theme.dark ? "dark" : "light",
        },
        false,
      );
      await this.$store.dispatch("setUser");
    } catch (e) {
      console.warn(e);
    }
  }

  movePage(item: DrawerItem) {
    if (!item.to || item.to === this.$route.fullPath) {
      return;
    }
    if (item.type === "W") {
      this.popupWindow(item.to);
    } else {
      item.to && this.$router.push(item.to);
    }
  }
}
</script>
