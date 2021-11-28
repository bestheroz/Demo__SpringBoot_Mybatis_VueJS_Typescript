<template>
  <div>
    <v-menu rounded="lg" :close-on-content-click="false" v-if="show">
      <template #activator="{ on, attrs }">
        <v-chip
          color="primary"
          outlined
          filter
          filter-icon="mdi-filter-variant"
          :input-value="true"
          v-bind="attrs"
          v-on="on"
          class="pa-2"
          label
        >
          필터
        </v-chip>
      </template>
      <v-card min-width="400" class="mt-6">
        <v-row no-gutters>
          <v-col cols="12" sm="6">
            <v-list-item-group v-model="index" mandatory>
              <v-list-item
                v-for="filter in filters"
                :key="filter.key"
                :disabled="filter.disabled"
              >
                <v-list-item-content>
                  <v-list-item-title v-text="filter.text" />
                </v-list-item-content>
                <v-list-item-icon>
                  <v-icon> mdi-chevron-right </v-icon>
                </v-list-item-icon>
              </v-list-item>
            </v-list-item-group>
          </v-col>

          <v-col cols="12" sm="6">
            <data-table-filter-items
              v-model="selectedFilter"
              @change="onChangeFilter"
            />
          </v-col>
        </v-row>
      </v-card>
    </v-menu>
    <!--    필터 선택 Chip 부분-->
    <template v-if="show">
      <span
        v-for="filter in filters"
        :key="filter.key"
        class="d-inline-flex ml-1 my-0"
      >
        <data-table-filter-selected-chip
          :filter="filter"
          @change="onChangeFilter"
        />
      </span>
    </template>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";
import { Filter } from "@/definitions/types";
import DataTableFilterItems from "@/components/datatable/DataTableFilterItems.vue";
import DataTableFilterSelectedChip from "@/components/datatable/DataTableFilterSelectedChip.vue";

@Component({
  components: { DataTableFilterSelectedChip, DataTableFilterItems },
})
export default class extends Vue {
  @Prop({ required: true }) readonly filters!: Filter[];

  index = 0;
  show = true;

  get selectedFilter(): Filter {
    return this.filters[this.index];
  }

  get selectedFilters(): Filter[] {
    return this.filters
      .filter((v) => v.items.some((i) => i.checked))
      .map((v) => {
        return {
          ...v,
          items: v.items.filter((i) => i.checked),
        };
      });
  }

  protected onChangeFilter(): void {
    this.watchFilters(this.filters);
  }

  @Watch("filters", { deep: true })
  @Emit("update:output")
  protected watchFilters(
    val: Filter[],
  ): Record<string, (string | number | boolean)[]> {
    this.show = false;
    this.$nextTick(() => (this.show = true));
    return Object.fromEntries(
      Object.entries(
        val
          .filter((v) => v.items.some((i) => i.checked))
          .map((v) => {
            return {
              key: v.key,
              value: v.items.filter((i) => i.checked).map((i) => i.value),
            };
          }),
      ).map(([, v]) => {
        return [v.key, v.value];
      }),
    );
  }
}
</script>
