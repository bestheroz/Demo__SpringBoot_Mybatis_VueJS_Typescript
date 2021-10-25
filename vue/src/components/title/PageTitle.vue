<template>
  <div class="d-flex align-center pb-2">
    <div class="display-1" v-text="_title" />
    <v-spacer />
    <v-menu bottom left v-if="moreActions">
      <template #activator="{ on, attrs }">
        <v-btn
          icon
          large
          v-bind="attrs"
          v-on="on"
          class="mr-1"
          color="secondary"
        >
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
      </template>

      <slot name="list" />
    </v-menu>
    <button-icon-tooltip
      :text="buttonText"
      :icon="buttonIcon"
      large
      @click="$emit('click')"
      v-if="!hideButton && $store.getters.writeAuthority"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import { RoleMenuMap } from "@/definitions/models";

@Component({
  components: { ButtonIconTooltip },
})
export default class extends Vue {
  @Prop({}) readonly title!: string;
  @Prop({ default: "추가" }) readonly buttonText!: string;
  @Prop({ default: "mdi-plus" }) readonly buttonIcon!: string;
  @Prop({ type: Boolean }) readonly moreActions!: boolean;
  @Prop({ type: Boolean }) readonly hideButton!: boolean;

  get _title(): string {
    return (
      this.title ||
      this.$store.getters.flatAuthorities.find(
        (f: RoleMenuMap) => f.menu.url === this.$route.path,
      ).menu.name
    );
  }
}
</script>
