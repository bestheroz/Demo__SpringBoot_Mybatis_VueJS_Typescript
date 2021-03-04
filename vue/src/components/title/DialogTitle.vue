<template>
  <div>
    <v-card-title class="pb-2">
      <v-icon
        v-text="
          isNew ? 'mdi-database-plus-outline' : 'mdi-database-edit-outline'
        "
        :size="28"
        class="mr-2"
      />
      {{ title }}
      <v-spacer />
      <slot name="buttons" />
    </v-card-title>
    <v-system-bar height="10" color="secondary" class="mb-4" />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({
  name: "DialogTitle",
  components: {},
})
export default class extends Vue {
  @Prop({}) readonly prefix!: string;
  @Prop({}) readonly text!: string;
  @Prop({}) readonly isNew!: string;

  get title(): string {
    if (this.text) {
      return this.text;
    } else if (this.prefix) {
      return `${this.prefix} ${this.isNew ? "추가" : "수정"} `;
    } else {
      return "";
    }
  }
}
</script>
