<template>
  <div>
    <v-system-bar color="secondary" window class="mb-4 text-h6">
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
    </v-system-bar>
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
