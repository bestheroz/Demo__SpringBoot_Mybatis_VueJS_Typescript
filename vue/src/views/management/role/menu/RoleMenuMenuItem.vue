<template>
  <div :class="`w-full offset-${depth}`">
    <div
      v-for="menu in menus"
      :key="menu.id"
      :class="menu.type === MENU_TYPE.GROUP ? '' : 'd-inline-block'"
    >
      <v-chip
        :key="menu.id"
        :value="menu.id"
        :outlined="!selected.includes(menu.id)"
        color="primary"
        class="px-4"
        label
        :disabled="disabled || !$store.getters.writeAuthority"
      >
        <v-icon
          v-text="menu.icon"
          v-if="menu.type === MENU_TYPE.GROUP"
          class="pr-2"
          size="24"
        />
        {{ menu.name }}
      </v-chip>
      <role-menu-menu-item
        v-model="selected"
        :menus="menu.children"
        :depth="depth + 1"
        :disabled="disabled"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, VModel, Vue } from "vue-property-decorator";
import type { Menu } from "@/definitions/models";
import { MENU_TYPE } from "@/definitions/selections";

@Component({
  components: {
    RoleMenuMenuItem: () =>
      import("@/views/management/role/menu/RoleMenuMenuItem.vue"),
  },
})
export default class extends Vue {
  @VModel({ required: true }) selected!: number[];
  @Prop({ required: true }) readonly menus!: Menu[];
  @Prop({ default: 0 }) readonly depth!: number;
  @Prop({ type: Boolean }) readonly disabled!: boolean;

  readonly MENU_TYPE = MENU_TYPE;
}
</script>
