<template>
  <div>
    <v-tooltip
      :bottom="bottom || defaultPosition"
      :top="top"
      :left="left"
      :right="right"
    >
      <template #activator="{ on, attrs }">
        <v-btn
          :color="color"
          @click="$emit('click')"
          v-bind="attrs"
          v-on="on"
          :disabled="disabled"
          :loading="loading"
          :small="small"
          :x-small="xSmall"
          :large="large"
          :x-large="xLarge"
          :block="block"
        >
          <v-icon
            v-text="icon"
            :size="size"
            :small="small"
            :x-small="xSmall"
            :large="large"
            :x-large="xLarge"
          />
        </v-btn>
      </template>
      <span v-text="text" />
    </v-tooltip>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({
  components: {},
})
export default class extends Vue {
  @Prop({ required: true }) readonly icon!: string;
  @Prop({ required: true }) readonly text!: string;
  @Prop({ default: "primary" }) readonly color!: string;
  @Prop({ type: Boolean }) readonly disabled!: boolean;
  @Prop({ type: Boolean }) readonly loading!: boolean;
  @Prop({ type: Boolean }) readonly bottom!: boolean;
  @Prop({ type: Boolean }) readonly left!: boolean;
  @Prop({ type: Boolean }) readonly top!: boolean;
  @Prop({ type: Boolean }) readonly right!: boolean;
  @Prop({ type: Boolean }) readonly small!: boolean;
  @Prop({ type: Boolean }) readonly xSmall!: boolean;
  @Prop({ type: Boolean }) readonly large!: boolean;
  @Prop({ type: Boolean }) readonly xLarge!: boolean;
  @Prop({ type: Boolean }) readonly block!: boolean;
  @Prop({}) readonly size!: number | string;

  get defaultPosition(): boolean {
    return !this.top && !this.bottom && !this.right && !this.left;
  }
}
</script>
