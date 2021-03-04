<template>
  <v-main :title="null">
    <v-system-bar
      v-if="title"
      :color="$vuetify.theme.dark ? '#1E1E1E' : '#FFFFFF'"
      :height="40"
      class="pt-4 pl-4"
    >
      <v-icon :size="28" style="top: -1px" v-text="icon" v-if="icon" />
      <v-card-title v-text="title" />
    </v-system-bar>
    <v-container fluid class="pa-0">
      <router-view />
    </v-container>
  </v-main>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import type { DrawerItem } from "@/common/types";
import { errorPage } from "@/utils/errors";
import { getVariableApi } from "@/utils/apis";
import { defaultTableMenuEntity } from "@/common/values";

@Component({ name: "Viewer" })
export default class extends Vue {
  icon: string | null = null;

  get title(): string {
    if (this.$route.fullPath === "/index") {
      return "";
    }
    if (this.$store.getters.drawers?.length > 0) {
      return (this.findThisPage().name || "").split("(팝업)").join("");
    }
    return "";
  }

  @Watch("title")
  protected async watchTitle(val: string): Promise<void> {
    document.title = ((await getVariableApi("title")) || "") + `:: ${val}`;
  }

  protected findThisPage(): DrawerItem {
    let result: DrawerItem | undefined;
    if (this.$route.name) {
      return defaultTableMenuEntity();
    }
    this.$store.getters.drawers?.forEach((drawer: DrawerItem) => {
      if (!result) {
        result = drawer.children?.find((child: DrawerItem) => {
          return child?.url === this.$route.fullPath;
        });
        this.icon = drawer.icon || "mdi-file-document-outline";
      }
    });
    if (!result) {
      errorPage(403);
      return defaultTableMenuEntity();
    }
    return result || defaultTableMenuEntity();
  }
}
</script>
