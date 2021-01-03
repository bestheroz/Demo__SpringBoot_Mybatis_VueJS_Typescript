<template>
  <div>
    <ValidationObserver ref="observer">
      <v-dialog
        ref="refDialog"
        v-model="dialog"
        :return-value.sync="textFieldString"
        :width="470"
        @keydown.esc="dialog = false"
        @keydown.enter="$refs.refDialog.save(textFieldString)"
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
              prepend-inner-icon="mdi-calendar-clock"
              @click:prepend-inner="dialog = true"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="hideDetails"
              :clearable="clearable"
              @click:clear="onClear"
              :error-messages="errors"
              :append-outer-icon="startType ? 'mdi-tilde' : undefined"
              :class="endType ? 'ml-3' : undefined"
              :style="style"
              v-on="on"
            />
          </ValidationProvider>
        </template>
        <v-date-picker
          v-model="datePickerString"
          :locale="envs.LOCALE"
          landscape
          reactive
          scrollable
          :max="maxDate"
          :min="minDate"
        >
        </v-date-picker>
        <v-time-picker
          v-model="timePickerString"
          format="24hr"
          landscape
          scrollable
          :use-seconds="useSeconds"
          :max="maxTime"
          :min="minTime"
        >
          <v-btn outlined @click="setNow" :disabled="disableToday">
            지금
          </v-btn>
          <div class="flex-grow-1"></div>
          <v-btn outlined @click="dialog = false"> 취소 </v-btn>
          <v-btn outlined @click="$refs.refDialog.save(textFieldString)">
            확인
          </v-btn>
        </v-time-picker>
      </v-dialog>
    </ValidationObserver>
  </div>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue, Watch } from "vue-property-decorator";
import envs from "@/constants/envs";
import dayjs from "dayjs";
import { ValidationObserver } from "vee-validate";

