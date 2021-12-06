<template>
  <div>
    <v-list-item
      v-if="drawer.type !== MENU_TYPE.GROUP"
      @click="newTab"
      active-class="primary--text"
      :style="{
        'margin-left': `24px`,
      }"
    >
      <v-list-item-icon v-if="drawer.icon">
        <v-icon v-text="drawer.icon" />
      </v-list-item-icon>
      <v-list-item-content>
        <v-list-item-title v-text="drawer.name" />
      </v-list-item-content>
    </v-list-item>

    <v-list-group
      v-else
      :style="{
        'margin-left': depth === 0 ? undefined : `24px`,
      }"
    >
      <template #activator>
        <v-list-item-icon
          v-if="drawer.type === MENU_TYPE.GROUP"
          :style="
            depth !== 0
              ? 'margin-right: 4px !important;'
              : 'margin-right: 8px !important'
          "
        >
          <v-icon v-text="drawer.icon" />
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title v-text="drawer.name" />
        </v-list-item-content>
      </template>
      <slot />
    </v-list-group>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import type { Drawer } from "@/definitions/types";
import { MENU_TYPE } from "@/definitions/selections";
import { routerPush, routerToNewTab } from "@/utils/commands";

@Component({})
export default class extends Vue {
  @Prop({ required: true }) readonly drawer!: Drawer;
  @Prop({ required: true }) readonly depth!: number;

  readonly MENU_TYPE = MENU_TYPE;

  protected newTab(): void {
    if (!this.drawer.url) {
      return;
    }
    const routeData = this.$router.resolve(this.drawer.url);
    if (this.drawer.type === MENU_TYPE.PAGE) {
      routerPush(routeData.href);
    } else if (this.drawer.type === MENU_TYPE.NEW_TAB) {
      routerToNewTab(routeData.href);
    }
  }
}
</script>
