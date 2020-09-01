<template>
  <router-view />
</template>

<script lang="ts">
import '@/scss/common.scss';
import { Component, Vue } from 'vue-property-decorator';
import { getVariableApi } from '@/utils/apis';

@Component({ name: 'App' })
export default class extends Vue {
  title: string | null = null;

  async mounted() {
    // @ts-ignore
    document.title = await getVariableApi('title');
    this.$vuetify.theme.dark =
      (window.localStorage.getItem('theme') || 'dark') === 'dark';
  }
}
</script>
