<template>
  <div>
    <div
      :class="textEllipsisTargetId"
      :style="{
        'text-overflow': 'ellipsis',
        overflow: 'hidden',
        'white-space': 'nowrap',
        'word-break': 'break-all',
        cursor: 'help',
      }"
    >
      <v-tooltip
        top
        v-model="show"
        v-if="value"
        color="secondary"
        :max-width="tooltipWidth"
        open-on-click
        :open-on-hover="false"
      >
        <template #activator="{ on, attrs }">
          <div
            v-on="on"
            v-bind="attrs"
            @click="show = !show"
            :style="{
              'max-width': maxWidth + '!important',
              'text-overflow': 'ellipsis',
              overflow: 'hidden',
            }"
          >
            {{ value }}
          </div>
        </template>
        <div @click="show = !show">{{ value }}</div>
      </v-tooltip>
      <span v-else>{{ value }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import { debounce, DebouncedFunc, uniqueId } from "lodash-es";
import {
  computed,
  defineComponent,
  reactive,
  toRefs,
  watch,
} from "@vue/composition-api";

export default defineComponent({
  props: {
    value: { type: String, required: true },
    width: { type: [String, Number], default: "25rem" },
    tooltipWidth: { type: [String, Number], default: "25rem" },
  },
  setup(props) {
    const state = reactive({ show: false });
    const computes = {
      textEllipsisTargetId: computed((): string =>
        uniqueId("text-ellipsis-target-"),
      ),
      debounce: computed((): DebouncedFunc<() => Record<string, never>> => {
        return debounce(methods.debounceTextEllipsis, 100);
      }),
      maxWidth: computed((): string =>
        typeof props.width === "string" ? props.width : `${props.width}px`,
      ),
    };
    const methods = {
      debounceTextEllipsis: (): void => {
        Promise.resolve()
          .then(() => {
            document
              .querySelectorAll<HTMLElement>(
                `.${computes.textEllipsisTargetId.value}`,
              )
              .forEach((item) => item.classList.remove("text-ellipsis"));
          })
          .then(() => {
            document
              .querySelectorAll<HTMLElement>(
                `.${computes.textEllipsisTargetId.value}`,
              )
              .forEach((item) => {
                item.style.maxWidth = item.offsetWidth + "px";
                item.classList.add("text-ellipsis");
              });
          });
      },
    };
    watch(
      () => props.value,
      () => {
        computes.debounce.value && computes.debounce.value();
      },
      { immediate: true },
    );
    return { ...toRefs(state), ...computes, ...methods };
  },
});
</script>
<style lang="scss">
.v-application {
  table {
    td,
    th {
      white-space: nowrap;
    }
  }
  .v-tooltip__content {
    pointer-events: initial;
    span {
      white-space: pre-wrap !important;
      word-break: break-all;
    }
  }
}
</style>
