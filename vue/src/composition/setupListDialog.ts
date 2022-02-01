import { reactive, toRefs, UnwrapRef } from "@vue/composition-api";
import { cloneDeep } from "lodash-es";

export default function <T>(initItemFunc: CallableFunction) {
  const state = reactive({
    editItem: initItemFunc() as T,
    dialog: false,
  });
  const methods = {
    showAddDialog: (): void => {
      state.editItem = initItemFunc() as UnwrapRef<T>;
      state.dialog = true;
    },
    showEditDialog: (item: T): void => {
      state.editItem = cloneDeep(item) as UnwrapRef<T>;
      state.dialog = true;
    },
  };
  return { ...toRefs(state), ...methods };
}
