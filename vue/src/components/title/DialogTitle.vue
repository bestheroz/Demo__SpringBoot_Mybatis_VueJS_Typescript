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
      {{ text }} {{ isNew ? "추가" : "수정" }}
      <v-spacer />
      <slot name="buttons" />
    </v-card-title>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";

@Component({
  name: "DialogTitle",
  components: {},
})
export default class extends Vue {
  @Prop({ required: true }) readonly text: string;
  @Prop({ required: true }) readonly isNew: string;
}
</script>
