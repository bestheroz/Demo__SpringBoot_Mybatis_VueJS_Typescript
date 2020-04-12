<template>
  <v-content :title="null">
    <router-view />
  </v-content>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component({ name: 'ViewNoDrawer' })
export default class extends Vue {}
</script>
