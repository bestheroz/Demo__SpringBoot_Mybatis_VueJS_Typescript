<template>
  <div>
    <v-card flat :loading="loading">
      <v-row dense>
        <v-col sm="12" md="6">
          <role-menu-nested-draggable
            v-model="items"
            :role-id="roleId"
            v-if="roleId"
          />
        </v-col>
        <v-col sm="12" md="6">
          <v-card-text class="py-0 elevation-1">
            <v-chip-group
              v-model="selected"
              multiple
              column
              @change="onChangeSelectedChip"
            >
              <role-menu-menu-item
                v-model="selected"
                :menus="menus"
                :disabled="
                  $store.getters.writeAuthority &&
                  roleId === $store.getters.roleId
                "
              />
            </v-chip-group>
          </v-card-text>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script lang="ts">
import { getApi, postApi } from "@/utils/apis";
import type { Menu, RoleMenuMap } from "@/definitions/models";
import { MENU_TYPE } from "@/definitions/selections";
import RoleMenuMenuItem from "@/views/management/role/menu/RoleMenuMenuItem.vue";
import RoleMenuNestedDraggable from "@/views/management/role/menu/RoleMenuNestedDraggable.vue";
import { defaultRoleMenuMap } from "@/definitions/defaults";
import {
  computed,
  defineComponent,
  reactive,
  toRefs,
  watch,
} from "@vue/composition-api";
import setupList from "@/composition/setupList";
import setupReadonly from "@/composition/setupReadonly";
import store from "@/store";

export default defineComponent({
  components: { RoleMenuNestedDraggable, RoleMenuMenuItem },
  props: {
    roleId: { type: Number, default: undefined },
  },
  setup(props) {
    const list = setupList<RoleMenuMap>(`roles/${props.roleId}/maps/`);

    const state = reactive({
      menus: [] as Menu[],
      selected: [] as number[],
    });

    const computes = {
      flattMenus: computed((): Menu[] =>
        methods.getMenusFromChildren(state.menus),
      ),
      flatItems: computed((): RoleMenuMap[] =>
        methods.getItemsWithChildren(list.items.value),
      ),
    };

    const methods = {
      getList: async (): Promise<void> => {
        state.menus = await methods.getMenus(props.roleId);

        if (state.menus.length === 0 || !props.roleId) {
          list.items.value = [];
          state.selected = [];
          return;
        }
        list.loading.value = true;
        const response = await getApi<RoleMenuMap[]>(
          `roles/${props.roleId}/maps/`,
        );
        list.loading.value = false;
        list.items.value = response.data;
        state.selected =
          computes.flatItems.value
            .filter((item) =>
              computes.flattMenus.value.some(
                (menu) => menu.id === item.menu.id,
              ),
            )
            .map((item) => item.menu.id || 0) || [];
      },

      getMenus: async (roleId: number): Promise<Menu[]> => {
        if (props.roleId === store.getters.roleId) {
          const response = await getApi<Menu[]>(`menus/?roleId=${roleId}`);
          return response.data || [];
        } else {
          const response = await getApi<Menu[]>(`menus/?childRoleId=${roleId}`);
          return response.data || [];
        }
      },
      onChangeSelectedChip: (selected: number[]): void => {
        // 삭제!
        list.items.value = methods.removeNotSelectedChildrenItem(
          selected,
          list.items.value,
        );

        for (const selectedId of selected.filter((selectedId) =>
          computes.flatItems.value.every((item) => item.menu.id !== selectedId),
        )) {
          const foundMenu = computes.flattMenus.value.find(
            (m) => m.id === selectedId,
          );
          if (foundMenu) {
            list.items.value = [
              ...list.items.value,
              {
                ...(list.items.value.find(
                  (item) => item.menu.id === selectedId,
                ) || {
                  ...defaultRoleMenuMap(),
                  menu: { ...foundMenu, children: [] },
                }),
              } as RoleMenuMap,
            ];
          }
        }
      },

      removeNotSelectedChildrenItem: (
        selected: number[],
        items: RoleMenuMap[],
      ): RoleMenuMap[] => {
        const filtered = items.filter((item) =>
          selected.includes(item.menu.id || 0),
        );
        for (const item of filtered.filter(
          (item) => item.menu.type === MENU_TYPE.GROUP,
        )) {
          item.children = methods.removeNotSelectedChildrenItem(
            selected,
            item.children || [],
          );
        }
        return filtered;
      },

      saveItems: async (): Promise<void> => {
        const response = await postApi<RoleMenuMap[]>(
          `roles/${props.roleId}/maps/save-all/`,
          list.items.value,
        );
        if (response.success && response.data) {
          list.items.value = response.data;
          if (props.roleId === store.getters.roleId) {
            store.dispatch("reloadRole").then();
          }
        }
      },

      getMenusFromChildren: (menus: Menu[]): Menu[] => {
        let result = [] as Menu[];
        for (const menu of menus) {
          if (menu.type === MENU_TYPE.GROUP) {
            result = [
              ...result,
              menu,
              ...methods.getMenusFromChildren(menu.children),
            ];
          } else {
            result = [...result, menu];
          }
        }
        return result;
      },

      getItemsWithChildren: (roleMenus: RoleMenuMap[]): RoleMenuMap[] => {
        let result = [] as RoleMenuMap[];
        for (const roleMenu of roleMenus) {
          if (roleMenu.menu.type === MENU_TYPE.GROUP) {
            result = [
              ...result,
              roleMenu,
              ...methods.getItemsWithChildren(roleMenu.children || []),
            ];
          } else {
            result = [...result, roleMenu];
          }
        }
        return result;
      },
    };

    watch(
      () => props.roleId,
      (val: number) => {
        if (!val) {
          return;
        }
        methods.getList();
      },
      {
        immediate: true,
      },
    );

    return {
      ...list,
      ...setupReadonly(),
      ...toRefs(state),
      ...computes,
      ...methods,
    };
  },
});
</script>
