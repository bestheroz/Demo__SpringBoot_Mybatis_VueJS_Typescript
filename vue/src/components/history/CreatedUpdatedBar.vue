<template>
  <div>
    <div class="text-right mr-4" style="opacity: 0.7">
      <span v-if="createdDateTimeString">
        Created
        <v-icon size="16" style="vertical-align: initial">
          mdi-clock-outline
        </v-icon>

        {{ createdDateTimeString }}
      </span>
      <span
        class="px-2 grey--text text--darken-1"
        v-if="createdDateTimeString && updatedDateTimeString"
        >|</span
      >
      <span v-if="updatedDateTimeString">
        Updated
        <v-icon size="16" style="vertical-align: initial">
          mdi-clock-check-outline
        </v-icon>

        {{ updatedDateTimeString }}
      </span>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import type { DateTime } from "@/definitions/types";
import { formatDatetime } from "@/utils/formatter";

@Component({
  components: {},
})
export default class extends Vue {
  @Prop() readonly createdDateTime!: DateTime;
  @Prop() readonly updatedDateTime!: DateTime;

  get createdDateTimeString(): string {
    if (!this.createdDateTime) {
      return "";
    }
    return formatDatetime(this.createdDateTime);
  }
  get updatedDateTimeString(): string {
    if (!this.updatedDateTime) {
      return "";
    }
    return formatDatetime(this.updatedDateTime);
  }
}
</script>
