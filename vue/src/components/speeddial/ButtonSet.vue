<template>
  <div>
    <v-speed-dial
      class="button-set"
      v-model="fab"
      absolute
      top
      right
      direction="left"
      open-on-hover
    >
      <template #activator>
        <v-btn
          v-model="fab"
          color="button-default"
          elevation="5"
          dark
          fab
          small
          :loading="loading"
        >
          <v-icon v-if="fab" color="black">mdi-close</v-icon>
          <v-icon v-else>mdi-function</v-icon>
        </v-btn>
      </template>
      <v-tooltip v-if="reloadButton" top>
        <template #activator="{ on }">
          <v-btn
            elevation="5"
            fab
            dark
            color="button-reload"
            :disabled="disabled || reloadDisabled || loading"
            @click="clickReload"
            v-on="on"
          >
            <v-icon>mdi-reload</v-icon>
          </v-btn>
        </template>
        <span> {{ reloadText }} </span>
      </v-tooltip>
      <v-tooltip v-if="saveButton" top>
        <template #activator="{ on }">
          <v-btn
            elevation="5"
            fab
            dark
            color="primary"
            :disabled="disabled || saveDisabled || loading"
            @click="clickSave"
            v-on="on"
          >
            <v-icon>mdi-content-save</v-icon>
          </v-btn>
        </template>
        <span> {{ saveText }} </span>
      </v-tooltip>
      <v-tooltip v-if="excelButton" top>
        <template #activator="{ on }">
          <v-btn
            elevation="5"
            fab
            dark
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
      <v-tooltip v-if="deleteButton" top>
        <template #activator="{ on }">
          <v-btn
            elevation="5"
            fab
            dark
            color="button-delete"
            :disabled="disabled || deleteDisabled || loading"
            @click="clickDelete"
            v-on="on"
          >
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </template>
        <span> {{ deleteText }} </span>
      </v-tooltip>
      <v-tooltip v-if="addButton" top>
        <template #activator="{ on }">
          <v-btn
            elevation="5"
            fab
            dark
            color="button-add"
            :disabled="disabled || addDisabled || loading"
            @click="clickAdd"
            v-on="on"
          >
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </template>
        <span> {{ addText }} </span>
      </v-tooltip>
      <v-tooltip v-if="inquiryButton" top>
        <template #activator="{ on }">
          <v-btn
            elevation="5"
            fab
            dark
            color="button-default"
            :disabled="disabled || inquiryDisabled || loading"
            @click="clickInquiry"
            v-on="on"
          >
            <v-icon>mdi-magnify</v-icon>
          </v-btn>
        </template>
        <span> {{ inquiryText }} </span>
      </v-tooltip>
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
<style lang="scss" scoped>
.button-set {
  right: 8px;
  top: 12px;
}
</style>
