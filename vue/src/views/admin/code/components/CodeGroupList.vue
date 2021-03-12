<template>
  <div>
    <v-card flat>
      <button-set
        :loading="saving"
        add-button
        @click:add="dialog = true"
        delete-button
        :delete-disabled="!selected || selected.length === 0"
        @click:delete="deleteItem"
        reload-button
        @click:reload="getList"
      />
      <v-card-text>
        <v-data-table
          v-model="selected"
          must-sort
          fixed-header
          :loading="loading"
          :headers="headers"
          :items="items"
          :options.sync="pagination"
          item-key="codeGroup"
          single-select
          show-select
          dense
          :height="height"
          :footer-props="envs.FOOTER_PROPS_MAX_1000"
        >
          <template #header>
            <data-table-filter
              :headers="headers"
              :filter.sync="datatableFilter"
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
      v-model="item"
      :dialog.sync="dialog"
      @finished="getList"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";
import type { DataTableHeader, TableCodeGroupEntity } from "@/common/types";
import { Pagination } from "@/common/types";
import { deleteApi, getApi } from "@/utils/apis";
import envs from "@/constants/envs";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import CodeGroupEditDialog from "@/views/admin/code/components/CodeGroupEditDialog.vue";
import { confirmDelete } from "@/utils/alerts";
import qs from "qs";
import { defaultTableCodeGroupEntity } from "@/common/values";

@Component({
  name: "CodeGroupList",
  components: {
    CodeGroupEditDialog,
    ButtonSet,
    DataTableFilter,
  },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;
  readonly envs: typeof envs = envs;
  selected: TableCodeGroupEntity[] = [];
  pagination: Pagination = {
    page: 1,
    sortBy: ["codeGroup"],
    sortDesc: [true],
    itemsPerPage: 20,
  };
  items: TableCodeGroupEntity[] = [];
  loading = false;
  saving = false;
  dialog = false;
  datatableFilter: { [p: string]: string | number } = {};
  item: TableCodeGroupEntity = defaultTableCodeGroupEntity();

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

  get queryString(): string {
    return qs.stringify({
      filter: this.datatableFilter,
      ...this.pagination,
    });
  }

  @Watch("selected")
  @Emit()
  selectRow(val: TableCodeGroupEntity[]): TableCodeGroupEntity {
    return val && val.length > 0 ? val[0] : defaultTableCodeGroupEntity();
  }

  @Watch("queryString")
  public async getList(): Promise<void> {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableCodeGroupEntity[]>("admin/code/groups/");
    this.loading = false;
    this.items = response?.data || [];
  }

  protected editItem(value: TableCodeGroupEntity): void {
    this.item = { ...value };
    this.dialog = true;
  }

  protected async deleteItem(): Promise<void> {
    this.item = this.selected[0];
    const result = await confirmDelete();
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<TableCodeGroupEntity>(
        `admin/code/groups/${this.item.codeGroup}/`,
      );
      this.saving = false;
      if (response?.code?.startsWith("S")) {
        this.getList().then();
      }
    }
  }
}
</script>
