<template>
  <div>
    <button-set
      :loading="saving"
      add-button
      @click:add="dialog = true"
      delete-button
      :delete-disabled="!selected || selected.length === 0"
      @click:delete="deleteItem"
      reload-button
      @click:reload="getList"
      excel-button
      @click:excel="excel"
    />
    <v-card flat>
      <v-card-text class="pb-0">
        <v-data-table
          v-model="selected"
          must-sort
          fixed-header
          :loading="loading"
          :headers="headers"
          :items="items"
          :options.sync="pagination"
          item-key="id"
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
          <template #[`item.id`]="{ item }">
            <a
              class="text--anchor"
              @click="editItem({ ...item, password: undefined })"
            >
              {{ item.id }}
            </a>
          </template>
          <template #[`item.available`]="{ item }">
            <span style="display: inline-flex">
              <v-checkbox
                readonly
                :input-value="item.available"
                :ripple="false"
                dense
                hide-details
                class="mt-0"
              />
            </span>
          </template>
          <template v-if="AUTHORITY" #[`item.authority`]="{ item }">
            {{ item.authority | getCodeText(AUTHORITY) }}
          </template>
          <template #[`item.expired`]="{ item }">
            {{ item.expired | formatDatetime }}
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
    <member-edit-dialog
      v-model="item"
      :dialog.sync="dialog"
      @finished="getList"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import type {
  DataTableHeader,
  Pagination,
  SelectItem,
  TableMemberEntity,
} from "@/common/types";
import { deleteApi, getApi, getCodesApi, getExcelApi } from "@/utils/apis";
import envs from "@/constants/envs";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import MemberEditDialog from "@/views/admin/member/components/MemberEditDialog.vue";
import { confirmDelete } from "@/utils/alerts";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import qs from "qs";
import { defaultTableMemberEntity } from "@/common/values";

@Component({
  name: "MemberList",
  components: {
    DataTableFilter,
    MemberEditDialog,
    ButtonSet,
  },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;
  readonly envs: typeof envs = envs;
  selected: TableMemberEntity[] = [];
  pagination: Pagination = {
    page: 1,
    sortBy: ["authority"],
    sortDesc: [true],
    itemsPerPage: 20,
  };

  items: TableMemberEntity[] = [];
  loading = false;
  saving = false;
  item: TableMemberEntity = defaultTableMemberEntity();
  AUTHORITY: SelectItem[] = [];
  dialog = false;
  datatableFilter: { [p: string]: string | number } = {};

  get headers(): DataTableHeader[] {
    return [
      {
        text: "사용자아이디",
        align: "start",
        value: "id",
      },
      {
        text: "사용자명",
        align: "start",
        value: "name",
      },
      {
        text: "권한",
        align: "center",
        value: "authority",
        filterType: "select",
        filterSelectItem: this.AUTHORITY,
        width: "8rem",
      },
      {
        text: "만료일",
        align: "center",
        value: "expired",
        width: "10rem",
      },
      {
        text: "사용 가능",
        align: "center",
        value: "available",
        filterType: "switch",
        width: "6rem",
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

  protected async created(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  @Watch("queryString")
  public async getList(): Promise<void> {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableMemberEntity[]>(
      `admin/members/?${this.queryString}`,
    );
    this.loading = false;
    this.items = response?.data || [];
  }

  protected editItem(value: TableMemberEntity): void {
    this.item = value;
    this.dialog = true;
  }

  protected async deleteItem(): Promise<void> {
    const result = await confirmDelete();
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<TableMemberEntity>(
        `admin/members/${this.selected[0].id}/`,
      );
      this.saving = false;
      if (response?.code?.startsWith("S")) {
        await this.$store.dispatch("initMemberCodes");
        this.getList().then();
      }
    }
  }

  protected async excel(): Promise<void> {
    this.saving = true;
    await getExcelApi("admin/members/download/excel");
    this.saving = false;
  }
}
</script>
