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
        v-else-if="data.filterable !== false && data.filterType === 'switch'"
      />
      <v-text-field
        v-model.trim="filter[index]"
        outlined
        dense
        hide-details
        clearable
        v-else-if="data.filterable !== false"
      />
    </td>
  </tr>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";
import type { DataTableHeader, SelectItem } from "@/common/types";
import _, { DebouncedFunc } from "lodash";

@Component({ name: "DataTableFilter" })
export default class extends Vue {
  /* eslint-disable */
  @Prop({ required: true }) readonly output!: any[];
  @Prop({ required: true }) readonly input!: any[];
  /* eslint-enable */
  @Prop({ required: true }) readonly header!: DataTableHeader[];
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
    { value: "true", text: "예" },
    { value: "false", text: "아니요" },
  ];

  filter: string[] = [];
  filterMap: string[] = [];

  @Watch("header", { deep: true, immediate: true })
  watchHeader(): void {
    this.debounceHeader && this.debounceHeader();
  }

  @Watch("input", { deep: true, immediate: true })
  @Watch("filter", { deep: true })
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

  @Emit("update:output")
  debouncedFilter(): unknown[] {
    let output = this.input;
    this.filter &&
      this.filter.forEach((filter: string | undefined | null, index) => {
        if (filter === undefined || filter === "" || filter === null) {
          return;
        }
        output = output.filter(
          (value) =>
            !this.filterMap[index] ||
            value[this.filterMap[index]] === undefined ||
            value[this.filterMap[index]]
              .toString()
              .toUpperCase()
              .indexOf(filter.toUpperCase()) !== -1,
        );
      });
    return output;
  }
}
</script>
