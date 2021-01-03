<template>
  <v-main :title="null">
    <v-alert
      v-if="title"
      id="pageTitle"
      border="bottom"
      colored-border
      color="divider"
      dense
      class="mt-1 mb-0 pl-8"
    >
      <v-icon :size="20" style="top: -1px">{{ icon }}</v-icon>
      {{ title }}
    </v-alert>
    <v-container fluid class="pa-0 elevation-1">
      <router-view />
    </v-container>
  </v-main>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import type { DrawerItem } from "@/common/types";
import { errorPage } from "@/utils/errors";
import { getVariableApi } from "@/utils/apis";

@Component({ name: "Viewer" })
export default class extends Vue {
  icon = "mdi-file-document-outline";

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
      return { id: 0, name: "" };
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
      return { id: 0, name: "" };
    }
    return result || { id: 0, name: "" };
  }
}
</script>
