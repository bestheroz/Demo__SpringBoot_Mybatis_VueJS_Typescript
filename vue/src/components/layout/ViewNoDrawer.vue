<template>
  <v-main :title="null">
    <router-view />
  </v-main>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component({ name: "ViewNoDrawer" })
export default class extends Vue {}
</script>
