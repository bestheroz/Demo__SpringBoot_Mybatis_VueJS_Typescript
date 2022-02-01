import { postApi, putApi } from "@/utils/apis";
import setupSyncedDialog from "@/composition/setupSyncedDialog";
import { computed, reactive, toRefs } from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default function <T>(
  props: { value: T; dialog: boolean },
  emit: unknown,
  url: string,
) {
  const syncedDialog = setupSyncedDialog(props, emit);
  const vModel = setupVModel<T>(props, emit);
  const state = reactive({
    url: url,
    loading: false,
  });
  const methods = {
    create: async (): Promise<void> => {
      state.loading = true;
      const response = await postApi<T>(state.url, vModel.vModel.value);
      state.loading = false;
      if (response.success) {
        syncedDialog.syncedDialog.value = false;
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        emit("created", response.data);
      }
    },

    update: async (): Promise<void> => {
      state.loading = true;
      const response = await putApi<T>(
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        `${state.url}${vModel.vModel.value.id}`,
        vModel.vModel.value,
      );
      state.loading = false;
      if (response.success) {
        syncedDialog.syncedDialog.value = false;
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        emit("updated", response.data);
      }
    },
  };
  const computes = {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    isNew: computed((): boolean => !vModel.vModel.value.id),
  };
  return {
    ...syncedDialog,
    ...vModel,
    ...toRefs(state),
    ...computes,
    ...methods,
  };
}
