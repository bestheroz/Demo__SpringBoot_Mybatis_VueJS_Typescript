<template>
  <div>
    <page-title
      @click="showAddDialog"
      :more-actions="$store.getters.writeAuthority"
    >
      <template #list>
        <v-list>
          <v-list-item>
            <v-btn @click="saveAll">
              <v-icon class="drag-handle"> mdi-sort </v-icon> 순서저장
            </v-btn>
          </v-list-item>
        </v-list>
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
          v-else
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
import { Component, Prop, Vue } from "vue-property-decorator";
import { deleteApi, getApi, postApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import RoleEditDialog from "@/views/management/role/RoleEditDialog.vue";
import { defaultRole } from "@/definitions/defaults";
import { cloneDeep } from "lodash-es";
import type { Role } from "@/definitions/models";
import PageTitle from "@/components/title/PageTitle.vue";
import RoleNestedDraggable from "@/views/management/role/RoleNestedDraggable.vue";

@Component({
  components: {
    RoleNestedDraggable,
    PageTitle,
    RoleEditDialog,
  },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;

  items: Role[] = this.$store.getters.isSuperAdmin ? [] : [defaultRole()];
  loading = false;
  saving = false;
  drag = false;

  editItem: Role = defaultRole();
  dialog = false;

  protected mounted(): void {
    this.getList();
  }

  public async getList(): Promise<void> {
    this.items = this.$store.getters.isSuperAdmin ? [] : [defaultRole()];
    this.loading = true;
    let response;
    if (this.$store.getters.isSuperAdmin) {
      response = await getApi<Role[]>("roles/");
    } else {
      response = await getApi<Role[]>("mine/roles/");
    }
    this.loading = false;
    this.items = response.data || [];
  }

  protected onCreated(): void {
    this.getList().then();
  }

  protected onUpdated(): void {
    this.getList().then();
  }

  public showAddDialog(): void {
    this.editItem = defaultRole();
    this.dialog = true;
  }
  protected showEditDialog(value: Role): void {
    this.editItem = cloneDeep(value);
    this.dialog = true;
  }

  protected async onDelete(value: Role): Promise<void> {
    const result = await confirmDelete(`역할명: ${value.name}`);
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<Role>(`roles/${value.id}`);
      this.saving = false;
      if (response.code.startsWith("S")) {
        this.getList().then();
      }
    }
  }

  public async saveAll(): Promise<void> {
    this.saving = true;
    const response = await postApi<Role[]>("roles/save-all/", this.items);
    this.saving = false;
    if (response.code.startsWith("S")) {
      this.items = response.data || [];
    }
  }
}
</script>
