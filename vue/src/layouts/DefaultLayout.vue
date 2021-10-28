<template>
  <div class="d-flex flex-grow-1">
    <!-- Navigation -->
    <v-navigation-drawer
      v-model="drawer"
      app
      floating
      class="elevation-1"
      :right="$vuetify.rtl"
      :light="$store.getters.menuTheme === 'light'"
      :dark="$store.getters.menuTheme === 'dark'"
    >
      <!-- Navigation menu info -->
      <template #prepend>
        <div class="pa-2">
          <div
            class="title font-weight-bold primary--text"
            v-text="envs.PRODUCT_TITLE"
          />
          <div
            class="overline grey--text"
            v-text="envs.PRODUCT_VERSION"
            style="text-transform: none"
          />
        </div>
      </template>

      <!-- Navigation menu -->
      <nav-menu :drawers="$store.getters.drawers" />
    </v-navigation-drawer>

    <!-- Toolbar -->
    <v-app-bar
      app
      :color="$store.getters.isToolbarDetached ? 'surface' : undefined"
      :flat="$store.getters.isToolbarDetached"
      :light="$store.getters.toolbarTheme === 'light'"
      :dark="$store.getters.toolbarTheme === 'dark'"
    >
      <v-card
        class="flex-grow-1 d-flex"
        :class="[
          $store.getters.isToolbarDetached ? 'pa-1 mt-3 mx-1' : 'pa-0 ma-0',
        ]"
        :flat="!$store.getters.isToolbarDetached"
      >
        <div class="d-flex flex-grow-1 align-center">
          <div class="d-flex flex-grow-1 align-center">
            <v-app-bar-nav-icon @click.stop="drawer = !drawer" />

            <v-spacer class="d-none d-lg-block" />

            <v-spacer class="d-block d-sm-none" />

            <toolbar-admin />
            <h4 class="primary--text mt-1" v-text="$store.getters.admin.name" />
          </div>
        </div>
      </v-card>
    </v-app-bar>

    <v-main>
      <v-container class="fill-height" :fluid="!$store.getters.isContentBoxed">
        <v-layout>
          <slot />
        </v-layout>
      </v-container>

      <v-footer app inset>
        <v-spacer />
        <div class="overline">
          <a
            class="text-decoration-none"
            href="https://github.com/bestheroz"
            target="_blank"
          >
            @bestheroz
          </a>
          <v-icon small color="pink"> mdi-heart </v-icon>
        </div>
      </v-footer>
    </v-main>
  </div>
</template>

<script lang="ts">
import ToolbarAdmin from "../components/toolbar/ToolbarAdmin.vue";
import { Component, Vue } from "vue-property-decorator";
import envs from "@/constants/envs";
import NavMenu from "@/components/navigation/NavMenu.vue";

@Component({
  components: {
    NavMenu,
    ToolbarAdmin,
  },
})
export default class extends Vue {
  drawer = null;
  readonly envs = envs;
}
</script>
