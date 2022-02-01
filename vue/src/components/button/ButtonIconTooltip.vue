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
          :class="className"
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
import { computed, defineComponent } from "@vue/composition-api";

export default defineComponent({
  props: {
    icon: { required: true, type: String },
    text: { required: true, type: String },
    color: { default: "primary", type: String },
    disabled: { type: Boolean },
    loading: { type: Boolean },
    bottom: { type: Boolean },
    left: { type: Boolean },
    top: { type: Boolean },
    right: { type: Boolean },
    small: { type: Boolean },
    xSmall: { type: Boolean },
    large: { type: Boolean },
    xLarge: { type: Boolean },
    block: { type: Boolean },
    className: { type: String, default: undefined },
    size: { type: [String, Number], default: undefined },
  },
  setup(props) {
    const computes = {
      defaultPosition: computed((): boolean => {
        return !props.top && !props.bottom && !props.right && !props.left;
      }),
    };
    return { ...computes };
  },
});
</script>
