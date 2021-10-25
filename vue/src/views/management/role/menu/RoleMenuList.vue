<template>
  <div>
    <v-card flat :loading="loading">
      <v-card-text>
        <v-row dense>
          <v-col sm="12" md="6">
            <role-menu-nested-draggable v-model="items" :role-id="roleId" />
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
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import { getApi, postApi } from "@/utils/apis";
import draggable from "vuedraggable";
import type { Menu, RoleMenuMap } from "@/definitions/models";
import { MENU_TYPE } from "@/definitions/selections";
import RoleMenuMenuItem from "@/views/management/role/menu/RoleMenuMenuItem.vue";
import RoleMenuNestedDraggable from "@/views/management/role/menu/RoleMenuNestedDraggable.vue";
import { defaultRoleMenuMap } from "@/definitions/defaults";

@Component({
  components: { RoleMenuNestedDraggable, RoleMenuMenuItem, draggable },
})
export default class extends Vue {
  @Prop({ required: true }) roleId!: number;

  items: RoleMenuMap[] = [];
  menus: Menu[] = [];
  selected: number[] = [];
  loading = false;
  saving = false;
  drag = false;

  get dragOptions(): { animation: number } {
    return {
      animation: 200,
    };
  }

  get flattMenus(): Menu[] {
    return this.getMenusFromChildren(this.menus);
  }

  get flatItems(): RoleMenuMap[] {
    return this.getItemsWithChildren(this.items);
  }

  @Watch("roleId")
  protected async watchRoleId(val: number): Promise<void> {
    this.menus = await this.getMenus(val);

    if (this.menus.length === 0 || !val) {
      this.items = [];
      this.selected = [];
      return;
    }
    this.loading = true;
    const response = await getApi<RoleMenuMap[]>(`roles/${val}/maps/`);
    this.loading = false;
    this.items = response.data;
    this.selected =
      this.flatItems
        .filter((item) =>
          this.flattMenus.some((menu) => menu.id === item.menu.id),
        )
        .map((item) => item.menu.id || 0) || [];
  }

  protected async getMenus(roleId: number): Promise<Menu[]> {
    if (this.roleId === this.$store.getters.roleId) {
      const response = await getApi<Menu[]>(`menus/?roleId=${roleId}`);
      return response.data || [];
    } else {
      const response = await getApi<Menu[]>(`menus/?childRoleId=${roleId}`);
      return response.data || [];
    }
  }
  protected onChangeSelectedChip(selected: number[]): void {
    // 삭제!
    this.items = this.removeNotSelectedChildrenItem(selected, this.items);

    for (const selectedId of selected.filter((selectedId) =>
      this.flatItems.every((item) => item.menu.id !== selectedId),
    )) {
      const foundMenu = this.flattMenus.find((m) => m.id === selectedId);
      if (foundMenu) {
        this.items = [
          ...this.items,
          {
            ...(this.items.find((item) => item.menu.id === selectedId) || {
              ...defaultRoleMenuMap(),
              menu: { ...foundMenu, children: [] },
            }),
          } as RoleMenuMap,
        ];
      }
    }
  }

  protected removeNotSelectedChildrenItem(
    selected: number[],
    items: RoleMenuMap[],
  ): RoleMenuMap[] {
    const filtered = items.filter((item) =>
      selected.includes(item.menu.id || 0),
    );
    for (const item of filtered.filter(
      (item) => item.menu.type === MENU_TYPE.GROUP,
    )) {
      item.children = this.removeNotSelectedChildrenItem(
        selected,
        item.children || [],
      );
    }
    return filtered;
  }

  public async saveItems(): Promise<void> {
    this.saving = true;
    const response = await postApi<RoleMenuMap[]>(
      `roles/${this.roleId}/maps/save-all/`,
      this.items,
    );
    this.saving = false;
    if (response.code.startsWith("S") && response.data) {
      this.items = response.data;
      await this.$store.dispatch("reloadRole");
    }
  }

  protected getMenusFromChildren(menus: Menu[]): Menu[] {
    let result = [] as Menu[];
    for (const menu of menus) {
      if (menu.type === MENU_TYPE.GROUP) {
        result = [...result, menu, ...this.getMenusFromChildren(menu.children)];
      } else {
        result = [...result, menu];
      }
    }
    return result;
  }

  protected getItemsWithChildren(roleMenus: RoleMenuMap[]): RoleMenuMap[] {
    let result = [] as RoleMenuMap[];
    for (const roleMenu of roleMenus) {
      if (roleMenu.menu.type === MENU_TYPE.GROUP) {
        result = [
          ...result,
          roleMenu,
          ...this.getItemsWithChildren(roleMenu.children || []),
        ];
      } else {
        result = [...result, roleMenu];
      }
    }
    return result;
  }
}
</script>
