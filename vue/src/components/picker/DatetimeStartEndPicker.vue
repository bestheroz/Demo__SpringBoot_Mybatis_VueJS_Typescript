<template>
  <div>
    <v-row no-gutters v-if="fullWidth">
      <v-col cols="6">
        <datetime-picker
          ref="refStart"
          v-model="syncedStart"
          :label="defaultLabelForStart"
          :message="startMessage"
          :required="required"
          :disabled="disabled || startDisabled"
          :dense="dense"
          :hide-details="hideDetails"
          :clearable="clearable"
          :hide-hint="hideHint"
          :use-seconds="useSeconds"
          :max="maxDate"
          full-width
          start-type
        />
      </v-col>
      <v-col cols="6">
        <datetime-picker
          ref="refEnd"
          v-model="syncedEnd"
          :label="defaultLabelForEnd"
          :message="endMessage"
          :required="required"
          :disabled="disabled || endDisabled"
          :dense="dense"
          :hide-details="hideDetails"
          :clearable="clearable"
          :hide-hint="hideHint"
          :use-seconds="useSeconds"
          :min="minDate"
          full-width
          end-type
        />
      </v-col>
    </v-row>
    <v-row no-gutters v-else>
      <v-col cols="12" style="display: inline-flex">
        <datetime-picker
          ref="refStart"
          v-model="syncedStart"
          :label="defaultLabelForStart"
          :message="startMessage"
          :required="required"
          :disabled="disabled || startDisabled"
          :dense="dense"
          :hide-details="hideDetails"
          :clearable="clearable"
          :hide-hint="hideHint"
          :use-seconds="useSeconds"
          :max="maxDate"
          start-type
        />
        <datetime-picker
          ref="refEnd"
          v-model="syncedEnd"
          :label="defaultLabelForEnd"
          :message="endMessage"
          :required="required"
          :disabled="disabled || endDisabled"
          :dense="dense"
          :hide-details="hideDetails"
          :clearable="clearable"
          :hide-hint="hideHint"
          :use-seconds="useSeconds"
          :min="minDate"
          end-type
        />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import DatetimePicker from "@/components/picker/DatetimePicker.vue";
import dayjs from "dayjs";
import { DateTime } from "@/definitions/types";
import {
  computed,
  defineComponent,
  PropType,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";

export default defineComponent({
  components: { DatetimePicker },
  props: {
    start: {
      type: [String, Number, Date, Object] as PropType<DateTime>,
      default: undefined,
    },
    end: {
      type: [String, Number, Date, Object] as PropType<DateTime>,
      default: undefined,
    },
    startLabel: { type: String, default: undefined },
    endLabel: { type: String, default: undefined },
    startMessage: { type: String, default: undefined },
    endMessage: { type: String, default: undefined },
    required: { type: Boolean },
    disabled: { type: Boolean },
    startDisabled: { type: Boolean },
    endDisabled: { type: Boolean },
    dense: { type: Boolean },
    hideDetails: { type: Boolean },
    clearable: { type: Boolean },
    useSeconds: { type: Boolean },
    fullWidth: { type: Boolean },
    hideHint: { type: Boolean },
  },
  setup(props, { emit }) {
    const state = reactive({});
    const computes = {
      syncedStart: computed({
        get(): DateTime {
          return props.start as DateTime;
        },
        set(value: DateTime) {
          emit("update:start", value);
        },
      }),
      syncedEnd: computed({
        get(): DateTime {
          return props.end as DateTime;
        },
        set(value: DateTime) {
          emit("update:end", value);
        },
      }),
      DATE_FORMAT: computed((): string => "YYYY-MM-DD"),
      MINUTE_TIME_PICKER_FORMAT: computed((): string => "HH:mm"),
      TIMEPICKER_FORMAT: computed((): string => "HH:mm:ss"),

      defaultLabelForStart: computed(
        (): string => props.startLabel || "시작 시각",
      ),

      defaultLabelForEnd: computed((): string => props.endLabel || "종료 시각"),

      minDate: computed((): string[] | undefined => {
        if (!computes.syncedStart.value) {
          return undefined;
        }
        return [
          dayjs(computes.syncedStart.value).format(computes.DATE_FORMAT.value),
          props.useSeconds
            ? dayjs(computes.syncedStart.value).format(
                computes.TIMEPICKER_FORMAT.value,
              )
            : dayjs(computes.syncedStart.value).format(
                computes.MINUTE_TIME_PICKER_FORMAT.value,
              ),
        ];
      }),

      maxDate: computed((): string[] | undefined => {
        if (!computes.syncedEnd.value) {
          return undefined;
        }
        return [
          dayjs(computes.syncedEnd.value).format(computes.DATE_FORMAT.value),
          props.useSeconds
            ? dayjs(computes.syncedEnd.value).format(
                computes.TIMEPICKER_FORMAT.value,
              )
            : dayjs(computes.syncedEnd.value).format(
                computes.MINUTE_TIME_PICKER_FORMAT.value,
              ),
        ];
      }),
    };
    const methods = {
      validate: async (): Promise<boolean> => {
        return (
          !!(await refStart.value?.validate()) &&
          !!(await refEnd.value?.validate())
        );
      },
    };
    const refStart = ref<null | InstanceType<typeof DatetimePicker>>(null);
    const refEnd = ref<null | InstanceType<typeof DatetimePicker>>(null);
    return { ...toRefs(state), ...computes, ...methods, refStart, refEnd };
  },
});
</script>
