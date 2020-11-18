<template>
  <div>
    <v-speed-dial
      id="buttonSet"
      v-model="fab"
      absolute
      top
      right
      direction="left"
      open-on-hover
      transition="scale-transition"
    >
      <template #activator>
        <v-btn v-model="fab" color="button-default" dark fab small>
          <v-icon color="black" v-if="fab">mdi-close</v-icon>
          <v-icon v-else>mdi-function</v-icon>
        </v-btn>
      </template>
      <v-tooltip top v-if="reloadButton">
        <template #activator="{ on }">
          <v-btn
            fab
            dark
            x-small
            color="button-reload"
            @click="clickReload"
            :disabled="disabled || reloadDisabled"
            v-on="on"
          >
            <v-icon>mdi-reload</v-icon>
          </v-btn>
        </template>
        <span> {{ reloadText }} </span>
      </v-tooltip>
      <v-tooltip top v-if="saveButton">
        <template #activator="{ on }">
          <v-btn
            fab
            dark
            x-small
            color="primary"
            @click="clickSave"
            :disabled="disabled || saveDisabled"
            v-on="on"
          >
            <v-icon>mdi-content-save</v-icon>
          </v-btn>
        </template>
        <span> {{ saveText }} </span>
      </v-tooltip>
      <v-tooltip top v-if="excelButton">
        <template #activator="{ on }">
          <v-btn
            fab
            dark
            x-small
            :color="excelColor"
            @click="clickExcel"
            :disabled="disabled || excelDisabled"
            v-on="on"
          >
            <v-icon>mdi-file-excel-outline</v-icon>
          </v-btn>
        </template>
        <span> {{ excelText }} </span>
      </v-tooltip>
      <v-tooltip top v-if="deleteButton">
        <template #activator="{ on }">
          <v-btn
            fab
            dark
            x-small
            color="button-delete"
            @click="clickDelete"
            :disabled="disabled || deleteDisabled"
            v-on="on"
          >
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </template>
        <span> {{ deleteText }} </span>
      </v-tooltip>
      <v-tooltip top v-if="addButton">
        <template #activator="{ on }">
          <v-btn
            fab
            dark
            x-small
            color="button-add"
            @click="clickAdd"
            :disabled="disabled || addDisabled"
            v-on="on"
          >
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </template>
        <span> {{ addText }} </span>
      </v-tooltip>
      <v-tooltip top v-if="inquiryButton">
        <template #activator="{ on }">
          <v-btn
            fab
            dark
            x-small
            color="button-default"
            @click="clickInquiry"
            :disabled="disabled || inquiryDisabled"
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

  get excelColor() {
    return this.$vuetify.theme.dark ? colors.teal.darken4 : colors.teal.darken2;
  }

  @Emit("click:add") clickAdd() {}

  @Emit("click:delete") clickDelete() {}

  @Emit("click:excel") clickExcel() {}

  @Emit("click:inquiry") clickInquiry() {}

  @Emit("click:reload") clickReload() {}

  @Emit("click:save") clickSave() {}
}
</script>
<style lang="scss" scoped>
#buttonSet {
  right: 8px;
  top: 8px;
}
</style>
