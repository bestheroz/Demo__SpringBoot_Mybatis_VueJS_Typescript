<template>
  <v-app-bar
    v-if="!isPopup"
    absolute
    :dark="$vuetify.theme.dark"
    color="transparent"
    flat
    height="85"
  >
    <v-container>
      <v-row align="center">
        <v-toolbar-title>
          <v-btn x-large text color="primary" @click="goHome">
            {{ title }}
          </v-btn>
        </v-toolbar-title>
        <v-spacer />
        <v-toolbar-title
          v-if="$router.currentRoute.path !== '/login' && accessToken"
          class="font-weight-light"
          :style="{ cursor: 'pointer' }"
          @click="$store.dispatch('logout')"
        >
          <v-icon>mdi-logout</v-icon>
          로그아웃
        </v-toolbar-title>
      </v-row>
    </v-container>
  </v-app-bar>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { getVariableApi } from "@/utils/apis";

@Component({ name: "AppBarNoDrawer" })
export default class extends Vue {
  title: string | null = null;

  get isPopup(): boolean {
    return (
      !window.toolbar.visible || this.$route.fullPath.indexOf("/popup") === 0
    );
  }

  get accessToken(): string | undefined | null {
    return window?.localStorage?.getItem("accessToken");
  }

  async created(): Promise<void> {
    this.title = await getVariableApi("title");
  }

  protected goHome(): void {
    this.$router.currentRoute.path !== "/" && this.$router.push("/");
  }
}
</script>
