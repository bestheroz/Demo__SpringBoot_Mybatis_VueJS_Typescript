<template>
  <div>
    <v-divider />
    <v-card-actions>
      <v-spacer />
      <button-icon-tooltip
        text="닫기"
        icon="mdi-window-close"
        @click="onClickClose"
      />
      <button-icon-tooltip
        text="저장"
        icon="mdi-content-save-settings-outline"
        color="warning"
        :loading="loading"
        @click="onClickSave"
      />
    </v-card-actions>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue } from "vue-property-decorator";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";

@Component({
  name: "DialogActionButton",
  components: { ButtonIconTooltip },
})
export default class extends Vue {
  @Prop({ type: Boolean }) readonly loading!: boolean;

  @Emit("click:close")
  protected onClickClose(): void {
    // pass
  }
  @Emit("click:save")
  protected onClickSave(): void {
    // pass
  }
}
</script>
