<template>
  <div>
    <ValidationObserver ref="observer">
      <v-dialog
        ref="refDialog"
        v-model="dialog"
        :return-value.sync="timeValue"
        :width="470"
        @keydown.esc="dialog = false"
        @keydown.enter="$refs.refDialog.save(timeValue)"
      >
        <template v-slot:activator="{ on }">
          <ValidationProvider
            :name="label"
            :rules="required ? 'required' : ''"
            v-slot="{ errors }"
          >
            <v-text-field
              v-model="value"
              :label="label"
              :messages="message"
              prepend-inner-icon="mdi-calendar-clock"
              @click:prepend-inner="dialog = true"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="hideDetails"
              :clearable="clearable"
              :error-messages="errors"
              :append-outer-icon="startType ? 'mdi-tilde' : undefined"
              :class="endType ? 'ml-3' : undefined"
              :style="style"
              v-on="on"
            />
          </ValidationProvider>
        </template>
        <v-date-picker
          v-model="value"
          :locale="envs.LOCALE"
          landscape
          reactive
          scrollable
          header-color="primary"
          :max="maxDate"
          :min="minDate"
        >
        </v-date-picker>
        <v-time-picker
          v-model="timeValue"
          format="24hr"
          landscape
          scrollable
          :use-seconds="useSeconds"
          header-color="primary"
          :max="maxTime"
          :min="minTime"
        >
          <v-btn text color="primary" @click="setNow"> 지금</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn text color="primary" @click="dialog = false"> 취소</v-btn>
          <v-btn text color="primary" @click="$refs.refDialog.save(timeValue)">
            확인
          </v-btn>
        </v-time-picker>
      </v-dialog>
    </ValidationObserver>
  </div>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue, Watch } from 'vue-property-decorator';
import envs from '@/constants/envs';
import dayjs from 'dayjs';

@Component({ name: 'DatetimePicker' })
export default class extends Vue {
  @Model('input', { required: true }) readonly date!:
    | Date
    | string
    | number
    | null;

  @Prop({ type: String, default: '날짜선택' }) readonly label!: string | null;
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
  @Prop() readonly max!: string[];
  @Prop() readonly min!: string[];

  readonly envs: typeof envs = envs;
  value: string | null = null;
  dialog: boolean = false;
  timeValue: string | null = null;

  get format() {
    return this.useSeconds
      ? envs.DATETIME_FORMAT_STRING
      : envs.DATETIME_MINUTE_FORMAT_STRING;
  }

  get formatTime() {
    return this.useSeconds
      ? envs.TIME_FORMAT_STRING
      : envs.TIME_MINUTE_FORMAT_STRING;
  }

  get maxDate() {
    return this.max?.length > 0 ? this.max[0] : undefined;
  }

  get maxTime() {
    return this.max?.length > 1 ? this.max[1] : undefined;
  }

  get minDate() {
    return this.min?.length > 0 ? this.min[0] : undefined;
  }

  get minTime() {
    return this.min?.length > 1 ? this.min[1] : undefined;
  }

  get style() {
    if (this.fullWidth) {
      return undefined;
    }
    let defaultWidth = 10.5;
    this.clearable && (defaultWidth += 1.2);
    this.startType && (defaultWidth += 2.2);
    return `max-width: ${defaultWidth}rem;`;
  }

  @Watch('date', { immediate: true })
  watchDate(
    val: Date | string | number | null,
    oldVal: Date | string | number | null,
  ) {
    if (!val || val === oldVal || isNaN(dayjs(val).toDate().getTime())) {
      return;
    }
    this.value = dayjs(val).format(this.format);
    this.timeValue = this.value.split(' ')[1];
  }

  @Watch('timeValue')
  watchTimeValue(val: string) {
    if (this.value) {
      this.value = `${this.value.split(' ')[0]} ${val}`;
    }
  }

  @Watch('value', { immediate: true })
  watchValue(val: string, oldVal: string) {
    if (
      val !== oldVal &&
      dayjs(val).toDate().getTime() !== dayjs(oldVal).toDate().getTime()
    ) {
      this.$emit('input', dayjs(val).toDate());
    }
  }

  setNow() {
    this.value = dayjs().format(this.format);
  }

  async validate() {
    return await (this.$refs.observer as any).validate();
  }
}
</script>
