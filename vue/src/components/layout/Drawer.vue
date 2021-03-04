<template>
  <v-navigation-drawer v-if="!isPopup" v-model="syncedDrawer" app clipped fixed>
    <v-list>
      <template v-for="item in $store.getters.drawers">
        <v-list-group
          v-if="item.children"
          :key="item.id"
          :prepend-icon="item.icon"
        >
          <template #activator>
            <v-list-item-title v-text="item.name"></v-list-item-title>
          </template>
          <v-list-item
            v-for="child in item.children"
            :key="child.id"
            @click="movePage(child)"
            :class="selected === child.id ? 'v-list-item--active' : undefined"
            link
          >
            <v-list-item-icon>
              <v-icon>mdi-menu-right</v-icon>
            </v-list-item-icon>
            <v-list-item-title v-text="child.name" />
            <v-list-item-icon v-if="child.type === 'W'">
              <v-icon>mdi-dock-window</v-icon>
            </v-list-item-icon>
          </v-list-item>
        </v-list-group>

        <v-list-item
          v-else
          :key="item.id"
          @click="movePage(item)"
          :class="selected === item.id ? 'v-list-item--active' : undefined"
          link
        >
          <v-list-item-icon v-if="item.icon">
            <v-icon v-text="item.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-title v-text="item.name"></v-list-item-title>
        </v-list-item>
      </template>
    </v-list>
  </v-navigation-drawer>
</template>

<script lang="ts">
import { Component, PropSync, Vue } from "vue-property-decorator";
import type { DrawerItem } from "@/common/types";

@Component({
  name: "Drawer",
})
export default class extends Vue {
  @PropSync("drawer", { required: true, default: true })
  readonly syncedDrawer!: boolean;

  get isPopup(): boolean {
    return !window.toolbar.visible;
  }

  get selected(): number {
    return this.$store.getters.selected;
  }

  protected popupWindow(url: string): void {
    window.open(
      url,
      "_blank",
      // 'location=false,menubar=false,scrollbars=true,status=false,toolbar=false',
    );
  }

  protected movePage(item: DrawerItem): void {
    this.$store.dispatch("setMenuSelected", item.id || 0);
    if (!item.url || item.url === this.$route.fullPath) {
      return;
    }
    if (item.type === "W") {
      this.popupWindow(item.url);
    } else {
      item.url && this.$router.push(item.url);
    }
  }
}
</script>
