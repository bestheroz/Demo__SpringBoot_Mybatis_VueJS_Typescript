<template>
  <div class="w-full">
    <page-title
      button-icon="mdi-content-save"
      button-text="저장"
      :button-loading="saving"
      @click="saveItems"
      :hide-button="roleId === $store.getters.roleId"
    />
    <v-card>
      <v-card-text>
        <data-table-filter :filters="filters" :output.sync="filterOutput" />
        <role-menu-list ref="refRoleMenuList" :role-id="roleId" />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import RoleMenuList from "@/views/management/role/menu/RoleMenuList.vue";
import PageTitle from "@/components/title/PageTitle.vue";
import { Role } from "@/definitions/models";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import { Filter } from "@/definitions/types";
import { getApi } from "@/utils/apis";

@Component({
  components: {
    DataTableFilter,
    PageTitle,
    RoleMenuList,
  },
})
export default class extends Vue {
  @Ref() readonly refRoleMenuList!: RoleMenuList;

  roles: Role[] = [];
  saving = false;
  filterOutput: Record<string, string | number | boolean[]> = {};

  get filters(): Filter[] {
    return [
      {
        type: "checkbox",
        text: "타입",
        key: "roleId",
        single: true,
        required: true,
        items: this.roles.map((v, index) => {
          return { value: v.id || 0, text: v.name, checked: index === 0 };
        }),
      },
    ];
  }

  get roleId(): string {
    return this.filterOutput.roleId as string;
  }

  protected created(): void {
    this.getRoles().then();
  }

  protected async saveItems(): Promise<void> {
    this.saving = true;
    await this.refRoleMenuList.saveItems();
    this.saving = false;
  }

  protected async getRoles(): Promise<void> {
    const response = await getApi<Role[]>("mine/roles/selections/");
    this.roles = (response.data || []).filter((r) => r.id !== 1);
  }
}
</script>
