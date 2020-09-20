<template>
  <tr class="text-center datatable-header-filter">
    <td v-if="!filterFirstColumn" />
    <td v-for="(data, index) in filterHeader" :key="data.value">
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
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';
import { DataTableHeader, SelectItem } from '@/common/types';

@Component({ name: 'DataTableFilter' })
export default class extends Vue {
  @Prop({ required: true }) readonly filteredItems!: any[];
  @Prop({ required: true }) readonly filterHeader!: DataTableHeader[];
  @Prop({ required: true }) readonly originalItems!: any[];
  @Prop({ type: Boolean, default: false }) readonly filterFirstColumn!: boolean;

  readonly USE_YN: SelectItem[] = [
    { value: 'true', text: '예' },
    { value: 'false', text: '아니요' },
  ];

  filter: string[] | null = null;
  filterMap: string[] | null = null;

  @Watch('filterHeader', { deep: true, immediate: true })
  watchFilterData(val: DataTableHeader[]) {
    const filter: string[] = [];
    const filterMap: string[] = [];
    val.forEach((value: DataTableHeader) => {
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

  @Watch('originalItems', { deep: true, immediate: true })
  @Watch('filter', { deep: true })
  @Emit('update:filtered-items')
  watchFilter() {
    let filteredItems = this.originalItems;
    this.filter &&
      this.filter.forEach((filter: string | undefined | null, index) => {
        if (filter === undefined || filter === '' || filter === null) {
          return;
        }
        filteredItems = filteredItems.filter(
          (value) =>
            // console.log(filter);
            // @ts-ignore
            // console.log(this.filterMap[index]);
            // @ts-ignore
            // console.log(value[this.filterMap[index]]);
            // @ts-ignore
            // console.log(value[this.filterMap[index]].indexOf(filter));
            // @ts-ignore
            // console.log(this.filter[filter]);
            // @ts-ignore
            !this.filterMap[index] ||
            // @ts-ignore
            value[this.filterMap[index]] === undefined ||
            // @ts-ignore
            value[this.filterMap[index]]
              .toString()
              .toUpperCase()
              .indexOf(filter.toUpperCase()) !== -1,
        );
      });
    return filteredItems;
  }
}
</script>
<style lang="scss" scoped>
tr.datatable-header-filter {
  text-align: center;

  td {
    height: 1.5rem;
    padding-right: 2px;
    padding-left: 2px;
    border-bottom: thin solid var(--v-table-border-base);
    border-left: thin solid var(--v-table-border-base);

    div.v-input {
      div.v-input__append-inner {
        div.v-input__icon {
          width: 1rem;
          min-width: 1rem;
        }
      }

      div.v-input__control {
        div.v-input__slot {
          padding-left: 0.25rem;
        }
      }
    }

    .v-input__control {
      width: 0;

      .v-input__slot {
        padding-left: 0.5rem;
        padding-right: 3px;
        min-height: 0;
        height: 1.5rem;

        fieldset {
          height: 1.75rem;
        }

        .v-text-field__slot {
          height: 1.46rem;

          input {
            padding-top: 0;
            padding-bottom: 0;
          }
        }

        .v-input__append-inner {
          margin-top: 0;
          padding-left: 0;
        }

        .v-select__slot {
          height: 1.45rem;

          input {
            padding-top: 0;
            padding-bottom: 0;
          }
        }
      }
    }
  }

  td:last-child {
    border-right: thin solid var(--v-table-border-base);
  }
}
</style>
