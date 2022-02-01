<template>
  <div>
    <v-menu rounded="lg" v-model="isOpen" offset-y>
      <template #activator="{ on }">
        <v-chip
          :close="!vModel.required"
          @click:close="emptySelectFilters"
          outlined
          :color="vModel.required ? 'error' : 'primary'"
          v-on="on"
          class="pa-2"
          v-show="chipLabel"
          label
        >
          {{ chipLabel }}
        </v-chip>
      </template>
      <v-card
        v-if="isOpen && filtered.items.length > 0 && filtered.type !== 'text'"
      >
        <data-table-filter-items
          v-model="vModel"
          from-chip
          @closed="isOpen = false"
          @change="$emit('change')"
        />
      </v-card>
    </v-menu>
  </div>
</template>

<script lang="ts">
import type { Filter } from "@/definitions/types";
import DataTableFilterItems from "@/components/datatable/DataTableFilterItems.vue";
import {
  computed,
  defineComponent,
  PropType,
  reactive,
  toRefs,
} from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  components: { DataTableFilterItems },
  props: {
    value: { required: true, type: Object as PropType<Filter> },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<Filter>(props, emit);
    const state = reactive({ isOpen: false });
    const computes = {
      filtered: computed((): Filter => {
        return {
          ...vModel.vModel.value,
          items: vModel.vModel.value.items.filter((i) => i.checked),
        };
      }),
      chipLabel: computed((): string | undefined => {
        if (computes.filtered.value.items.length > 0) {
          if (
            ["dateStartEndPicker", "text"].includes(
              computes.filtered.value.type,
            )
          ) {
            return `${computes.filtered.value.text} = ["${
              computes.filtered.value.items[0].chipText ||
              computes.filtered.value.items[0].value
            }"]`;
          } else {
            return `${
              computes.filtered.value.text
            } = ["${computes.filtered.value.items
              .map((i) => i.text)
              /* eslint-disable quotes */
              .join('", "')}"]`;
          }
        }
        return undefined;
      }),
    };
    const methods = {
      emptySelectFilters: (): void => {
        vModel.vModel.value = {
          ...vModel.vModel.value,
          items: vModel.vModel.value.items.map((item) => {
            if (vModel.vModel.value.type === "text") {
              return { ...item, checked: false, value: "" };
            } else {
              return { ...item, checked: false };
            }
          }),
        };
        emit("change");
      },
    };
    return { ...vModel, ...toRefs(state), ...computes, ...methods };
  },
});
</script>
