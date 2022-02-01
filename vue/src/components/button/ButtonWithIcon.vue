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
import { defineComponent } from "@vue/composition-api";

export default defineComponent({
  props: {
    icon: { required: true, type: String },
    text: { required: true, type: String },
    color: { default: "primary", type: String },
    disabled: { type: Boolean },
    loading: { type: Boolean },
    small: { type: Boolean },
    xSmall: { type: Boolean },
    large: { type: Boolean },
    xLarge: { type: Boolean },
    block: { type: Boolean },
  },
});
</script>
