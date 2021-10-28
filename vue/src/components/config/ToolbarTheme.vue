<template>
  <div>
    <v-navigation-drawer
      v-model="dialog"
      fixed
      right
      hide-overlay
      temporary
      app
      width="330"
    >
      <div class="d-flex align-center pa-2">
        <div class="title">Settings</div>
        <v-spacer />
        <v-btn icon @click="dialog = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </div>

      <v-divider />

      <div class="pa-2">
        <div class="font-weight-bold my-1">Global Theme</div>
        <v-btn-toggle
          :value="$store.getters.globalTheme"
          color="primary"
          mandatory
          class="mb-2"
        >
          <v-btn
            x-large
            @click="$store.dispatch('setGlobalTheme', 'light')"
            value="light"
          >
            Light
          </v-btn>
          <v-btn
            x-large
            @click="$store.dispatch('setGlobalTheme', 'dark')"
            value="dark"
          >
            Dark
          </v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Toolbar Theme</div>
        <v-btn-toggle
          :value="$store.getters.toolbarTheme"
          color="primary"
          mandatory
          class="mb-2"
        >
          <v-btn
            x-large
            @click="$store.dispatch('setToolbarTheme', 'global')"
            value="global"
          >
            Global
          </v-btn>
          <v-btn
            x-large
            @click="$store.dispatch('setToolbarTheme', 'light')"
            value="light"
          >
            Light
          </v-btn>
          <v-btn
            x-large
            @click="$store.dispatch('setToolbarTheme', 'dark')"
            value="dark"
          >
            Dark
          </v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Menu Theme</div>
        <v-btn-toggle
          :value="$store.getters.menuTheme"
          color="primary"
          mandatory
          class="mb-2"
        >
          <v-btn
            x-large
            @click="$store.dispatch('setMenuTheme', 'global')"
            value="global"
          >
            Global
          </v-btn>
          <v-btn
            x-large
            @click="$store.dispatch('setMenuTheme', 'light')"
            value="light"
          >
            Light
          </v-btn>
          <v-btn
            x-large
            @click="$store.dispatch('setMenuTheme', 'dark')"
            value="dark"
          >
            Dark
          </v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Toolbar Style</div>
        <v-btn-toggle
          :value="$store.getters.isToolbarDetached"
          color="primary"
          mandatory
          class="mb-2"
        >
          <v-btn
            x-large
            @click="$store.dispatch('setToolbarDetached', false)"
            :value="false"
          >
            Full
          </v-btn>
          <v-btn
            x-large
            @click="$store.dispatch('setToolbarDetached', true)"
            :value="true"
          >
            Solo
          </v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Content Layout</div>
        <v-btn-toggle
          :value="$store.getters.isContentBoxed"
          color="primary"
          mandatory
          class="mb-2"
        >
          <v-btn
            x-large
            @click="$store.dispatch('setContentBoxed', false)"
            :value="false"
          >
            Fluid
          </v-btn>
          <v-btn
            x-large
            @click="$store.dispatch('setContentBoxed', true)"
            :value="true"
          >
            Boxed
          </v-btn>
        </v-btn-toggle>

        <div class="font-weight-bold my-1">Primary Color</div>

        <v-color-picker
          :value="$store.getters.primaryColor"
          mode="hexa"
          :swatches="swatches"
          @input="setPrimaryColor"
        />
        <v-btn
          v-text="'Reset All'"
          block
          large
          color="primary"
          class="my-4"
          @click="$store.dispatch('reloadConfig')"
        />
      </div>
    </v-navigation-drawer>
  </div>
</template>

<script lang="ts">
import { Component, VModel, Vue } from "vue-property-decorator";

@Component({})
export default class extends Vue {
  @VModel({ required: true, type: Boolean }) dialog!: boolean;

  timeout = 0;
  readonly swatches: string[][] = [
    ["#0096c7", "#31944f"],
    ["#EE4f12", "#46BBB1"],
    ["#ee44aa", "#55BB46"],
  ];

  protected setPrimaryColor(primaryColor: string): void {
    this.$store.dispatch("setPrimaryColor", primaryColor);
  }
}
</script>

<style lang="scss" scoped>
.drawer-button {
  position: fixed;
  top: 340px;
  right: -20px;
  z-index: 6;
  box-shadow: 1px 1px 18px #ee44aa;

  .v-icon {
    margin-left: -18px;
    font-size: 1.3rem;
  }
}
</style>
