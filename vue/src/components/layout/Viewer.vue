<template>
  <v-main :title="null">
    <v-alert
      id="pageTitle"
      border="bottom"
      colored-border
      color="divider"
      dense
      class="mx-3 mt-3 mb-0 pl-6 elevation-1"
      v-if="title"
    >
      <v-icon class="pb-1">{{ icon }}</v-icon>
      {{ title }}
    </v-alert>
    <v-container fluid class="pt-0 elevation-1">
      <router-view />
    </v-container>
  </v-main>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { DrawerItem } from "@/common/types";
import { errorPage } from "@/utils/errors";

@Component({ name: "Viewer" })
export default class extends Vue {
  drawers: DrawerItem[] = [];
  icon = "mdi-file-document-outline";

  get title(): string {
    if (this.$route.fullPath === "/index") {
      return "";
    }
    if (this.drawers?.length > 0) {
      return this.findThisPage()!.title.split("(팝업)").join("");
    }
    return "";
  }

  findThisPage(): DrawerItem | undefined {
    let result: DrawerItem | undefined;
    if (this.$route.name) {
      return { id: 0, title: "" };
    }
    this.drawers?.forEach((drawer) => {
      if (!result) {
        result = drawer.children?.find((child: DrawerItem) => {
          if (child.to) {
            return child.to === this.$route.fullPath;
          }
        });
        this.icon = drawer.icon || "mdi-file-document-outline";
      }
    });
    if (!result) {
      errorPage(403);
      return { id: 0, title: "" };
    }
    return result!;
  }

  @Watch("$store.state.drawer.drawers", { immediate: true })
  async watchDrawers() {
    this.drawers = await this.$store.dispatch("getDrawers");
  }
}
</script>
