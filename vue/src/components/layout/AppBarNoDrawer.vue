<template>
  <v-app-bar
    absolute
    :dark="$vuetify.theme.dark"
    color="transparent"
    flat
    height="85"
    v-if="!isPopup"
  >
    <v-container>
      <v-row align="center">
        <v-toolbar-title>
          <v-btn x-large text @click="goHome" color="primary">
            {{ title }}
          </v-btn>
        </v-toolbar-title>
        <v-spacer />
        <v-toolbar-title
          class="font-weight-light"
          :style="{ cursor: 'pointer' }"
          @click="logout"
          v-if="$router.currentRoute.path !== '/login' && accessToken"
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
import { logout } from "@/utils/authentications";

@Component({ name: "AppBarNoDrawer" })
export default class extends Vue {
  readonly logout: typeof logout = logout;
  title: string | null = null;

  get isPopup(): boolean {
    return (
      !window.toolbar.visible || this.$route.fullPath.indexOf("/popup") === 0
    );
  }

  get accessToken() {
    return window?.localStorage?.getItem("accessToken");
  }

  async beforeMount(): Promise<void> {
    this.title = await getVariableApi("title");
  }

  goHome() {
    this.$router.currentRoute.path !== "/" && this.$router.push("/");
  }
}
</script>
