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
import { Component, Prop, Vue } from "vue-property-decorator";
import { deleteApi, getApi, postApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import MenuEditDialog from "@/views/management/menu/MenuEditDialog.vue";
import { defaultMenu } from "@/definitions/defaults";
import { cloneDeep } from "lodash-es";
import type { Menu } from "@/definitions/models";
import PageTitle from "@/components/title/PageTitle.vue";
import MenuNestedDraggable from "@/views/management/menu/MenuNestedDraggable.vue";

@Component({
  components: {
    MenuNestedDraggable,
    PageTitle,
    MenuEditDialog,
  },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;

  items: Menu[] = [];
  loading = false;
  saving = false;
  drag = false;

  editItem: Menu = defaultMenu();
  dialog = false;

  protected mounted(): void {
    this.getList();
  }

  public async getList(): Promise<void> {
    this.items = [];
    this.loading = true;
    const response = await getApi<Menu[]>("menus/");
    this.loading = false;
    this.items = response.data || [];
  }

  protected onCreated(value: Menu): void {
    this.items = [...this.items, value];
  }

  protected onUpdated(value: Menu): void {
    this.changeUpdateItemRecursive(this.items, value);
  }

  protected changeUpdateItemRecursive(items: Menu[], value: Menu): boolean {
    if (items.some((item) => item.id === value.id)) {
      items.splice(
        items.findIndex((item) => item.id === value.id),
        1,
        value,
      );
      return true;
    } else {
      for (const item of items) {
        if (this.changeUpdateItemRecursive(item.children, value)) {
          return true;
        }
      }
      return false;
    }
  }

  public showAddDialog(): void {
    this.editItem = defaultMenu();
    this.dialog = true;
  }
  protected showEditDialog(value: Menu): void {
    this.editItem = cloneDeep(value);
    this.dialog = true;
  }

  protected async onDelete(value: Menu): Promise<void> {
    const result = await confirmDelete(`메뉴명: ${value.name}`);
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<Menu>(`menus/${value.id}`);
      this.saving = false;
      if (response.code.startsWith("S")) {
        await this.$store.dispatch("reloadRole");
        this.items = this.items.filter((item) => item.id !== value.id);
      }
    }
  }

  public async saveAll(): Promise<void> {
    this.saving = true;
    const response = await postApi<Menu[]>("menus/save-all/", this.items);
    this.saving = false;
    if (response.code.startsWith("S")) {
      await this.$store.dispatch("reloadRole");
      this.items = response.data || [];
    }
  }
}
</script>
