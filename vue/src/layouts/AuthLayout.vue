<template>
  <div class="d-flex text-center flex-column flex-md-row flex-grow-1">
    <v-sheet
      class="
        layout-side
        mx-auto mx-md-1
        d-none d-md-flex
        flex-md-column
        justify-space-between
        px-2
      "
    >
      <div class="mt-3 mt-md-10 pa-2">
        <div
          class="display-2 font-weight-bold primary--text"
          v-text="envs.PRODUCT_TITLE"
        />
      </div>
      <img class="w-full" src="/images/illustrations/signin-illustration.svg" />
    </v-sheet>

    <div
      class="
        pa-2 pa-md-4
        flex-grow-1
        align-center
        justify-center
        d-flex
        flex-column
      "
    >
      <div class="layout-content ma-auto w-full">
        <slot></slot>
      </div>
      <div
        class="overline mt-4"
        v-text="`${envs.PRODUCT_TITLE} - ${envs.PRODUCT_VERSION}`"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import envs from "@/constants/envs";

@Component({})
export default class extends Vue {
  readonly envs = envs;
}
</script>

<style scoped>
.layout-side {
  width: 420px;
}

.layout-content {
  max-width: 480px;
}
</style>
