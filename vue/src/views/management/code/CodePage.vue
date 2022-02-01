<template>
  <div class="w-full">
    <page-title @click="showAddDialog" :button-loading="saving">
      <template #more-buttons>
        <v-btn
          @click="saveItems"
          color="primary"
          outlined
          x-large
          v-if="$store.getters.writeAuthority"
        >
          <v-icon> mdi-sort </v-icon> 순서저장
        </v-btn>
        <v-btn
          @click="$refs.refCodeList.getList()"
          color="primary"
          outlined
          x-large
        >
          <v-icon> mdi-refresh </v-icon> 새로고침
        </v-btn>
      </template>
    </page-title>
    <v-card>
      <v-card-text>
        <data-table-filter :filters="filters" :output.sync="filterOutput" />
        <code-list
          ref="refCodeList"
          :type="type"
          class="pt-1"
          @created="onCreated"
          @removed="onRemoved"
        />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import CodeList from "@/views/management/code/CodeList.vue";
import PageTitle from "@/components/title/PageTitle.vue";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import { Filter, FilterOutput } from "@/definitions/types";
import { getApi } from "@/utils/apis";
import { Code } from "@/definitions/models";
import {
  computed,
  defineComponent,
  onMounted,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";

export default defineComponent({
  components: { DataTableFilter, PageTitle, CodeList },
  setup() {
    const state = reactive({
      types: [] as string[],
      saving: false,
      filterOutput: {} as FilterOutput,
    });

    const computes = {
      type: computed(() => (state.filterOutput.type?.[0] as string) || ""),
      filters: computed((): Filter[] => [
        {
          type: "checkbox",
          text: "타입",
          key: "type",
          single: true,
          required: true,
          items: state.types.map((v) => ({
            value: v,
            text: v,
            checked: false,
          })),
        },
      ]),
    };

    const methods = {
      saveItems: async (): Promise<void> => {
        state.saving = true;
        await refCodeList.value?.saveItems();
        state.saving = false;
      },
      showAddDialog: async (): Promise<void> => {
        await refCodeList.value?.showAddDialog();
      },
      getTypes: async (): Promise<void> => {
        state.types = [];
        const response = await getApi<string[]>("codes/types/");
        state.types = response.data ? response.data : [];
      },
      onCreated: async (value: Code): Promise<void> => {
        if (state.types.every((t) => t === value.type)) {
          state.types = [value.type, ...state.types];
        }
      },
      onRemoved: (value: Code): void => {
        state.types = state.types.filter((item) => item !== value.type);
      },
    };

    onMounted(async () => {
      await methods.getTypes();
      computes.filters.value
        .find((f) => f.key === "type")
        ?.items.forEach((i) => (i.checked = i.value === state.types?.[0]));
    });

    const refCodeList = ref<null | InstanceType<typeof CodeList>>(null);
    return {
      ...toRefs(state),
      ...computes,
      ...methods,
      refCodeList,
    };
  },
});
</script>
