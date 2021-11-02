<template>
  <v-list nav dense class="py-0">
    <!-- menu level 1 -->
    <nav-menu-item
      v-for="drawer in drawers"
      :key="drawer.id"
      :drawer="drawer"
      :depth="depth"
    >
      <template>
        <nav-menu :drawers="drawer.children" :depth="depth + 1" />
      </template>
    </nav-menu-item>
  </v-list>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import NavMenuItem from "@/components/navigation/NavMenuItem.vue";
import { Drawer } from "@/definitions/types";

@Component({
  components: {
    NavMenuItem,
    NavMenu: () => import("@/components/navigation/NavMenu.vue"),
  },
})
export default class extends Vue {
  @Prop({ default: () => [] }) readonly drawers!: Drawer[];
  @Prop({ default: 0 }) readonly depth!: number;
}
</script>