@Component({ name: "DatetimePicker" })
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
  @Prop({ type: Boolean, default: false }) readonly useSeconds!: boolean;
  @Prop({ type: Boolean, default: false }) readonly startType!: boolean;
  @Prop({ type: Boolean, default: false }) readonly endType!: boolean;
  @Prop({ type: Boolean, default: false }) readonly fullWidth!: boolean;
  @Prop({ type: Boolean, default: false }) readonly hideHint!: boolean;
  @Prop() readonly max!: string[];
  @Prop() readonly min!: string[];

  readonly envs: typeof envs = envs;
  readonly DATEPICKER_FORMAT = "YYYY-MM-DD";
  readonly DATETIMEPICKER_MINUTE_FORMAT = "YYYY-MM-DD HH:mm";
  readonly DATETIMEPICKER_FORMAT = "YYYY-MM-DD HH:mm:ss";
  readonly TIMEPICKER_MINUTE_FORMAT = "HH:mm";
  readonly TIMEPICKER_FORMAT = "HH:mm:ss";
  textFieldString: string | null = null;
  datePickerString: string | null = null;
  timePickerString: string | null = null;
  dialog = false;
  errors: string[] | null = null;
  valid = false;

  get defaultLabel(): string {
    return this.label || "날짜 선택";
  }

  get maxDate(): string | undefined {
    return this.max?.length > 0 ? this.max[0] : undefined;
  }

  get maxTime(): string | undefined {
    return this.max?.length > 1 ? this.max[1] : undefined;
  }

  get minDate(): string | undefined {
    return this.min?.length > 0 ? this.min[0] : undefined;
  }

  get minTime(): string | undefined {
    return this.min?.length > 1 ? this.min[1] : undefined;
  }

  get style(): string | undefined {
    if (this.fullWidth) {
      return undefined;
    }
    let defaultWidth = 9.5;
    defaultWidth +=
      ((this.useSeconds
        ? envs.DATETIME_FORMAT_STRING.length
        : envs.DATETIME_MINUTE_FORMAT_STRING.length) -
        12) *
      0.4;
    this.clearable && (defaultWidth += 1);
    this.startType && (defaultWidth += 2);
    return `max-width: ${defaultWidth}rem;`;
  }

  get hint(): string | undefined {
    return this.outputDate ? dayjs(this.outputDate).toISOString() : undefined;
  }

  get disableToday(): boolean {
    if (!this.min && !this.max) {
      return false;
    }
    if (this.useSeconds) {
      return this.endType
        ? dayjs().isBefore(
            dayjs(this.min.join(" "), this.DATETIMEPICKER_FORMAT),
          )
        : dayjs(this.max.join(" "), this.DATETIMEPICKER_FORMAT).isBefore(
            dayjs(),
          );
    } else {
      return this.endType
        ? dayjs().isBefore(
            dayjs(this.min.join(" "), this.DATETIMEPICKER_MINUTE_FORMAT),
          )
        : dayjs().isAfter(
            dayjs(this.max.join(" "), this.DATETIMEPICKER_MINUTE_FORMAT),
          );
    }
  }

  @Watch("outputDate", { immediate: true })
  watchValue(val: string, oldVal: string): void {
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
    this.datePickerString = dayjs(val).format(this.DATEPICKER_FORMAT);
    this.timePickerString = this.useSeconds
      ? dayjs(val).format(this.TIMEPICKER_FORMAT)
      : dayjs(val).format(this.TIMEPICKER_MINUTE_FORMAT);
  }

  @Watch("datePickerString")
  @Watch("timePickerString", { immediate: true })
  watchDatePickerString(): void {
    if (
      !this.datePickerString ||
      !this.timePickerString ||
      !dayjs(
        `${this.datePickerString} ${this.timePickerString}`,
        this.useSeconds
          ? this.DATETIMEPICKER_FORMAT
          : this.DATETIMEPICKER_MINUTE_FORMAT,
      ).isValid()
    ) {
      this.textFieldString = null;
      return;
    }
    const _dayjs = dayjs(
      `${this.datePickerString} ${this.timePickerString}`,
      this.useSeconds
        ? this.DATETIMEPICKER_FORMAT
        : this.DATETIMEPICKER_MINUTE_FORMAT,
    );
    if (_dayjs) {
      this.textFieldString = this.useSeconds
        ? dayjs(
            `${this.datePickerString} ${this.timePickerString}`,
            envs.DATETIME_FORMAT_STRING,
          ).format(this.DATETIMEPICKER_FORMAT)
        : dayjs(
            `${this.datePickerString} ${this.timePickerString}`,
            envs.DATETIME_MINUTE_FORMAT_STRING,
          ).format(this.DATETIMEPICKER_MINUTE_FORMAT);
    } else {
      this.textFieldString = null;
    }
  }
  @Watch("textFieldString", { immediate: true })
  watchTextFieldString(val: string): void {
    if (dayjs(val, envs.DATETIME_MINUTE_FORMAT_STRING).isValid()) {
      this.$emit(
        "input",
        this.endType
          ? this.useSeconds
            ? dayjs(val, envs.DATETIME_FORMAT_STRING)
                ?.endOf("second")
                ?.toISOString()
            : dayjs(val, envs.DATETIME_MINUTE_FORMAT_STRING)
                ?.endOf("minute")
                ?.toISOString()
          : this.useSeconds
          ? dayjs(val, envs.DATETIME_FORMAT_STRING)
              ?.startOf("second")
              ?.toISOString()
          : dayjs(val, envs.DATETIME_MINUTE_FORMAT_STRING)
              ?.startOf("minute")
              ?.toISOString(),
      );
    } else {
      this.$emit("input", null);
    }
  }

  setNow(): void {
    this.datePickerString = dayjs().format(this.DATEPICKER_FORMAT);
    this.timePickerString = this.useSeconds
      ? dayjs().format(this.TIMEPICKER_FORMAT)
      : dayjs().format(this.TIMEPICKER_MINUTE_FORMAT);
  }

  onClear(): void {
    this.datePickerString = null;
    this.timePickerString = null;
  }

  async validate(): Promise<boolean> {
    return await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
  }
}
</script>
