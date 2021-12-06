<template>
  <div>
    <v-card-title class="display-1 py-2">
      <v-icon
        v-text="
          isNew ? 'mdi-database-plus-outline' : 'mdi-database-edit-outline'
        "
        :size="28"
        class="mr-2"
      />
      {{ title }}
      <v-spacer />
      <v-switch class="d-none" />
      <v-switch
        v-model="available"
        :label="getSwitchLabel(available, switchText)"
        inset
        color="primary"
        :disabled="disabledSwitch || !$store.getters.writeAuthority"
        class="pr-4"
        v-if="withSwitch"
      />
      <v-tooltip bottom>
        <template #activator="{ on, attrs }">
          <v-btn
            icon
            fab
            @click="$emit('click:close')"
            v-bind="attrs"
            v-on="on"
          >
            <v-icon v-text="'mdi-window-close'" large />
          </v-btn>
        </template>
        <span v-text="'닫기'" />
      </v-tooltip>
    </v-card-title>
  </div>
</template>

<script lang="ts">
import { getSwitchLabel } from "@/utils/formatter";
import { Component, Prop, VModel, Vue } from "vue-property-decorator";

@Component({
  components: {},
})
export default class extends Vue {
  @VModel({ type: Boolean }) available!: boolean;
  @Prop({ type: Boolean }) readonly withSwitch!: boolean;
  @Prop({ type: Array }) readonly switchText!: string[];
  @Prop({ type: Boolean }) readonly disabledSwitch!: boolean;
  @Prop({ type: Boolean }) readonly isNew!: boolean;
  @Prop({}) readonly prefix!: string;
  @Prop({}) readonly text!: string;
  @Prop({}) readonly suffix!: string;

  readonly getSwitchLabel = getSwitchLabel;

  get title(): string {
    if (this.text) {
      return this.text;
    } else if (this.prefix) {
      return `${this.prefix} ${
        this.suffix
          ? this.suffix
          : this.$store.getters.writeAuthority
          ? this.isNew
            ? "등록"
            : "수정"
          : "보기"
      } `;
    } else {
      return "";
    }
  }
}
</script>
