<template>
  <div>
    <ValidationObserver ref="observer">
      <v-dialog
        ref="refDialog"
        v-model="dialog"
        :return-value.sync="pickerString"
        :width="470"
        @keydown.esc="dialog = false"
        @keydown.enter="$refs.refDialog.save(pickerString)"
      >
        <template #activator="{ on }">
          <ValidationProvider
            :name="label"
            :rules="required ? 'required' : ''"
            v-slot="{ errors }"
          >
            <v-text-field
              :value="textFieldString"
              :label="defaultLabel"
              :hint="hideHint ? undefined : hint"
              persistent-hint
              :messages="message"
              prepend-inner-icon="mdi-calendar-cursor"
              @click:prepend-inner="dialog = true"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="hideDetails"
              :clearable="clearable"
              @click:clear="pickerString = null"
              :error-messages="errors"
              :append-outer-icon="startType ? 'mdi-tilde' : undefined"
              :class="endType ? 'ml-3' : undefined"
              :style="style"
              v-on="on"
            />
          </ValidationProvider>
        </template>
        <v-date-picker
          v-model="pickerString"
          :locale="envs.LOCALE"
          landscape
          reactive
          scrollable
          :max="max"
          :min="min"
        >
          <v-btn outlined @click="setToday" :disabled="disableToday">
            오늘
          </v-btn>
          <div class="flex-grow-1"></div>
          <v-btn outlined @click="dialog = false"> 취소 </v-btn>
          <v-btn outlined @click="$refs.refDialog.save(pickerString)">
            확인
          </v-btn>
        </v-date-picker>
      </v-dialog>
    </ValidationObserver>
  </div>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue, Watch } from "vue-property-decorator";
import envs from "@/constants/envs";
import dayjs from "dayjs";
import { ValidationObserver } from "vee-validate";

@Component({ name: "DatePicker" })
export default class extends Vue {
  @Model("input", { required: true }) readonly outputDate!:
    | Date
    | string
    | number
    | null;

  @Prop({ type: String }) readonly label!: string | null;
  @Prop({ type: String }) readonly message!: string | null;
  @Prop({ type: Boolean, default: false }) readonly required!: boolean;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly dense!: boolean;
  @Prop({ type: Boolean, default: false }) readonly hideDetails!: boolean;
  @Prop({ type: Boolean, default: false }) readonly clearable!: boolean;
  @Prop({ type: Boolean, default: false }) readonly startType!: boolean;
  @Prop({ type: Boolean, default: false }) readonly endType!: boolean;
  @Prop({ type: Boolean, default: false }) readonly fullWidth!: boolean;
  @Prop({ type: Boolean, default: false }) readonly hideHint!: boolean;
  @Prop({ type: String }) readonly max!: string;
  @Prop({ type: String }) readonly min!: string;

  readonly envs: typeof envs = envs;
  readonly DATEPICKER_FORMAT = "YYYY-MM-DD";
  pickerString: string | null = null;
  dialog = false;
  valid = false;
  errors: string[] | null = null;

  get defaultLabel(): string {
    return this.label || "날짜 선택";
  }

  get style(): string | undefined {
    if (this.fullWidth) {
      return undefined;
    }
    let defaultWidth = 9.5;
    this.startType && (defaultWidth += 2);
    return `max-width: ${defaultWidth}rem;`;
  }

  get hint(): string | undefined {
    return this.outputDate && dayjs(this.outputDate).isValid()
      ? dayjs(this.outputDate).toISOString()
      : undefined;
  }

  get disableToday(): boolean {
    if (!this.min && !this.max) {
      return false;
    }
    return this.endType
      ? dayjs().isBefore(dayjs(this.min, this.DATEPICKER_FORMAT))
      : dayjs().isAfter(dayjs(this.max, this.DATEPICKER_FORMAT));
  }

  get textFieldString(): string {
    if (
      !this.pickerString ||
      !dayjs(this.pickerString, this.DATEPICKER_FORMAT).isValid()
    ) {
      return "";
    }
    return dayjs(this.pickerString, this.DATEPICKER_FORMAT).format(
      this.DATEPICKER_FORMAT,
    );
  }

  @Watch("outputDate", { immediate: true })
  watchDate(
    val: Date | string | number | null,
    oldVal: Date | string | number | null,
  ): void {
    if (
      !val ||
      !dayjs(val).isValid() ||
      val === oldVal ||
      (oldVal &&
        dayjs(oldVal).isValid() &&
        dayjs(val).diff(dayjs(oldVal)) === 0)
    ) {
      return;
    }
    this.pickerString = dayjs(val).format(this.DATEPICKER_FORMAT);
  }

  @Watch("textFieldString", { immediate: true })
  watchText(val: string): void {
    if (dayjs(val, envs.DATE_FORMAT_STRING).isValid()) {
      this.$emit(
        "input",
        this.endType
          ? dayjs(val, envs.DATE_FORMAT_STRING)?.endOf("day")?.toISOString()
          : dayjs(val, envs.DATE_FORMAT_STRING)?.startOf("day")?.toISOString(),
      );
    } else {
      this.$emit("input", null);
    }
  }

  setToday(): void {
    this.pickerString = dayjs().format(this.DATEPICKER_FORMAT);
  }

  async validate(): Promise<boolean> {
    return await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
  }
}
</script>
