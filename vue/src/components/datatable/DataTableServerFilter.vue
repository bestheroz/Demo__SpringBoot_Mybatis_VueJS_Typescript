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
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';
import { DataTableHeader, SelectItem } from '@/common/types';
import _, { DebouncedFunc } from 'lodash';

@Component({ name: 'DataTableServerFilter' })
export default class extends Vue {
  @Prop({ required: true }) readonly header!: DataTableHeader[];
  @Prop({ required: true }) readonly input!: any[];
  @Prop({ type: Boolean, default: false }) readonly filterFirstColumn!: boolean;

  readonly debounceHeader: DebouncedFunc<() => {}> = _.debounce(
    this.debouncedHeader,
    100,
  );

  readonly debounceFilter: DebouncedFunc<() => {}> = _.debounce(
    this.debouncedFilter,
    100,
  );

  readonly USE_YN: SelectItem[] = [
    { value: 'Y', text: '예' },
    { value: 'N', text: '아니요' },
  ];

  filter: string[] = [];
  filterMap: string[] = [];

  @Watch('header', { deep: true, immediate: true })
  watchHeader() {
    this.debounceHeader && this.debounceHeader();
  }

  @Watch('input', { deep: true, immediate: true })
  @Watch('filter', { deep: true })
  @Emit('update:query-string')
  watchFilter() {
    this.debounceFilter && this.debounceFilter();
  }

  debouncedHeader() {
    const filter: string[] = [];
    const filterMap: string[] = [];
    this.header.forEach((value: DataTableHeader) => {
      filterMap.push(value.value);
      filter.push(value.filterDefaultValue || '');
      value.filterSelectItem &&
        value.filterSelectItem!.forEach((item: SelectItem) => {
          item.text = item.text || '-';
        });
    });
    this.filter = filter;
    this.filterMap = filterMap;
  }

  @Emit('update:query-string')
  debouncedFilter() {
    const result: any = Object.create(null);
    this.filter &&
      this.filter.forEach((filter: string | undefined | null, index) => {
        if (filter === undefined || filter === '' || filter === null) {
          return;
        }
        result[this.filterMap![index]] = filter;
      });
    return require('query-string').stringify(result);
  }
}
</script>
