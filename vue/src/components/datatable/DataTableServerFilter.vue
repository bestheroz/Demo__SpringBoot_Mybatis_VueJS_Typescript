<template>
  <tr id="datatable-header-filter" class="text-center">
    <td v-if="!filterFirstColumn" />
    <td v-for="(data, index) in filterHeader" :key="data.value">
      <v-select
        v-model.trim="filter[index]"
        :items="data.filterSelectItem"
        outlined
        dense
        clearable
        hide-details
        style="width: 95%;"
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
        style="width: 95%;"
        v-else-if="data.filterable !== false && data.filterType === 'switch'"
      />
      <v-text-field
        v-model.trim="filter[index]"
        outlined
        dense
        hide-details
        clearable
        style="width: 95%;"
        v-else-if="data.filterable !== false"
      />
    </td>
  </tr>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';
import { DataTableHeader, SelectItem } from '@/common/types';

@Component({ name: 'DataTableServerFilter' })
export default class extends Vue {
  @Prop({ required: true }) readonly filterHeader!: DataTableHeader[];
  @Prop({ required: true }) readonly originalItems!: any[];
  @Prop({ type: Boolean, default: false }) readonly filterFirstColumn!: boolean;

  readonly USE_YN: SelectItem[] = [
    { value: 'Y', text: '예' },
    { value: 'N', text: '아니요' },
  ];

  filter: string[] | null = null;
  filterMap: string[] | null = null;

  @Watch('filterHeader', { deep: true, immediate: true })
  watchFilterData(val: DataTableHeader[]) {
    const filter: string[] = [];
    const filterMap: string[] = [];
    val.forEach((value: DataTableHeader) => {
      filterMap.push(value.value);
      filter.push('');
      value.filterSelectItem &&
        value.filterSelectItem!.forEach((item: SelectItem) => {
          item.text = item.text || '-';
        });
    });
    this.filter = filter;
    this.filterMap = filterMap;
  }

  @Watch('originalItems', { deep: true, immediate: true })
  @Watch('filter', { deep: true })
  @Emit('update:query-string')
  watchFilter() {
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
<style lang="scss">
.v-data-table--dense {
  #datatable-header-filter {
    text-align: center;

    td {
      height: 24px;
      padding-right: 2px;
      padding-left: 2px;

      .v-input__control {
        width: 0;

        .v-input__slot {
          padding-left: 8px;
          padding-right: 3px;
          min-height: 0;
          height: 24px;

          fieldset {
            height: 28px;
          }

          .v-text-field__slot {
            height: 23px;

            input {
              font-size: 14px;
              padding-top: 0;
              padding-bottom: 0;
            }
          }

          .v-input__append-inner {
            margin-top: 0;
            padding-left: 0;

            .v-icon.v-icon {
              font-size: 16px;
            }
          }

          .v-select__slot {
            height: 23px;

            input {
              font-size: 14px;
              padding-top: 0;
              padding-bottom: 0;
            }
          }
        }
      }
    }
  }
}
</style>
