<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class Index extends Vue {
  async created() {
    await this.$store.commit('loginCheck');
    if (this.$store.state.accessToken === null) {
      this.$router.push('/login');
    } else {
      this.$router.push('/manage/member');
    }
  }
}
</script>

<style scoped></style>
