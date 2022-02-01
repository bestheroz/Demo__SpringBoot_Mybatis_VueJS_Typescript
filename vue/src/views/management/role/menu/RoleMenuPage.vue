<template>
  <div class="w-full">
    <page-title
      button-icon="mdi-content-save"
      button-text="저장"
      :button-loading="saving"
      @click="saveItems"
      :hide-button="roleId === $store.getters.roleId"
    >
      <template #more-buttons>
        <v-btn
          @click="$refs.refRoleMenuList.getList()"
          color="primary"
          outlined
          x-large
        >
          <v-icon> mdi-refresh </v-icon> 새로고침
        </v-btn>
      </template>
    </page-title>
    <v-card>
      <v-card-text>
        <data-table-filter :filters="filters" :output.sync="filterOutput" />
        <role-menu-list ref="refRoleMenuList" :role-id="roleId" />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import RoleMenuList from "@/views/management/role/menu/RoleMenuList.vue";
import PageTitle from "@/components/title/PageTitle.vue";
import { Role } from "@/definitions/models";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import { Filter, FilterOutput } from "@/definitions/types";
import { getApi } from "@/utils/apis";
import {
  computed,
  defineComponent,
  onBeforeMount,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";

export default defineComponent({
  components: { DataTableFilter, PageTitle, RoleMenuList },
  setup() {
    const state = reactive({
      roles: [] as Role[],
      saving: false,
      filterOutput: {} as FilterOutput,
    });
    const computes = {
      filters: computed((): Filter[] => [
        {
          type: "checkbox",
          text: "타입",
          key: "roleId",
          single: true,
          required: true,
          items: state.roles.map((v, index) => {
            return { value: v.id || 0, text: v.name, checked: index === 0 };
          }),
        },
      ]),
      roleId: computed((): number => state.filterOutput.roleId?.[0] as number),
    };

    const methods = {
      saveItems: async (): Promise<void> => {
        state.saving = true;
        await refRoleMenuList.value?.saveItems();
        state.saving = false;
      },
      getRoles: async (): Promise<void> => {
        const response = await getApi<Role[]>("mine/roles/selections/");
        state.roles = (response.data || []).filter((r) => r.id !== 1);
      },
    };
    onBeforeMount(() => {
      methods.getRoles().then();
    });
    const refRoleMenuList = ref<null | InstanceType<typeof RoleMenuList>>(null);
    return { ...toRefs(state), ...computes, ...methods, refRoleMenuList };
  },
});
</script>
