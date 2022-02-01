import { computed } from "@vue/composition-api";

export default function (props: { dialog: boolean }, emit: unknown) {
  const computes = {
    syncedDialog: computed({
      get(): boolean {
        return props.dialog;
      },
      set(value: boolean) {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        emit("update:dialog", value);
      },
    }),
  };
  return { ...computes };
}
