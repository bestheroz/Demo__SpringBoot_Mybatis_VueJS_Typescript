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
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import _, { DebouncedFunc } from "lodash";

@Component({ name: "DataTableColumnEllipsis" })
export default class extends Vue {
  @Prop({ required: true }) readonly value!: string;
  @Prop({ required: false, default: "25rem" }) readonly width!: string | number;
  @Prop({ required: false, default: "25rem" }) readonly tooltipWidth!:
    | string
    | number;
  show = false;

  readonly textEllipsisTargetId = _.uniqueId("text-ellipsis-target-");
  readonly debounce: DebouncedFunc<() => Record<string, never>> = _.debounce(
    this.debounceTextEllipsis,
    100,
  );

  get maxWidth(): string {
    return typeof this.width === "string" ? this.width : `${this.width}px`;
  }

  @Watch("value", { immediate: true })
  watchValue(): void {
    this.debounce && this.debounce();
  }

  debounceTextEllipsis(): void {
    Promise.resolve()
      .then(() => {
        document
          .querySelectorAll<HTMLElement>(`.${this.textEllipsisTargetId}`)
          .forEach((item) => item.classList.remove("text-ellipsis"));
      })
      .then(() => {
        document
          .querySelectorAll<HTMLElement>(`.${this.textEllipsisTargetId}`)
          .forEach((item) => {
            item.style.maxWidth = item.offsetWidth + "px";
            item.classList.add("text-ellipsis");
          });
      });
  }
}
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
