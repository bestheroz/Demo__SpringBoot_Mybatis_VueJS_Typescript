<template>
  <div>
    <v-card-text class="pb-1">
      <v-data-table
        must-sort
        fixed-header
        v-model="syncedSelected"
        :loading="loading"
        :headers="headers"
        :items="filteredItems"
        :sort-by="sortBy"
        :sort-desc="sortDesc"
        item-key="id"
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
        <template #[`item.id`]="{ item }">
          <a
            :style="{ 'font-weight': 'bold' }"
            @click="$emit('row-id-clicked', { ...item, password: undefined })"
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
        <template #[`item.authority`]="{ item }" v-if="AUTHORITY">
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
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, PropSync, Vue } from "vue-property-decorator";
import type {
  DataTableHeader,
  SelectItem,
  TableMemberEntity,
} from "@/common/types";
import { getApi, getCodesApi } from "@/utils/apis";
import envs from "@/constants/envs";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";

@Component({
  name: "MemberList",
  components: {
    DataTableFilter,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  @PropSync("selected") syncedSelected!: TableMemberEntity[];
  readonly envs: typeof envs = envs;
  readonly ENDPOINT_URL: string = "admin/members/";
  sortBy: string[] = ["authority"];
  sortDesc: boolean[] = [true];
  items: TableMemberEntity[] = [];
  filteredItems: TableMemberEntity[] = [];
  loading = false;

  AUTHORITY: SelectItem[] = [];

  headers: DataTableHeader[] = [
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
      filterSelectItem: [],
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

  async beforeMount(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  async mounted(): Promise<void> {
    const find = this.headers.find((item) => item.value === "authority");
    if (find) {
      this.headers[
        this.headers.indexOf(find)
      ].filterSelectItem = this.AUTHORITY;
    }
    await this.getList();
  }

  async getList(): Promise<void> {
    this.syncedSelected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableMemberEntity[]>(this.ENDPOINT_URL);
    this.loading = false;
    this.items = response?.data || [];
  }

  @Emit("row-id-clicked")
  rowIdClicked(value: TableMemberEntity): TableMemberEntity {
    return value;
  }
}
</script>
