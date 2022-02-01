<template>
  <div>
    <page-title @click="showAddDialog" :button-loading="saving">
      <template #more-buttons>
        <v-btn
          @click="saveAll"
          color="primary"
          outlined
          x-large
          v-if="$store.getters.writeAuthority"
        >
          <v-icon> mdi-sort </v-icon> 순서저장
        </v-btn>
        <v-btn @click="getList" color="primary" outlined x-large>
          <v-icon> mdi-refresh </v-icon> 새로고침
        </v-btn>
      </template>
    </page-title>
    <v-card :loading="loading || saving">
      <v-card-text>
        <menu-nested-draggable
          v-model="items"
          @click:edit="(item) => showEditDialog(item)"
          @click:delete="(item) => onDelete(item)"
        />
      </v-card-text>
    </v-card>
    <menu-edit-dialog
      v-model="editItem"
      :dialog.sync="dialog"
      @created="onCreated"
      @updated="onUpdated"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { deleteApi, getApi, postApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import MenuEditDialog from "@/views/management/menu/MenuEditDialog.vue";
import { defaultMenu } from "@/definitions/defaults";
import { cloneDeep } from "lodash-es";
import type { Menu } from "@/definitions/models";
import PageTitle from "@/components/title/PageTitle.vue";
import MenuNestedDraggable from "@/views/management/menu/MenuNestedDraggable.vue";
import {
  defineComponent,
  onMounted,
  reactive,
  toRefs,
} from "@vue/composition-api";
import setupList from "@/composition/setupList";
import setupListDialog from "@/composition/setupListDialog";
import setupReadonly from "@/composition/setupReadonly";
import store from "@/store";

export default defineComponent({
  components: { MenuNestedDraggable, PageTitle, MenuEditDialog },
  props: {
    height: {
      type: [Number, String],
      default: undefined,
    },
  },
  setup() {
    const listDialog = setupListDialog<Menu>(defaultMenu);
    const list = setupList<Menu>("menus/");

    const state = reactive({
      saving: false,
      drag: false,
    });

    const methods = {
      getList: async (): Promise<void> => {
        list.items.value = [];
        list.loading.value = true;
        const response = await getApi<Menu[]>("menus/");
        list.loading.value = false;
        list.items.value = response.data || [];
      },

      onCreated: (value: Menu): void => {
        list.items.value = [...list.items.value, value];
      },

      onUpdated: (value: Menu): void => {
        methods.changeUpdateItemRecursive(list.items.value, value);
      },

      changeUpdateItemRecursive: (items: Menu[], value: Menu): boolean => {
        if (items.some((item) => item.id === value.id)) {
          items.splice(
            items.findIndex((item) => item.id === value.id),
            1,
            value,
          );
          return true;
        } else {
          for (const item of items) {
            if (methods.changeUpdateItemRecursive(item.children, value)) {
              return true;
            }
          }
          return false;
        }
      },

      showAddDialog: (): void => {
        listDialog.editItem.value = defaultMenu();
        listDialog.dialog.value = true;
      },
      showEditDialog: (value: Menu): void => {
        listDialog.editItem.value = cloneDeep(value);
        listDialog.dialog.value = true;
      },

      onDelete: async (value: Menu): Promise<void> => {
        const result = await confirmDelete(`메뉴명: ${value.name}`);
        if (result.value) {
          state.saving = true;
          const response = await deleteApi<Menu>(`menus/${value.id}`);
          state.saving = false;
          if (response.success) {
            store.dispatch("reloadRole").then();
            list.items.value = methods.deleteWithRecursiveChildren(
              list.items.value,
              value,
            );
          }
        }
      },

      deleteWithRecursiveChildren: (menus: Menu[], menu: Menu): Menu[] => {
        return menus
          .filter((item) => item.id !== menu.id)
          .map((m) => {
            m.children = methods.deleteWithRecursiveChildren(m.children, menu);
            return m;
          });
      },

      saveAll: async (): Promise<void> => {
        state.saving = true;
        const response = await postApi<Menu[]>(
          "menus/save-all/",
          list.items.value,
        );
        state.saving = false;
        if (response.success) {
          store.dispatch("reloadRole").then();
          list.items.value = response.data || [];
        }
      },
    };
    onMounted(() => {
      methods.getList();
    });
    return {
      ...listDialog,
      ...list,
      ...setupReadonly(),
      ...toRefs(state),
      ...methods,
    };
  },
});
</script>
