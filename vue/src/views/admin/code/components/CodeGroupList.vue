<template>
  <div>
    <v-data-table
      must-sort
      fixed-header
      v-model="syncedSelected"
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
      <template v-slot:header>
        <data-table-filter
          :header="headers"
          :output.sync="filteredItems"
          :input="items"
        />
      </template>
      <template v-slot:[`item.codeGroup`]="{ item }">
        <a
          :style="{ 'font-weight': 'bold' }"
          @click="$emit('row-id-clicked', { ...item })"
        >
          {{ item.codeGroup }}
        </a>
      </template>
      <template v-slot:[`item.updated`]="{ item }">
        {{ item.updated | formatDatetime }}
      </template>
      <template v-slot:[`item.updatedBy`]="{ item }">
        {{ item.updatedBy | formatMemberNm }}
      </template>
    </v-data-table>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, PropSync, Vue } from "vue-property-decorator";
import { DataTableHeader, TableCodeGroupEntity } from "@/common/types";
import { getApi } from "@/utils/apis";
import envs from "@/constants/envs";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";

@Component({
  name: "CodeGroupList",
  components: {
    DataTableFilter,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  @PropSync("selected") syncedSelected!: TableCodeGroupEntity[];
  readonly envs: typeof envs = envs;
  sortBy: string[] = ["codeGroup"];
  sortDesc: boolean[] = [false];
  items: TableCodeGroupEntity[] = [];
  filteredItems: TableCodeGroupEntity[] = [];
  loading = false;

  headers: DataTableHeader[] = [
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

  mounted() {
    this.getList();
  }

  @Emit("updated")
  async getList() {
    this.syncedSelected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableCodeGroupEntity[]>("admin/codeGroups/");
    this.loading = false;
    this.items = response?.data || [];
  }
}
</script>
