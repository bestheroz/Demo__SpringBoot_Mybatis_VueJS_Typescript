<template>
  <div class="d-flex text-center flex-column flex-md-row flex-grow-1">
    <div
      class="pa-2 pa-md-4 flex-grow-1 align-center justify-center d-flex flex-column"
    >
      <div class="layout-content ma-auto w-full">
        <slot></slot>
      </div>
      <div
        class="text-overline mt-4"
        v-text="`${envs.PRODUCT_TITLE} - ${envs.PRODUCT_VERSION}`"
      />
    </div>
  </div>
</template>

<script lang="ts">
import envs from "@/constants/envs";
import { computed, defineComponent } from "@vue/composition-api";

export default defineComponent({
  setup() {
    const computes = {
      envs: computed((): typeof envs => envs),
    };

    return { ...computes };
  },
});
</script>

<style scoped>
.layout-side {
  width: 420px;
}

.layout-content {
  max-width: 480px;
}
</style>
