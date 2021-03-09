<template>
  <tr class="datatable-header-filter">
    <td v-if="!filterFirstColumn" />
    <td v-for="(data, index) in headers" :key="data.value">
      <v-autocomplete
        v-if="
          data.filterable !== false &&
          data.filterType === 'select' &&
          data.filterSelectItem
        "
        v-model.trim="filters[index]"
        :items="data.filterSelectItem"
        outlined
        dense
        clearable
        hide-details
        multiple
        small-chips
        single-line
        style="width: 95%"
      />
      <v-autocomplete
        v-else-if="data.filterable !== false && data.filterType === 'switch'"
        v-model.trim="filters[index]"
        :items="USE_YN"
        outlined
        dense
        clearable
        hide-details
        small-chips
        single-line
        style="width: 95%"
      />
      <v-text-field
        v-else-if="data.filterable !== false"
        v-model.trim="filters[index]"
        outlined
        dense
        hide-details
        clearable
        style="width: 95%"
      />
    </td>
  </tr>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";
import type { DataTableHeader, SelectItem } from "@/common/types";
import _, { DebouncedFunc } from "lodash";

@Component({ name: "DataTableClientSideFilter" })
export default class extends Vue {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  @Prop({ required: true }) readonly output!: any[];
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  @Prop({ required: true }) readonly input!: any[];
  @Prop({ required: true }) readonly headers!: DataTableHeader[];
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

  filters: string[] = [];
  filterMaps: string[] = [];

  @Watch("headers", { deep: true, immediate: true })
  protected watchHeader(): void {
    this.debounceHeader && this.debounceHeader();
  }

  @Watch("input", { deep: true, immediate: true })
  @Watch("filters", { deep: true })
  protected watchFilter(): void {
    this.debounceFilter && this.debounceFilter();
  }

  protected debouncedHeader(): void {
    const filters: string[] = [];
    const filterMaps: string[] = [];
    this.headers.forEach((header: DataTableHeader) => {
      filterMaps.push(header.value);
      filters.push(header.filterDefaultValue || "");
      (header.filterSelectItem || []).forEach(
        (filterSelectItem: SelectItem) => {
          filterSelectItem.text = filterSelectItem.text || "-";
        },
      );
    });
    this.filters = filters;
    this.filterMaps = filterMaps;
  }

  @Emit("update:output")
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  protected debouncedFilter(): any[] {
    let output = this.input;
    (this.filters || []).forEach((filter: string | undefined | null, index) => {
      if (
        filter === undefined ||
        filter === "" ||
        filter === null ||
        filter.length === 0
      ) {
        return;
      }
      if (_.isArray(filter)) {
        output = output.filter((value) => {
          return (
            !this.filterMaps[index] ||
            value[this.filterMaps[index]] === undefined ||
            filter
              .map((value) => value.toString().toUpperCase())
              .includes(value[this.filterMaps[index]].toString())
          );
        });
      } else {
        output = output.filter((value) => {
          return (
            !this.filterMaps[index] ||
            value[this.filterMaps[index]] === undefined ||
            value[this.filterMaps[index]]
              .toString()
              .toUpperCase()
              .indexOf(filter.toUpperCase()) !== -1
          );
        });
      }
    });
    return output;
  }
}
</script>
<style lang="scss">
div.v-data-table {
  div.v-data-table__wrapper {
    tr.datatable-header-filter {
      text-align: center;

      td {
        height: 1.5rem;
        padding-right: 2px;
        padding-left: 2px;
        border-bottom: thin solid var(--v-secondary-base);
        border-left: thin solid var(--v-secondary-base);

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
              padding-right: 0.25rem;
              min-height: 0;
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
                font-size: 0.8rem;
                padding-top: 0;
                padding-bottom: 0;
              }
            }

            .v-input__append-inner {
              margin-top: 0;
              padding-left: 0;
            }

            .v-select__slot {
              font-size: 0.9rem;
              height: 1.45rem;

              input {
                padding-top: 0;
                padding-bottom: 0;
              }

              .v-select__selections {
                display: contents;

                .v-select__selection--comma:not(:first-child) {
                  display: none;
                }

                .v-chip {
                  :not(:first-child) {
                    display: none;
                  }

                  max-height: 1.2rem;
                  min-height: 1.2rem;
                  height: 1.2rem;
                  padding-left: 0.25rem;
                  padding-right: 0.25rem;
                  margin-left: 0.1rem;
                  margin-right: 0.1rem;
                }

                input {
                  min-width: 1rem;
                }
              }
            }
          }
        }
      }

      td:last-child {
        border-right: thin solid var(--v-secondary-base);
      }
    }
  }
}
</style>
