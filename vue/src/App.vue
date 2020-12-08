<template>
  <router-view />
</template>

<script lang="ts">
import "@/scss/common.scss";
import { Component, Vue, Watch } from "vue-property-decorator";
import { getVariableApi } from "@/utils/apis";

@Component({ name: "App" })
export default class extends Vue {
  title: string | null = null;

  protected async mounted(): Promise<void> {
    document.title = (await getVariableApi("title")) || "";
  }

  @Watch("$store.getters.theme", { immediate: true })
  watchTheme(val: string): void {
    this.$vuetify.theme.dark = val === "dark";
  }
}
</script>
