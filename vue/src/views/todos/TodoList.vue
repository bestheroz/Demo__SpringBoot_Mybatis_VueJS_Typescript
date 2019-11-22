<template>
  <div>
    <v-alert type="success">
      Write your code
    </v-alert>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class TodoList extends Vue {}
</script>

<style scoped></style>
