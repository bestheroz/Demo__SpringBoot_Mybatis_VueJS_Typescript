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
        :disabled="!$store.getters.writeAuthority"
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
              :class="classSet"
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
import envs from "@/constants/envs";
import dayjs from "dayjs";
import { ValidationObserver } from "vee-validate";
import { DateTime } from "@/definitions/types";
import {
  computed,
  defineComponent,
  PropType,
  reactive,
  ref,
  toRefs,
  watch,
} from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  props: {
    value: {
      type: [String, Number, Date, Object] as PropType<DateTime>,
      default: undefined,
    },
    label: { type: String, default: undefined },
    message: { type: String, default: undefined },
    required: { type: Boolean },
    disabled: { type: Boolean },
    dense: { type: Boolean },
    hideDetails: { type: Boolean },
    clearable: { type: Boolean },
    useSeconds: { type: Boolean },
    startType: { type: Boolean },
    endType: { type: Boolean },
    fullWidth: { type: Boolean },
    hideHint: { type: Boolean },
    max: { type: Array as PropType<string[]>, default: undefined },
    min: { type: Array as PropType<string[]>, default: undefined },
  },
  setup(props, { emit }) {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    const vModel = setupVModel<DateTime>(props, emit);
    const state = reactive({
      textFieldString: null as string | null,
      datePickerString: null as string | null,
      timePickerString: null as string | null,
      dialog: false,
      valid: false,
    });
    const computes = {
      envs: computed((): typeof envs => envs),
      DATEPICKER_FORMAT: computed((): string => "YYYY-MM-DD"),
      DATETIMEPICKER_MINUTE_FORMAT: computed((): string => "YYYY-MM-DD HH:mm"),
      DATETIMEPICKER_FORMAT: computed((): string => "YYYY-MM-DD HH:mm:ss"),
      TIMEPICKER_MINUTE_FORMAT: computed((): string => "HH:mm"),
      TIMEPICKER_FORMAT: computed((): string => "HH:mm:ss"),
      defaultLabel: computed((): string => props.label || "날짜 선택"),

      maxDate: computed((): string | undefined =>
        props.max?.length > 0 ? props.max[0] : undefined,
      ),

      maxTime: computed((): string | undefined =>
        props.max?.length > 1 ? props.max[1] : undefined,
      ),

      minDate: computed((): string | undefined =>
        props.min?.length > 0 ? props.min[0] : undefined,
      ),

      minTime: computed((): string | undefined =>
        props.min?.length > 1 ? props.min[1] : undefined,
      ),

      style: computed((): string | undefined => {
        if (props.fullWidth) {
          return undefined;
        }
        let defaultWidth = 9.5;
        defaultWidth +=
          ((props.useSeconds
            ? envs.DATETIME_FORMAT_STRING.length
            : envs.DATETIME_MINUTE_FORMAT_STRING.length) -
            12) *
          0.4;
        props.clearable && (defaultWidth += 1);
        props.startType && (defaultWidth += 2);
        return `max-width: ${defaultWidth}rem;`;
      }),

      classSet: computed((): string | undefined => {
        let result = "";
        if (props.endType) {
          result += " ml-3";
        }
        if (props.required) {
          result += " required";
        }
        return result;
      }),

      hint: computed((): string | undefined =>
        vModel.vModel.value
          ? dayjs(vModel.vModel.value).toISOString()
          : undefined,
      ),

      disableToday: computed((): boolean => {
        if (!props.min && !props.max) {
          return false;
        }
        if (props.useSeconds) {
          return props.endType
            ? dayjs().isBefore(
                dayjs(
                  props.min.join(" "),
                  computes.DATETIMEPICKER_FORMAT.value,
                ),
              )
            : dayjs(
                props.max.join(" "),
                computes.DATETIMEPICKER_FORMAT.value,
              ).isBefore(dayjs());
        } else {
          return props.endType
            ? dayjs().isBefore(
                dayjs(
                  props.min.join(" "),
                  computes.DATETIMEPICKER_MINUTE_FORMAT.value,
                ),
              )
            : dayjs().isAfter(
                dayjs(
                  props.max.join(" "),
                  computes.DATETIMEPICKER_MINUTE_FORMAT.value,
                ),
              );
        }
      }),
    };
    const methods = {
      watchDatePickerString: (): void => {
        if (
          !state.datePickerString ||
          !state.timePickerString ||
          !dayjs(
            `${state.datePickerString} ${state.timePickerString}`,
            props.useSeconds
              ? computes.DATETIMEPICKER_FORMAT.value
              : computes.DATETIMEPICKER_MINUTE_FORMAT.value,
          ).isValid()
        ) {
          state.textFieldString = null;
          return;
        }
        const _dayjs = dayjs(
          `${state.datePickerString} ${state.timePickerString}`,
          props.useSeconds
            ? computes.DATETIMEPICKER_FORMAT.value
            : computes.DATETIMEPICKER_MINUTE_FORMAT.value,
        );
        if (_dayjs) {
          state.textFieldString = props.useSeconds
            ? dayjs(
                `${state.datePickerString} ${state.timePickerString}`,
                envs.DATETIME_FORMAT_STRING,
              ).format(computes.DATETIMEPICKER_FORMAT.value)
            : dayjs(
                `${state.datePickerString} ${state.timePickerString}`,
                envs.DATETIME_MINUTE_FORMAT_STRING,
              ).format(computes.DATETIMEPICKER_MINUTE_FORMAT.value);
        } else {
          state.textFieldString = null;
        }
      },
      setNow: (): void => {
        state.datePickerString = dayjs().format(
          computes.DATEPICKER_FORMAT.value,
        );
        state.timePickerString = props.useSeconds
          ? dayjs().format(computes.TIMEPICKER_FORMAT.value)
          : dayjs().format(computes.TIMEPICKER_MINUTE_FORMAT.value);
      },

      onClear: (): void => {
        state.datePickerString = null;
        state.timePickerString = null;
      },

      validate: async (): Promise<boolean> => {
        return !!(await observer.value?.validate());
      },
    };
    watch(
      () => vModel.vModel.value,
      (val: DateTime, oldVal: DateTime) => {
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
        state.datePickerString = dayjs(val).format(
          computes.DATEPICKER_FORMAT.value,
        );
        state.timePickerString = props.useSeconds
          ? dayjs(val).format(computes.TIMEPICKER_FORMAT.value)
          : dayjs(val).format(computes.TIMEPICKER_MINUTE_FORMAT.value);
      },
      { immediate: true },
    );
    watch(
      () => state.datePickerString,
      () => {
        methods.watchDatePickerString();
      },
    );
    watch(
      () => state.timePickerString,
      () => {
        methods.watchDatePickerString();
      },
      { immediate: true },
    );
    watch(
      () => state.textFieldString,
      (val: string | null) => {
        if (dayjs(val, envs.DATETIME_MINUTE_FORMAT_STRING).isValid()) {
          emit(
            "input",
            props.endType
              ? props.useSeconds
                ? dayjs(val, envs.DATETIME_FORMAT_STRING)
                    ?.endOf("second")
                    ?.toISOString()
                : dayjs(val, envs.DATETIME_MINUTE_FORMAT_STRING)
                    ?.endOf("minute")
                    ?.toISOString()
              : props.useSeconds
              ? dayjs(val, envs.DATETIME_FORMAT_STRING)
                  ?.startOf("second")
                  ?.toISOString()
              : dayjs(val, envs.DATETIME_MINUTE_FORMAT_STRING)
                  ?.startOf("minute")
                  ?.toISOString(),
          );
        } else {
          emit("input", null);
        }
      },
      { immediate: true },
    );
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...toRefs(state), ...computes, ...methods, observer };
  },
});
</script>
