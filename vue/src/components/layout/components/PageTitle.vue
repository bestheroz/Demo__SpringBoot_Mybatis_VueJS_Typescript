<template>
  <div>
    <v-alert
      id="pageTitle"
      border="bottom"
      colored-border
      color="divider"
      dense
      class="ma-0 mb-2"
    >
      {{ title }}
    </v-alert>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

@Component({ name: 'PageTitle' })
export default class extends Vue {
  @Prop({ type: String, required: true }) readonly title!: string;
}
</script>
