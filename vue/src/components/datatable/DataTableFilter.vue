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
        v-for="(filter, _index) in cloneFilters"
        :key="filter.key"
        class="d-inline-flex ml-1 mt-0 mb-1"
      >
        <data-table-filter-selected-chip
          v-model="cloneFilters[_index]"
          @change="onChangeFilter"
        />
      </span>
    </template>
  </div>
</template>

<script lang="ts">
import type { Filter } from "@/definitions/types";
import DataTableFilterItems from "@/components/datatable/DataTableFilterItems.vue";
import DataTableFilterSelectedChip from "@/components/datatable/DataTableFilterSelectedChip.vue";
import {
  computed,
  defineComponent,
  PropType,
  reactive,
  toRefs,
  watch,
} from "@vue/composition-api";

export default defineComponent({
  components: { DataTableFilterSelectedChip, DataTableFilterItems },
  props: {
    filters: { type: Array as PropType<Filter[]>, required: true },
  },
  setup(props, { emit }) {
    const state = reactive({ cloneFilters: [] as Filter[], index: 0 });
    const computes = {
      filteredLength: computed(
        (): number =>
          state.cloneFilters
            .map((f) => f.items.some((i) => i.checked))
            .filter((f) => f).length,
      ),
    };
    const methods = {
      resetFilter: (): void => {
        state.cloneFilters = [];
        methods.changeCloneFilters(props.filters);
      },
      changeCloneFilters: (val: Filter[]): void => {
        state.cloneFilters = [
          ...val.map((f) => {
            const cloneItems =
              state.cloneFilters.find((c) => c.key === f.key)?.items || [];
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
      },
      onChangeFilter: (): void => {
        emit(
          "update:output",
          Object.fromEntries(
            Object.entries(
              state.cloneFilters
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
          ),
        );
      },
    };
    watch(
      () => props.filters,
      (val: Filter[]) => {
        methods.changeCloneFilters(val);
        methods.onChangeFilter();
      },
      { immediate: true },
    );
    return { ...toRefs(state), ...computes, ...methods };
  },
});
</script>
