<template>
  <div>
    <div
      class="text-ellipsis-target"
      :style="`max-width: ${maxWidth} !important;`"
    >
      <v-tooltip
        v-if="value && $store.state.temp.finishTextEllipsis"
        v-model="show"
        top
        color="secondary"
        :max-width="tooltipWidth"
        open-on-click
        :open-on-hover="false"
      >
        <template #activator="{ on, attrs }">
          <span v-bind="attrs" v-on="on" @click="show = !show">
            {{ value }}
          </span>
        </template>
        <div @click="show = !show">{{ value }}</div>
      </v-tooltip>
      <span v-else>{{ value }}</span>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({ name: "DataTableColumnEllipsis" })
export default class extends Vue {
  @Prop({ required: true }) readonly value!: string;
  @Prop({ required: false, default: "25rem" }) readonly width!: string | number;
  @Prop({ required: false, default: "25rem" }) readonly tooltipWidth!:
    | string
    | number;

  show = false;

  get maxWidth(): string {
    return typeof this.width === "string" ? this.width : `${this.width}px`;
  }
}
</script>
<style lang="scss">
.v-application {
  table {
    td,
    th {
      white-space: nowrap;
      .text-ellipsis {
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        word-break: break-all;
      }
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
