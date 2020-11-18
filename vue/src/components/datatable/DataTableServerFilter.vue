<template>
  <tr class="datatable-header-filter">
    <td v-if="!filterFirstColumn" />
    <td v-for="(data, index) in header" :key="data.value">
      <v-select
        v-model.trim="filter[index]"
        :items="data.filterSelectItem"
        outlined
        dense
        clearable
        hide-details
        style="width: 95%"
        v-if="
          data.filterable !== false &&
          data.filterType === 'select' &&
          data.filterSelectItem
        "
      />
      <v-select
        v-model.trim="filter[index]"
        :items="USE_YN"
        outlined
        dense
        clearable
        hide-details
        style="width: 95%"
        v-else-if="data.filterable !== false && data.filterType === 'switch'"
      />
      <v-text-field
        v-model.trim="filter[index]"
        outlined
        dense
        hide-details
        clearable
        style="width: 95%"
        v-else-if="data.filterable !== false"
      />
    </td>
  </tr>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";
import type { DataTableHeader, SelectItem } from "@/common/types";
import _, { DebouncedFunc } from "lodash";
import qs from "qs";

@Component({ name: "DataTableServerFilter" })
export default class extends Vue {
  @Prop({ required: true }) readonly header!: DataTableHeader[];
  /* eslint-disable */
  @Prop({ required: true }) readonly input!: any[];
  /* eslint-enable */
  @Prop({ type: Boolean, default: false }) readonly filterFirstColumn!: boolean;

  readonly debounceHeader: DebouncedFunc<
    () => {
      //
    }
  > = _.debounce(this.debouncedHeader, 100);

  readonly debounceFilter: DebouncedFunc<
    () => {
      //
    }
  > = _.debounce(this.debouncedFilter, 100);

  readonly USE_YN: SelectItem[] = [
    { value: "Y", text: "예" },
    { value: "N", text: "아니요" },
  ];

  filter: string[] = [];
  filterMap: string[] = [];

  @Watch("header", { deep: true, immediate: true })
  watchHeader(): void {
    this.debounceHeader && this.debounceHeader();
  }

  @Watch("input", { deep: true, immediate: true })
  @Watch("filter", { deep: true })
  @Emit("update:query-string")
  watchFilter(): void {
    this.debounceFilter && this.debounceFilter();
  }

  debouncedHeader(): void {
    const filter: string[] = [];
    const filterMap: string[] = [];
    this.header.forEach((value: DataTableHeader) => {
      filterMap.push(value.value);
      filter.push(value.filterDefaultValue || "");
      value.filterSelectItem &&
        value.filterSelectItem?.forEach((item: SelectItem) => {
          item.text = item.text || "-";
        });
    });
    this.filter = filter;
    this.filterMap = filterMap;
  }

  @Emit("update:query-string")
  debouncedFilter(): string {
    /* eslint-disable */
    const result: any = Object.create(null);
    /* eslint-enable */
    this.filter &&
      this.filter.forEach((filter: string | undefined | null, index) => {
        if (filter === undefined || filter === "" || filter === null) {
          return;
        }
        result[this.filterMap?.[index]] = filter;
      });
    return qs.stringify(result);
  }
}
</script>
