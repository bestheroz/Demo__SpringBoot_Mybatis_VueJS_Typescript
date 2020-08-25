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
    if (
      !window.localStorage.getItem('accessToken') ||
      !window.localStorage.getItem('refreshToken')
    ) {
      await this.$router.push('/login');
      return;
    }
    await this.$router.push('/index');
  }
}
</script>
