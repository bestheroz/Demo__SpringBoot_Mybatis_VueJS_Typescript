<template>
  <span>
    <v-checkbox v-model="params.value" readonly hide-details class="mt-1" />
  </span>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  name: 'RenderColumnCheckbox',
  components: {},
})
export default class extends Vue {
  params: { value?: string | null } = { value: null };
}
</script>
