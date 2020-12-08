<template>
  <div>
    <v-card flat>
      <button-set
        add-button
        delete-button
        reload-button
        :delete-disabled="!selected || selected.length === 0"
        @click:add="addItem"
        @click:delete="deleteItem"
        @click:reload="getList"
      />
      <v-card-title class="datatable-title">
        <v-icon> mdi-format-list-checkbox </v-icon>
        코드 관리 - Master
      </v-card-title>
      <v-card-text class="py-0">
        <v-data-table
          v-model="selected"
          must-sort
          fixed-header
          :loading="loading"
          :headers="headers"
          :items="filteredItems"
          :sort-by="sortBy"
          :sort-desc="sortDesc"
          item-key="codeGroup"
          single-select
          show-select
          dense
          :height="height"
          :footer-props="envs.FOOTER_PROPS_MAX_1000"
        >
          <template #header>
            <data-table-filter
              :header="headers"
              :output.sync="filteredItems"
              :input="items"
            />
          </template>
          <template #[`item.codeGroup`]="{ item }">
            <a class="text--anchor" @click="editItem(item)">
              {{ item.codeGroup }}
            </a>
          </template>
          <template #[`item.updated`]="{ item }">
            {{ item.updated | formatDatetime }}
          </template>
          <template #[`item.updatedBy`]="{ item }">
            {{ item.updatedBy | formatMemberNm }}
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <code-group-edit-dialog
      :item="item"
      :dialog.sync="dialog"
      @finished="getList"
    />
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";
import type { DataTableHeader, TableCodeGroupEntity } from "@/common/types";
import { deleteApi, getApi } from "@/utils/apis";
import envs from "@/constants/envs";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import CodeGroupEditDialog from "@/views/admin/code/components/CodeGroupEditDialog.vue";
import { confirmDelete } from "@/utils/alerts";

@Component({
  name: "CodeGroupList",
  components: {
    CodeGroupEditDialog,
    ButtonSet,
    DataTableFilter,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  readonly envs: typeof envs = envs;
  sortBy: string[] = ["codeGroup"];
  sortDesc: boolean[] = [false];
  items: TableCodeGroupEntity[] = [];
  filteredItems: TableCodeGroupEntity[] = [];
  loading = false;
  selected: TableCodeGroupEntity[] = [];
  dialog = false;
  item: TableCodeGroupEntity = Object.create(null);

  get headers(): DataTableHeader[] {
    return [
      {
        text: "그룹코드",
        align: "start",
        value: "codeGroup",
      },
      {
        text: "그룹코드명",
        align: "start",
        value: "name",
      },
      {
        text: "작업 일시",
        align: "center",
        value: "updated",
        filterable: false,
        width: "10rem",
      },
      {
        text: "작업자",
        align: "start",
        value: "updatedBy",
        filterable: false,
        width: "7rem",
      },
    ];
  }

  @Watch("selected")
  @Emit()
  selectRow(val: TableCodeGroupEntity[]): TableCodeGroupEntity {
    return val && val.length > 0 ? val[0] : {};
  }

  public async getList(): Promise<void> {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableCodeGroupEntity[]>("admin/codeGroups/");
    this.loading = false;
    this.items = response?.data || [];
  }

  protected addItem(): void {
    this.item = Object.create(null);
    this.dialog = true;
  }

  protected editItem(value: TableCodeGroupEntity): void {
    this.item = { ...value };
    this.dialog = true;
  }

  protected async deleteItem(): Promise<void> {
    this.item = this.selected[0];
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteApi<TableCodeGroupEntity>(
        `admin/codeGroups/${this.item.codeGroup}/`,
      );
      this.loading = false;
      if (response?.code?.startsWith("S")) {
        this.getList().then();
      }
    }
  }
}
</script>
