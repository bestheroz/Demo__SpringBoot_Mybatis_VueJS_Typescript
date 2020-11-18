<template>
  <div>
    <ValidationObserver ref="observer">
      <v-dialog
        ref="refDialog"
        v-model="dialog"
        :return-value.sync="value"
        :width="470"
        @keydown.esc="dialog = false"
        @keydown.enter="$refs.refDialog.save(value)"
      >
        <template #activator="{ on }">
          <ValidationProvider
            :name="label"
            :rules="required ? 'required' : ''"
            v-slot="{ errors }"
          >
            <v-text-field
              v-model="value"
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
          :max="max"
          :min="min"
        >
          <v-btn text color="primary" @click="setToday"> 오늘</v-btn>
          <div class="flex-grow-1"></div>
          <v-btn text color="primary" @click="dialog = false"> 취소</v-btn>
          <v-btn text color="primary" @click="$refs.refDialog.save(value)">
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
  @Model("input", { required: true }) readonly date!:
    | Date
    | string
    | number
    | null;

  @Prop({ type: String, default: "날짜선택" }) readonly label!: string | null;
  @Prop({ type: String }) readonly message!: string | null;
  @Prop({ type: Boolean, default: false }) readonly required!: boolean;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly dense!: boolean;
  @Prop({ type: Boolean, default: false }) readonly hideDetails!: boolean;
  @Prop({ type: Boolean, default: false }) readonly clearable!: boolean;
  @Prop({ type: Boolean, default: false }) readonly endOfDay!: boolean;
  @Prop({ type: Boolean, default: false }) readonly startType!: boolean;
  @Prop({ type: Boolean, default: false }) readonly endType!: boolean;
  @Prop({ type: Boolean, default: false }) readonly fullWidth!: boolean;
  @Prop({ type: String }) readonly max!: string;
  @Prop({ type: String }) readonly min!: string;

  readonly envs: typeof envs = envs;
  value: string | null = null;
  dialog = false;

  get format(): string {
    return envs.DATE_FORMAT_STRING;
  }

  get style(): string | undefined {
    if (this.fullWidth) {
      return undefined;
    }
    let defaultWidth = 7.7;
    this.clearable && (defaultWidth += 1);
    this.startType && (defaultWidth += 2);
    return `max-width: ${defaultWidth}rem;`;
  }

  @Watch("date", { immediate: true })
  watchDate(
    val: Date | string | number | null,
    oldVal: Date | string | number | null,
  ): void {
    if (!val || val === oldVal || isNaN(dayjs(val).toDate().getTime())) {
      return;
    }
    this.value = this.endOfDay
      ? dayjs(val).endOf("day").format(this.format)
      : dayjs(val).startOf("day").format(this.format);
  }

  @Watch("value", { immediate: true })
  watchValue(val: string, oldVal: string): void {
    if (
      val !== oldVal &&
      dayjs(val).toDate().getTime() !== dayjs(oldVal).toDate().getTime()
    ) {
      this.$emit(
        "input",
        this.endOfDay
          ? dayjs(val).endOf("day").toDate()
          : dayjs(val).startOf("day").toDate(),
      );
    }
  }

  setToday(): void {
    this.value = this.endOfDay
      ? dayjs().endOf("day").format(this.format)
      : dayjs().startOf("day").format(this.format);
  }

  async validate(): Promise<boolean> {
    return await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
  }
}
</script>
