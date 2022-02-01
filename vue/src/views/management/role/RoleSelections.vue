<template>
  <div>
    <v-select
      v-model="vModel"
      :items="items"
      item-text="name"
      :label="label"
      :error-messages="errorMessages"
      :loading="loading"
      :clearable="clearable"
      return-object
      :class="required ? 'required' : undefined"
      :disabled="disabled"
    />
  </div>
</template>

<script lang="ts">
import { getApi } from "@/utils/apis";
import type { Role } from "@/definitions/models";
import { defaultRole } from "@/definitions/defaults";

import {
  defineComponent,
  onBeforeMount,
  PropType,
  reactive,
  toRefs,
} from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  props: {
    value: {
      type: Object as PropType<Role>,
      required: true,
      default: () => defaultRole(),
    },
    errorMessages: {
      type: Array as PropType<string[]>,
      default: undefined,
    },
    label: {
      type: String,
      default: "역할",
    },
    clearable: {
      type: Boolean,
    },
    required: {
      type: Boolean,
    },
    disabled: {
      type: Boolean,
    },
    paramAvailable: {
      type: Boolean,
    },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<Role>(props, emit);
    const state = reactive({ items: [] as Role[], loading: false });
    onBeforeMount(async () => {
      state.loading = true;
      const response = await getApi<Role[]>(
        `roles/selections/?available=${
          !props.disabled && props.paramAvailable !== undefined
            ? props.paramAvailable
            : ""
        }`,
      );
      state.items = response.data || [];
      state.loading = false;
    });
    return { ...vModel, ...toRefs(state) };
  },
});
</script>
