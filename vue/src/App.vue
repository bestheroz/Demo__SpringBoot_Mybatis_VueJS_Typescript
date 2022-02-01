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

import AuthLayout from "@/layouts/AuthLayout.vue";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ErrorLayout from "@/layouts/ErrorLayout.vue";
import Vuetify from "@/plugins/vuetify";
import envs from "@/constants/envs";
// eslint-disable-next-line camelcase
import jwt_decode from "jwt-decode";
import dayjs from "dayjs";
import { goSignInPage, signOut } from "@/utils/commands";
import type { RoleMenuMap } from "@/definitions/models";
import {
  computed,
  defineComponent,
  onBeforeMount,
  onMounted,
  watch,
} from "@vue/composition-api";
import router from "@/router";
import store from "@/store";

export default defineComponent({
  components: {
    AuthLayout,
    DefaultLayout,
    ErrorLayout,
  },
  setup() {
    const computes = {
      isRouterLoaded: computed((): boolean => router.app.$route.name !== null),
      currentLayout: computed(
        (): string => (router.app.$route.meta?.layout || "default") + "Layout",
      ),
    };
    onBeforeMount(async () => {
      const refreshToken = window.localStorage.getItem("refreshToken");
      const accessToken = window.localStorage.getItem("accessToken");
      if (!refreshToken || !accessToken) {
        await goSignInPage();
        return;
      }

      if (
        dayjs(
          (jwt_decode(refreshToken) as { exp: number }).exp * 1000,
        ).isBefore(dayjs())
      ) {
        await signOut();
        return;
      }
      try {
        if (
          dayjs(
            (jwt_decode(accessToken) as { exp: number }).exp * 1000,
          ).isBefore(dayjs())
        ) {
          await store.dispatch("reIssueAccessToken");
        }
      } catch (e: unknown) {
        await signOut();
      }
    });

    onMounted(() => {
      document.title = envs.PRODUCT_TITLE;
    });

    watch(
      () => router.app.$route.fullPath,
      (val: string) => {
        store.commit("reloadCurrentAuthority", val);
      },
    );
    watch(
      () => store.getters.currentAuthority,
      (val: RoleMenuMap) => {
        const pageTitle = val?.menu?.name;
        document.title = pageTitle
          ? `${envs.PRODUCT_TITLE}::${pageTitle}`
          : envs.PRODUCT_TITLE;
      },
      { immediate: true },
    );
    watch(
      () => store.getters.primaryColor,
      (val: string) => {
        Vuetify.framework.theme.themes.dark.primary = val;
        Vuetify.framework.theme.themes.light.primary = val;
      },
    );

    return { ...computes };
  },
});
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
