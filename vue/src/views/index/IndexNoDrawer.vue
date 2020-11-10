<template>
  <v-app>
    <app-bar-no-drawer />
    <view-no-drawer />
  </v-app>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import AppBarNoDrawer from "@/components/layout/AppBarNoDrawer.vue";
import ViewNoDrawer from "@/components/layout/ViewNoDrawer.vue";

@Component({
  name: "IndexNoDrawer",
  components: {
    ViewNoDrawer,
    AppBarNoDrawer,
  },
})
export default class extends Vue {}
</script>
