<template>
  <v-snackbar
    v-model="snackbar"
    :bottom="position === 'bottom'"
    :color="color"
    :left="position === 'left'"
    :right="position === 'right'"
    :timeout="timeout"
    :top="position === 'top'"
  >
    {{ text }}
    <v-btn dark text @click="updateAlert">
      Close
    </v-btn>
  </v-snackbar>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";
import { AlertOptions } from "@/components/alert/common/types";
import _ from "lodash";
import { ApiDataResult } from "@/utils/api";

@Component({})
export default class Alert extends Vue {
  @Prop({
    type: Object,
    default: {}
  })
  options!: AlertOptions;

  color?: string = "info"; // 'success'| 'info'| 'error'| 'cyan darken-2'
  position?: string = "top";
  snackbar: boolean = false;
  text?: string = "";
  timeout?: number = 5000;

  @Watch("options.color")
  watchOptionColor(value: string): void {
    this.color = value;
  }

  @Watch("options.position")
  watchOptionPosition(value: string): void {
    this.position = value;
  }

  @Watch("options.snackbar")
  watchOptionSnackbar(value: boolean): void {
    this.snackbar = value;
  }

  @Watch("options.text")
  watchOptionText(value: string): void {
    this.text = value;
  }

  @Watch("options.timeout")
  watchOptionTimeout(value: number): void {
    this.timeout = value;
  }

  @Watch("options.result", { deep: true })
  watchOptionResult(value: ApiDataResult<any>): void {
    this.text = value.responseMessage;
    if (value && _.startsWith(value.responseCode, "S")) {
      this.color = "success";
    } else if (value && _.startsWith(value.responseCode, "F")) {
      this.color = "error";
    }
    this.snackbar = true;
  }

  @Emit()
  updateAlert(): boolean {
    return false
  }
}
</script>

<style scoped></style>
