<template>
  <div>
    <v-menu rounded="lg" :close-on-content-click="false">
      <template #activator="{ on, attrs }">
        <v-chip
          :color="filteredLength === 0 ? 'secondary' : 'primary'"
          outlined
          filter
          filter-icon="mdi-filter-variant"
          :input-value="true"
          v-bind="attrs"
          v-on="on"
          class="pa-2"
          label
        >
          필터 ({{ filteredLength }} / {{ cloneFilters.length }})
        </v-chip>
      </template>
      <v-card min-width="400" class="mt-6">
        <v-row no-gutters>
          <v-col width="auto">
            <v-list-item-group v-model="index" mandatory>
              <v-list-item
                v-for="filter in cloneFilters"
                :key="filter.key"
                :disabled="filter.disabled"
                active-class="primary--text"
              >
                <v-list-item-content>
                  <v-list-item-title v-text="filter.text" />
                </v-list-item-content>
                <v-list-item-icon>
                  <v-icon> mdi-chevron-right</v-icon>
                </v-list-item-icon>
              </v-list-item>
            </v-list-item-group>
          </v-col>

          <v-col width="auto">
            <data-table-filter-items
              v-model="cloneFilters[index]"
              @change="onChangeFilter"
            />
          </v-col>
        </v-row>
      </v-card>
    </v-menu>
    <!--    필터 선택 Chip 부분-->
    <template>
      <span
        v-for="(filter, index) in cloneFilters"
        :key="filter.key"
        class="d-inline-flex ml-1 mt-0 mb-1"
      >
        <data-table-filter-selected-chip
          v-model="cloneFilters[index]"
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

  cloneFilters: Filter[] = [];
  index = 0;

  get selectedFilters(): Filter[] {
    return this.cloneFilters
      .filter((v) => v.items.some((i) => i.checked))
      .map((v) => {
        return {
          ...v,
          items: v.items.filter((i) => i.checked),
        };
      });
  }

  get filteredLength(): number {
    return this.cloneFilters
      .map((f) => f.items.some((i) => i.checked))
      .filter((f) => f).length;
  }

  @Watch("filters", { immediate: true })
  protected watchFilters(): void {
    this.cloneFilters = [
      ...this.filters.map((f) => {
        const cloneItems =
          this.cloneFilters.find((c) => c.key === f.key)?.items || [];
        return {
          ...f,
          items: [
            ...f.items.map((i) => {
              return {
                ...i,
                checked:
                  cloneItems.find((ci) => ci.value === i.value)?.checked ||
                  i.checked,
              };
            }),
          ],
        };
      }),
    ];
    this.onChangeFilter();
  }

  public resetFilter(): void {
    this.cloneFilters = [];
    this.watchFilters();
  }

  @Emit("update:output")
  protected onChangeFilter(): Record<string, (string | number | boolean)[]> {
    return Object.fromEntries(
      Object.entries(
        this.cloneFilters
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
