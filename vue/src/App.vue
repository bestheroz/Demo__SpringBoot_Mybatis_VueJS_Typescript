<template>
  <router-view />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getVariableApi } from '@/utils/apis';

@Component({ name: 'App' })
export default class extends Vue {
  title: string | null = null;

  async mounted() {
    // @ts-ignore
    document.title = await getVariableApi('title');
  }
}
</script>

<style lang="scss">
.v-application a {
  color: #4caf50;
}

#buttonSet {
  right: 8px;
  top: 8px;
}
</style>
