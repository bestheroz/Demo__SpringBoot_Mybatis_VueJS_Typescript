<template>
  <div>
    <v-data-table
      v-model="syncedSelected"
      must-sort
      fixed-header
      :loading="loading"
      :headers="headers"
      :items="filteredItems"
      :sort-by="sortBy"
      :sort-desc="sortDesc"
      item-key="code"
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
      <template #[`item.code`]="{ item }">
        <a
          :style="{ 'font-weight': 'bold' }"
          @click="$emit('row-id-clicked', { ...item })"
        >
          {{ item.code }}
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
      <template #[`item.updatedBy`]="{ item }">
        {{ item.updatedBy | formatMemberNm }}
      </template>
      <template #[`item.updated`]="{ item }">
        {{ item.updated | formatDatetime }}
      </template>
    </v-data-table>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Vue, Watch } from "vue-property-decorator";
import type {
  DataTableHeader,
  SelectItem,
  TableCodeEntity,
  TableCodeGroupEntity,
} from "@/common/types";
import { getApi, getCodesApi } from "@/utils/apis";
import envs from "@/constants/envs";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";

@Component({
  name: "CodeList",
  components: {
    DataTableFilter,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly parentItem!: TableCodeGroupEntity;
  @Prop({ required: true }) readonly height!: number | string;
  @PropSync("selected") syncedSelected!: TableCodeEntity[];

  readonly envs: typeof envs = envs;
  AUTHORITY: SelectItem[] = [];

  loading = false;
  sortBy: string[] = ["displayOrder"];
  sortDesc: boolean[] = [false];
  items: TableCodeEntity[] = [];
  filteredItems: TableCodeEntity[] = [];
  headers: DataTableHeader[] = [
    {
      text: "상세 코드",
      align: "start",
      value: "code",
    },
    {
      text: "상세 코드명",
      align: "start",
      value: "name",
    },
    {
      text: "사용 가능",
      align: "center",
      value: "available",
      filterType: "switch",
      width: "6rem",
    },
    {
      text: "출력 순서",
      align: "end",
      value: "displayOrder",
      width: "6rem",
    },
    {
      text: "권한(~부터 볼수 있음)",
      align: "center",
      value: "authority",
      filterType: "select",
      filterSelectItem: [],
      width: "11rem",
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

  async beforeMount(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  mounted(): void {
    const find = this.headers.find((item) => item.value === "authority");
    if (find) {
      this.headers[
        this.headers.indexOf(find)
      ].filterSelectItem = this.AUTHORITY;
    }
  }

  @Watch("parentItem")
  watchParentItem(val: TableCodeGroupEntity): void {
    this.items = [];
    val?.codeGroup && this.getList();
  }

  async getList(): Promise<void> {
    this.syncedSelected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableCodeEntity[]>(
      `admin/codes/${this.parentItem.codeGroup}`,
    );
    this.loading = false;
    this.items = response?.data || [];
  }
}
</script>
