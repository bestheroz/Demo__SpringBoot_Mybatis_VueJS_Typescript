<template>
  <div class="w-full">
    <page-title
      button-icon="mdi-content-save"
      button-text="저장"
      @click="saveItems"
      :hide-button="role.id === $store.getters.roleId"
    />
    <role-chips v-model="role" />
    <v-divider />
    <role-menu-list ref="refRoleMenuList" :role-id="role.id" />
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import RoleMenuList from "@/views/management/role/menu/RoleMenuList.vue";
import PageTitle from "@/components/title/PageTitle.vue";
import RoleChips from "@/views/management/role/RoleChips.vue";
import { Role } from "@/definitions/models";
import { defaultRole } from "@/definitions/defaults";

@Component({
  components: {
    RoleChips,
    PageTitle,
    RoleMenuList,
  },
})
export default class extends Vue {
  @Ref() readonly refRoleMenuList!: RoleMenuList;

  role: Role = defaultRole();

  protected async saveItems(): Promise<void> {
    await this.refRoleMenuList.saveItems();
  }
}
</script>
