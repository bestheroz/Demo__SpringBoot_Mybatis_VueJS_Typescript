<template>
  <v-app>
    <drawer :drawer.sync="drawer" />
    <app-bar :drawer.sync="drawer" />
    <viewer />
  </v-app>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import AppBar from "@/components/layout/AppBar.vue";
import Drawer from "@/components/layout/Drawer.vue";
import Viewer from "@/components/layout/Viewer.vue";

@Component({
  name: "Index",
  components: {
    Viewer,
    Drawer,
    AppBar,
  },
})
export default class extends Vue {
  drawer = true;
}
</script>
