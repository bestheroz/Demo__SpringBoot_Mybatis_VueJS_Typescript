<template>
  <div>
    <div class="text-right mr-4" style="opacity: 0.7">
      <span v-if="createdDateTimeString">
        Created
        <v-icon size="16" style="vertical-align: initial">
          mdi-clock-outline
        </v-icon>

        {{ createdDateTimeString }}
      </span>
      <span
        class="px-2 grey--text text--darken-1"
        v-if="createdDateTimeString && updatedDateTimeString"
        >|</span
      >
      <span v-if="updatedDateTimeString">
        Updated
        <v-icon size="16" style="vertical-align: initial">
          mdi-clock-check-outline
        </v-icon>

        {{ updatedDateTimeString }}
      </span>
    </div>
  </div>
</template>

<script lang="ts">
import { formatDatetime } from "@/utils/formatter";
import { computed, defineComponent, PropType } from "@vue/composition-api";
import { DateTime } from "@/definitions/types";

export default defineComponent({
  props: {
    createdDateTime: {
      type: [String, Number, Date, Object] as PropType<DateTime>,
      default: undefined,
    },
    updatedDateTime: {
      type: [String, Number, Date, Object] as PropType<DateTime>,
      default: undefined,
    },
  },
  setup(props) {
    const computes = {
      createdDateTimeString: computed((): string => {
        if (!props.createdDateTime) {
          return "";
        }
        return formatDatetime(props.createdDateTime);
      }),
      updatedDateTimeString: computed((): string => {
        if (!props.updatedDateTime) {
          return "";
        }
        return formatDatetime(props.updatedDateTime);
      }),
    };
    return { ...computes };
  },
});
</script>
