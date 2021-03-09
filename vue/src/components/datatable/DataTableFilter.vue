<template>
  <tr class="datatable-header-filter">
    <td v-if="!filterFirstColumn" />
    <td v-for="(header, index) in headers" :key="header.value">
      <v-autocomplete
        v-if="
          header.filterable !== false &&
          header.filterType === 'select' &&
          header.filterSelectItem
        "
        v-model.trim="filters[index]"
        :items="header.filterSelectItem"
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
        v-else-if="
          header.filterable !== false && header.filterType === 'switch'
        "
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
        v-else-if="header.filterable !== false"
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
import {
  Component,
  Emit,
  Prop,
  PropSync,
  Vue,
  Watch,
} from "vue-property-decorator";
import type { DataTableHeader, SelectItem } from "@/common/types";
import _, { DebouncedFunc } from "lodash";

@Component({ name: "DataTableFilter" })
export default class extends Vue {
  @Prop({ required: true }) readonly headers!: DataTableHeader[];
  @Prop({ type: Boolean }) readonly filterFirstColumn!: boolean;
  @PropSync("filter") syncedFilter!: { [p: string]: string | number };

  ready = false;

  readonly debounceHeader: DebouncedFunc<
    () => {
      //
    }
  > = _.debounce(this.debouncedHeader, 100);

  readonly debounceFilter: DebouncedFunc<
    () => {
      //
    }
  > = _.debounce(this.debouncedFilter, 300);

  readonly USE_YN: SelectItem[] = [
    { value: "true", text: "예" },
    { value: "false", text: "아니요" },
  ];

  filters: string[] = [];
  filtersMap: {
    key: string;
    value?: string | string[] | null;
    condition: string;
  }[] = [];

  protected created(): void {
    this.filters = this.headers.map((header) => {
      return header.filterDefaultValue || "";
    });
    this.$nextTick(() => {
      this.ready = true;
    });
  }

  @Watch("headers", { deep: true, immediate: true })
  protected watchHeader(): void {
    this.debounceHeader && this.debounceHeader();
  }

  @Watch("filters", { deep: true })
  protected watchFilter(): void {
    if (this.ready) {
      this.debounceFilter && this.debounceFilter();
    }
  }

  protected debouncedHeader(): void {
    this.headers.forEach((value: DataTableHeader) => {
      (value.filterSelectItem || [])
        .filter((item) => !item)
        .forEach((item: SelectItem) => {
          item.text = item.text || "-";
        });
    });
    this.filtersMap = this.headers.map((header) => {
      let condition;
      if (header.filterType === "select") {
        condition = "set";
      } else if (header.filterType === "switch") {
        condition = "booleanEquals";
      } else {
        condition = "contains";
      }
      return { key: header.value, condition: condition };
    });
  }

  @Emit("update:filter")
  protected debouncedFilter(): { [p: string]: string | string[] | number } {
    (this.filters || []).forEach((filter: string | undefined | null, index) => {
      this.filtersMap[index] = { ...this.filtersMap[index], value: filter };
    });
    return Object.fromEntries(
      Object.entries(
        this.filtersMap.filter(
          (filter) =>
            (filter.condition !== "set" && filter.value) ||
            (filter.condition === "set" && (filter.value || []).length > 0),
        ),
      ).map(([, v]) => [
        `${v.key}:${v.condition || "contains"}`,
        v.condition === "set" ? JSON.stringify(v.value || "") : v.value || "",
      ]),
    );
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
