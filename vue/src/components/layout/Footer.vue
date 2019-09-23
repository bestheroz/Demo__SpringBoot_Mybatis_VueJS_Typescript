<template>
  <v-footer app>
    <div class="flex-grow-1" />
    <div>&copy; {{ new Date().getFullYear() }}</div>
  </v-footer>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
@Component({})
export default class Footer extends Vue {}
</script>

<style scoped></style>
