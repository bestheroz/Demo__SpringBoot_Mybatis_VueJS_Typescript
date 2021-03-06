<template>
  <div>
    <v-speed-dial
      v-model="fab"
      absolute
      top
      right
      direction="left"
      open-on-hover
      style="right: 1rem; top: 0.75rem"
    >
      <template #activator>
        <v-btn v-model="fab" color="info" fab small outlined :loading="loading">
          <v-icon v-if="fab">mdi-close-outline</v-icon>
          <v-icon v-else>mdi-function</v-icon>
        </v-btn>
      </template>
      <slot name="afterReloadButton" />
      <v-tooltip v-if="reloadButton" top>
        <template #activator="{ on }">
          <v-btn
            fab
            color="primary"
            :disabled="disabled || reloadDisabled || loading"
            @click="clickReload"
            v-on="on"
          >
            <v-icon>mdi-reload</v-icon>
          </v-btn>
        </template>
        <span> {{ reloadText }} </span>
      </v-tooltip>
      <slot name="afterSaveButton" />
      <v-tooltip v-if="saveButton" top>
        <template #activator="{ on }">
          <v-btn
            fab
            color="warning"
            :disabled="disabled || saveDisabled || loading"
            @click="clickSave"
            v-on="on"
          >
            <v-icon>mdi-content-save-outline</v-icon>
          </v-btn>
        </template>
        <span> {{ saveText }} </span>
      </v-tooltip>
      <slot name="afterExcelButton" />
      <v-tooltip v-if="excelButton" top>
        <template #activator="{ on }">
          <v-btn
            fab
            :color="excelColor"
            :disabled="disabled || excelDisabled || loading"
            @click="clickExcel"
            v-on="on"
          >
            <v-icon>mdi-file-excel-outline</v-icon>
          </v-btn>
        </template>
        <span> {{ excelText }} </span>
      </v-tooltip>
      <slot name="afterDeleteButton" />
      <v-tooltip v-if="deleteButton" top>
        <template #activator="{ on }">
          <v-btn
            fab
            color="error"
            :disabled="disabled || deleteDisabled || loading"
            @click="clickDelete"
            v-on="on"
          >
            <v-icon>mdi-delete-outline</v-icon>
          </v-btn>
        </template>
        <span> {{ deleteText }} </span>
      </v-tooltip>
      <slot name="afterAddButton" />
      <v-tooltip v-if="addButton" top>
        <template #activator="{ on }">
          <v-btn
            fab
            color="warning"
            :disabled="disabled || addDisabled || loading"
            @click="clickAdd"
            v-on="on"
          >
            <v-icon>mdi-plus-outline</v-icon>
          </v-btn>
        </template>
        <span> {{ addText }} </span>
      </v-tooltip>
      <slot name="afterInquiryButton" />
      <v-tooltip v-if="inquiryButton" top>
        <template #activator="{ on }">
          <v-btn
            fab
            color="primary"
            :disabled="disabled || inquiryDisabled || loading"
            @click="clickInquiry"
            v-on="on"
          >
            <v-icon>mdi-magnify-outline</v-icon>
          </v-btn>
        </template>
        <span> {{ inquiryText }} </span>
      </v-tooltip>
      <slot name="beforeInquiryButton" />
    </v-speed-dial>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue } from "vue-property-decorator";
import colors from "vuetify/lib/util/colors";

@Component({ name: "ButtonSet" })
export default class extends Vue {
  @Prop({ type: Boolean, default: false }) readonly loading!: boolean;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly addButton!: boolean;
  @Prop({ type: Boolean, default: false }) readonly addDisabled!: boolean;
  @Prop({ type: String, default: "추가" }) readonly addText!: string;
  @Prop({ type: Boolean, default: false }) readonly deleteButton!: boolean;
  @Prop({ type: Boolean, default: false }) readonly deleteDisabled!: boolean;
  @Prop({ type: String, default: "삭제" }) readonly deleteText!: string;
  @Prop({ type: Boolean, default: false }) readonly excelButton!: boolean;
  @Prop({ type: Boolean, default: false }) readonly excelDisabled!: boolean;
  @Prop({ type: String, default: "Excel" }) readonly excelText!: string;
  @Prop({ type: Boolean, default: false }) readonly inquiryButton!: boolean;
  @Prop({ type: Boolean, default: false }) readonly inquiryDisabled!: boolean;
  @Prop({ type: String, default: "조회" }) readonly inquiryText!: string;
  @Prop({ type: Boolean, default: false }) readonly reloadButton!: boolean;
  @Prop({ type: Boolean, default: false }) readonly reloadDisabled!: boolean;
  @Prop({ type: String, default: "새로고침" }) readonly reloadText!: string;
  @Prop({ type: Boolean, default: false }) readonly saveButton!: boolean;
  @Prop({ type: Boolean, default: false }) readonly saveDisabled!: boolean;
  @Prop({ type: String, default: "저장" }) readonly saveText!: string;

  fab = false;

  get excelColor(): string {
    return this.$vuetify.theme.dark ? colors.teal.darken4 : colors.teal.darken2;
  }

  /* eslint-disable @typescript-eslint/no-empty-function */
  @Emit("click:add") clickAdd(): void {}

  @Emit("click:delete") clickDelete(): void {}

  @Emit("click:excel") clickExcel(): void {}

  @Emit("click:inquiry") clickInquiry(): void {}

  @Emit("click:reload") clickReload(): void {}

  @Emit("click:save") clickSave(): void {}
  /* eslint-enable @typescript-eslint/no-empty-function */
}
</script>
