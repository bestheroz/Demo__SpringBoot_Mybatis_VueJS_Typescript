<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({
  name: 'Redirect',
})
export default class extends Vue {
  async mounted() {
    await this.$router.push('/admin/menu');
  }
}
</script>
