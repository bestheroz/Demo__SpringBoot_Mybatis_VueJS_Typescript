<template>
  <div>
    <ValidationObserver ref="observer">
      <v-dialog
        ref="refDialog"
        v-model="dialog"
        :return-value.sync="date"
        :width="470"
        @keydown.esc="dialog = false"
        @keydown.enter="$refs.refDialog.save(date)"
      >
        <template v-slot:activator="{ on }">
          <ValidationProvider
            :name="label"
            :rules="required ? 'required' : ''"
            v-slot="{ errors }"
          >
            <v-text-field
              v-model="date"
              :label="label"
              :messages="message"
              prepend-inner-icon="mdi-calendar-cursor"
              @click:prepend-inner="dialog = true"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="hideDetails"
              :clearable="clearable"
              :error-messages="errors"
              :append-outer-icon="startType ? 'mdi-tilde' : undefined"
              :class="endType ? 'ml-3' : undefined"
              v-on="on"
            />
          </ValidationProvider>
        </template>
        <v-date-picker
          v-model="date"
          :locale="envs.LOCALE"
          landscape
          reactive
          scrollable
          header-color="primary"
          :max="max"
          :min="min"
        >
          <v-btn text color="primary" @click="dialog = false">
            취소
          </v-btn>
          <div class="flex-grow-1"></div>
          <v-btn text color="primary" @click="setToday">
            오늘
          </v-btn>
          <v-btn text color="primary" @click="$refs.refDialog.save(date)">
            확인
          </v-btn>
        </v-date-picker>
      </v-dialog>
    </ValidationObserver>
  </div>
</template>

<script lang="ts">
import { Component, Model, Prop, Vue, Watch } from 'vue-property-decorator';
import envs from '@/constants/envs';
import dayjs from 'dayjs';

@Component({ name: 'DatePicker' })
export default class extends Vue {
  @Model('input', { required: true }) readonly value!:
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
  @Prop({ type: Boolean, default: false }) readonly endOfDay!: boolean;
  @Prop({ type: Boolean, default: false }) readonly startType!: boolean;
  @Prop({ type: Boolean, default: false }) readonly endType!: boolean;
  @Prop({ type: String }) readonly max: string;
  @Prop({ type: String }) readonly min: string;

  readonly envs: typeof envs = envs;
  date: string | null = null;
  dialog: boolean = false;

  get format() {
    return envs.DATE_FORMAT_STRING;
  }

  @Watch('value', { immediate: true })
  watchDate(
    val: Date | string | number | null,
    oldVal: Date | string | number | null,
  ) {
    if (!val || val === oldVal || isNaN(dayjs(val).toDate().getTime())) {
      return;
    }
    this.date = this.endOfDay
      ? dayjs(val).endOf('day').format(this.format)
      : dayjs(val).startOf('day').format(this.format);
  }

  @Watch('date', { immediate: true })
  watchValue(val: string, oldVal: string) {
    if (
      val !== oldVal &&
      dayjs(val).toDate().getTime() !== dayjs(oldVal).toDate().getTime()
    ) {
      this.$emit(
        'input',
        this.endOfDay
          ? dayjs(val).endOf('day').toDate()
          : dayjs(val).startOf('day').toDate(),
      );
    }
  }

  setToday() {
    this.date = this.endOfDay
      ? dayjs().endOf('day').format(this.format)
      : dayjs().startOf('day').format(this.format);
  }

  async validate() {
    return await (this.$refs.observer as any).validate();
  }
}
</script>
