<template>
  <div :class="`offset-${depth}`">
    <div
      v-for="menu in menus"
      :key="menu.id"
      :class="menu.type === MENU_TYPE.GROUP ? '' : 'd-inline-block'"
    >
      <v-chip
        :value="menu.id"
        :outlined="!vModel.includes(menu.id)"
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
        v-model="vModel"
        :menus="menu.children"
        :depth="depth + 1"
        :disabled="disabled"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { MENU_TYPE } from "@/definitions/selections";
import { Menu } from "@/definitions/models";
import {
  defineComponent,
  PropType,
  reactive,
  toRefs,
} from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  name: "RoleMenuMenuItem",
  props: {
    value: {
      type: Array as PropType<number[]>,
      required: true,
    },
    menus: {
      type: Array as PropType<Menu[]>,
      required: true,
    },
    depth: {
      type: Number,
      default: 0,
    },
    disabled: {
      type: Boolean,
    },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<number[]>(props, emit);
    const state = reactive({ MENU_TYPE: MENU_TYPE });
    return { ...vModel, ...toRefs(state) };
  },
});
</script>
