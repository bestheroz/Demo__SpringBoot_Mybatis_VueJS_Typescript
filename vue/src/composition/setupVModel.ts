import { computed } from "@vue/composition-api";

export default function <T>(props: { value: T }, emit: unknown) {
  const computes = {
    vModel: computed({
      get(): T {
        return props.value;
      },
      set(value: T) {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        emit("input", value);
      },
    }),
  };
  return { ...computes };
}
