<template>
  <div>
    <v-card-title class="text-h4 py-2">
      <v-icon
        v-text="
          isNew ? 'mdi-database-plus-outline' : 'mdi-database-edit-outline'
        "
        :size="28"
        class="mr-2"
      />
      {{ title }}
      <v-spacer />
      <v-switch class="d-none" />
      <slot name="utils"></slot>
      <v-switch
        v-model="vModel"
        :label="getSwitchLabel(vModel, switchText)"
        inset
        color="primary"
        :disabled="disabledSwitch || !$store.getters.writeAuthority"
        class="pr-4"
        v-if="withSwitch"
      />
      <v-tooltip bottom>
        <template #activator="{ on, attrs }">
          <v-btn
            icon
            fab
            @click="$emit('click:close')"
            v-bind="attrs"
            v-on="on"
          >
            <v-icon v-text="'mdi-window-close'" large />
          </v-btn>
        </template>
        <span v-text="'닫기'" />
      </v-tooltip>
    </v-card-title>
  </div>
</template>

<script lang="ts">
import { getSwitchLabel } from "@/utils/formatter";
import {
  computed,
  defineComponent,
  PropType,
  reactive,
  toRefs,
} from "@vue/composition-api";
import store from "@/store";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  props: {
    value: {
      type: Boolean,
    },
    withSwitch: {
      type: Boolean,
    },
    switchText: {
      type: Array as PropType<string[]>,
      default: undefined,
    },
    disabledSwitch: {
      type: Boolean,
    },
    prefix: {
      type: String,
      default: undefined,
    },
    text: {
      type: String,
      default: undefined,
    },
    isNew: {
      type: Boolean,
    },
    suffix: {
      type: String,
      default: undefined,
    },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<boolean>(props, emit);
    const state = reactive({ getSwitchLabel: getSwitchLabel });
    const computes = {
      title: computed((): string => {
        if (props.text) {
          return props.text;
        } else if (props.prefix) {
          return `${props.prefix} ${
            props.suffix
              ? props.suffix
              : store.getters.writeAuthority
              ? props.isNew
                ? "등록"
                : "수정"
              : "보기"
          } `;
        } else {
          return "";
        }
      }),
    };
    return { ...vModel, ...toRefs(state), ...computes };
  },
});
</script>
