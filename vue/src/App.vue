<template>
  <v-app>
    <!-- Layout component -->
    <component :is="currentLayout" v-if="isRouterLoaded">
      <transition name="fade" mode="out-in">
        <router-view />
      </transition>
    </component>
  </v-app>
</template>

<script lang="ts">
import "@/scss/common.scss";
import { Component, Vue, Watch } from "vue-property-decorator";

import AuthLayout from "@/layouts/AuthLayout.vue";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ErrorLayout from "@/layouts/ErrorLayout.vue";
import Vuetify from "@/plugins/vuetify";
import envs from "@/constants/envs";
// eslint-disable-next-line camelcase
import jwt_decode from "jwt-decode";
import dayjs from "dayjs";
import { signOut } from "@/utils/commands";
import type { RoleMenuMap } from "@/definitions/models";

@Component({
  components: {
    AuthLayout,
    DefaultLayout,
    ErrorLayout,
  },
})
export default class extends Vue {
  get isRouterLoaded(): boolean {
    return this.$route.name !== null;
  }

  get currentLayout(): string {
    return (this.$route.meta?.layout || "default") + "Layout";
  }

  protected async mounted(): Promise<void> {
    document.title = envs.PRODUCT_TITLE;
  }

  @Watch("$route.fullPath")
  protected watchRouteFullPath(val: string): void {
    this.$store.commit("reloadCurrentAuthority", val);
  }

  @Watch("$store.getters.currentAuthority", { immediate: true })
  watchCurrentAuthority(val: RoleMenuMap): void {
    const pageTitle = val?.menu?.name;
    document.title = pageTitle
      ? `${envs.PRODUCT_TITLE}::${pageTitle}`
      : envs.PRODUCT_TITLE;
  }

  @Watch("$store.getters.accessToken", { immediate: true })
  watchAccessToken(val: string): void {
    if (!val) {
      signOut();
      return;
    }
    try {
      if (
        dayjs((jwt_decode(val) as { exp: number }).exp * 1000).isBefore(dayjs())
      ) {
        this.$store.dispatch("reIssueAccessToken");
      }
    } catch (e: unknown) {
      signOut();
    }
  }

  @Watch("$store.getters.primaryColor", { immediate: true })
  watchPrimaryColor(val: string): void {
    Vuetify.framework.theme.themes.dark.primary = val;
    Vuetify.framework.theme.themes.light.primary = val;
  }
}
</script>
<style scoped>
/**
 * Transition animation between pages
 */
.fade-enter-active,
.fade-leave-active {
  transition-duration: 0.2s;
  transition-property: opacity;
  transition-timing-function: ease;
}

.fade-enter,
.fade-leave-active {
  opacity: 0;
}
</style>
