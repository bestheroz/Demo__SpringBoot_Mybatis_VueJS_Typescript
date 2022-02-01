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
        <a
          class="orange--text"
          v-text="`${items[0].name}(Yours)`"
          v-if="!$store.getters.isSuperAdmin"
        />
        <role-nested-draggable
          v-model="items"
          @click:edit="(item) => showEditDialog(item)"
          @click:delete="(item) => onDelete(item)"
          v-if="$store.getters.isSuperAdmin"
        />
        <role-nested-draggable
          v-model="items[0].children"
          @click:edit="(item) => showEditDialog(item)"
          @click:delete="(item) => onDelete(item)"
          v-else-if="items && items.length > 0"
        />
      </v-card-text>
    </v-card>
    <role-edit-dialog
      v-model="editItem"
      :dialog.sync="dialog"
      @created="onCreated"
      @updated="onUpdated"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { deleteApi, postApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import RoleEditDialog from "@/views/management/role/RoleEditDialog.vue";
import { defaultRole } from "@/definitions/defaults";
import type { Role } from "@/definitions/models";
import PageTitle from "@/components/title/PageTitle.vue";
import RoleNestedDraggable from "@/views/management/role/RoleNestedDraggable.vue";
import {
  defineComponent,
  onMounted,
  reactive,
  toRefs,
} from "@vue/composition-api";
import setupList from "@/composition/setupList";
import setupListDialog from "@/composition/setupListDialog";
import store from "@/store";

export default defineComponent({
  components: { RoleNestedDraggable, PageTitle, RoleEditDialog },
  props: {
    height: {
      type: [Number, String],
      default: undefined,
    },
  },
  setup() {
    const list = store.getters.isSuperAdmin
      ? setupList<Role>("roles/")
      : setupList<Role>("mine/roles/");
    const listDialog = setupListDialog<Role>(defaultRole);

    const state = reactive({
      saving: false,
      drag: false,
    });
    const methods = {
      onCreated: (): void => {
        list.getList();
      },
      onUpdated: (): void => {
        list.getList();
      },
      onDelete: async (value: Role): Promise<void> => {
        const result = await confirmDelete(`역할명: ${value.name}`);
        if (result.value) {
          state.saving = true;
          const response = await deleteApi<Role>(`roles/${value.id}`);
          state.saving = false;
          if (response.success) {
            list.getList();
          }
        }
      },
      saveAll: async (): Promise<void> => {
        state.saving = true;
        const response = await postApi<Role[]>(
          "roles/save-all/",
          list.items.value,
        );
        state.saving = false;
        if (response.success) {
          list.items.value = response.data || [];
        }
      },
    };

    onMounted(() => {
      list.getList();
    });

    return { ...list, ...listDialog, ...toRefs(state), ...methods };
  },
});
</script>
