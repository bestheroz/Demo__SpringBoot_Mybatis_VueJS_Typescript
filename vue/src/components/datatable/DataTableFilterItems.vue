<template>
  <div>
    <v-list flat>
      <v-list-item
        v-for="item in vModel.items"
        :key="item.value"
        :ripple="false"
      >
        <v-list-item-action class="mr-4" v-if="vModel.type === 'checkbox'">
          <v-checkbox
            v-model="item.checked"
            :true-value="item.value"
            :disabled="vModel.required && checkedLength === 1 && item.checked"
            @click="onClickCheckbox(item)"
          />
        </v-list-item-action>
        <v-list-item-action
          class="mr-4"
          v-else-if="isDialogComponent && !fromChip"
        >
          <v-btn icon @click="searchDialog = true">
            <v-icon> mdi-dock-window </v-icon>
          </v-btn>
        </v-list-item-action>
        <v-list-item-content v-else-if="vModel.type === 'text'" class="py-0">
          <v-text-field
            :value="item.value"
            :label="item.text"
            @input="(_item) => onUpdateTextField(_item, item)"
            :disabled="fromChip"
            clearable
          />
        </v-list-item-content>
        <v-list-item-content v-if="vModel.type !== 'text'">
          <v-list-item-title
            v-text="item.text"
            :class="item.checked ? 'success--text' : undefined"
          />
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </div>
</template>

<script lang="ts">
import { debounce, DebouncedFunc } from "lodash-es";
import type { Filter, FilterItem } from "@/definitions/types";
import { FilterItemType } from "@/definitions/types";
import {
  computed,
  defineComponent,
  nextTick,
  onBeforeMount,
  PropType,
  reactive,
  toRefs,
  watch,
} from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  props: {
    value: {
      type: Object as PropType<Filter>,
      required: true,
    },
    fromChip: {
      type: Boolean,
    },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<Filter>(props, emit);
    const state = reactive({ searchDialog: false });
    const computes = {
      isDialogComponent: computed((): boolean =>
        ["dateStartEndPicker"].includes(vModel.vModel.value.type),
      ),
      checkedLength: computed(
        (): number => vModel.vModel.value.items.filter((i) => i.checked).length,
      ),
      fetchList: computed(
        (): DebouncedFunc<never> =>
          debounce((value: string, item: FilterItem<FilterItemType>): void => {
            item.value = value;
            item.checked = false;
            // chip 에 바로 적용안되는 이슈가 있어서 추가함
            nextTick(() => {
              item.checked = !!item.value;
              item.chipText = item.value;
            });
          }, 200),
      ),
    };
    const methods = {
      onClickCheckbox: (filterItem: FilterItem<FilterItemType>): void => {
        if (vModel.vModel.value.single) {
          vModel.vModel.value = {
            ...vModel.vModel.value,
            items: vModel.vModel.value.items.map((item) => {
              return {
                ...item,
                checked: filterItem.checked && item.value === filterItem.value,
              };
            }),
          };
          vModel.vModel.value.items = [...vModel.vModel.value.items];
        }
        emit("change");
      },
      onUpdateTextField: (
        value: string,
        item: FilterItem<FilterItemType>,
      ): void => {
        methods.fetchComments(value, item);
      },
      fetchComments: debounce(
        (value: string, item: FilterItem<FilterItemType>): void => {
          item.value = value;
          item.checked = false;
          // chip 에 바로 적용안되는 이슈가 있어서 추가함
          nextTick(() => {
            item.checked = !!item.value;
            item.chipText = item.value;
          });
        },
        300,
      ),
    };
    watch(
      () => state.searchDialog,
      (val: boolean) => {
        if (!val) {
          emit("closed");
        }
      },
      { immediate: true },
    );
    onBeforeMount(() => {
      if (props.fromChip && computes.isDialogComponent.value) {
        state.searchDialog = true;
      }
    });
    return { ...vModel, ...toRefs(state), ...computes, ...methods };
  },
});
</script>
