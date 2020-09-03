<template>
  <span>{{ params.value | formatDatetime }}</span>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  name: 'RenderColumnDateTime',
  components: {},
})
export default class extends Vue {
  params: { value?: number | null } = { value: null };
}
</script>
