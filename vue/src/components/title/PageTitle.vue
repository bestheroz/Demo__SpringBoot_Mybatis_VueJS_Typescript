<template>
  <div class="d-flex align-center pb-2">
    <div class="text-h4" v-text="_title" />
    <v-spacer />
    <v-menu
      bottom
      left
      transition="slide-x-reverse-transition"
      v-if="!hideMoreActions"
    >
      <template #activator="{ on, attrs }">
        <v-btn
          icon
          x-large
          :loading="buttonLoading"
          v-bind="attrs"
          v-on="on"
          class="mr-1"
          color="secondary"
        >
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
      </template>

      <v-list class="pa-0">
        <slot name="more-buttons" />
      </v-list>
    </v-menu>
    <button-icon-tooltip
      :text="buttonText"
      :icon="buttonIcon"
      :loading="buttonLoading"
      class-name="px-2"
      large
      @click="$emit('click')"
      v-if="!hideButton && $store.getters.writeAuthority"
    />
  </div>
</template>

<script lang="ts">
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import { RoleMenuMap } from "@/definitions/models";
import store from "@/store";
import { computed, defineComponent } from "@vue/composition-api";
import router from "@/router";

export default defineComponent({
  components: { ButtonIconTooltip },
  props: {
    title: { type: String, default: undefined },
    buttonText: { type: String, default: "추가" },
    buttonLoading: { type: Boolean },
    buttonIcon: { type: String, default: "mdi-plus" },
    hideMoreActions: { type: Boolean },
    hideButton: { type: Boolean },
  },
  setup(props) {
    const computes = {
      _title: computed(
        (): string =>
          props.title ||
          store.getters.flatAuthorities.find(
            (f: RoleMenuMap) => f.menu.url === router.app.$route.path,
          ).menu.name,
      ),
    };
    return { ...computes };
  },
});
</script>
