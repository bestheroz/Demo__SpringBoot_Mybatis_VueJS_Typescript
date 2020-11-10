<template>
  <v-container class="error-page fill-height" tag="section">
    <v-row class="text-center" justify="center">
      <v-col cols="auto">
        <h1
          class="display-4 font-weight-black"
          style="font-size: 200px !important"
        >
          500
        </h1>

        <div class="display-3 mb-5 mt-10">Internal Server Error</div>

        <v-btn depressed to="/"> Get me out of here! </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component({
  name: "Error500",
})
export default class extends Vue {}
</script>
