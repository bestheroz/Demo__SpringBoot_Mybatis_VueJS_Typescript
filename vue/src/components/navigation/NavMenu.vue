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
import NavMenuItem from "@/components/navigation/NavMenuItem.vue";
import { Drawer } from "@/definitions/types";
import { defineComponent, PropType } from "@vue/composition-api";

export default defineComponent({
  name: "NavMenu",
  components: { NavMenuItem },
  props: {
    drawers: { type: Array as PropType<Drawer[]>, default: () => [] },
    depth: { type: Number, default: 0 },
  },
});
</script>
