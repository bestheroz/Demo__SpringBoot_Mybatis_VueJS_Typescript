<template>
  <div>
    <v-btn
      :color="color"
      @click="$emit('click')"
      :disabled="disabled"
      :loading="loading"
      :small="small"
      :x-small="xSmall"
      :large="large"
      :x-large="xLarge"
      :block="block"
    >
      <v-icon v-text="icon" class="mr-1" />
      {{ text }}
    </v-btn>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({
  components: {},
})
export default class extends Vue {
  @Prop({ required: true }) readonly text!: string;
  @Prop({ required: true }) readonly icon!: string;
  @Prop({ default: "primary" }) readonly color!: string;
  @Prop({ type: Boolean }) readonly disabled!: boolean;
  @Prop({ type: Boolean }) readonly loading!: boolean;
  @Prop({ type: Boolean }) readonly small!: boolean;
  @Prop({ type: Boolean }) readonly xSmall!: boolean;
  @Prop({ type: Boolean }) readonly large!: boolean;
  @Prop({ type: Boolean }) readonly xLarge!: boolean;
  @Prop({ type: Boolean }) readonly block!: boolean;
}
</script>
