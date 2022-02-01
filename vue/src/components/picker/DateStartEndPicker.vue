<template>
  <div>
    <v-row no-gutters v-if="fullWidth">
      <v-col cols="6">
        <date-picker
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
          :max="maxDate"
          start-type
          full-width
        />
      </v-col>
      <v-col cols="6">
        <date-picker
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
          :min="minDate"
          end-type
          full-width
        />
      </v-col>
    </v-row>
    <v-row no-gutters v-else>
      <v-col cols="12" style="display: inline-flex">
        <date-picker
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
          :max="maxDate"
          start-type
        />
        <date-picker
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
          :min="minDate"
          end-type
        />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import DatePicker from "@/components/picker/DatePicker.vue";
import dayjs from "dayjs";
import { DateTime } from "@/definitions/types";
import { computed, defineComponent, PropType, ref } from "@vue/composition-api";

export default defineComponent({
  components: { DatePicker },
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
    fullWidth: { type: Boolean },
    hideHint: { type: Boolean },
  },
  setup(props, { emit }) {
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
      DATEPICKER_FORMAT: computed((): string => "YYYY-MM-DD"),
      defaultLabelForStart: computed(
        (): string => props.startLabel || "시작 날짜",
      ),
      defaultLabelForEnd: computed((): string => props.endLabel || "종료 날짜"),

      minDate: computed((): string | undefined => {
        if (!computes.syncedStart.value) {
          return undefined;
        }
        return dayjs(computes.syncedStart.value).format(
          computes.DATEPICKER_FORMAT.value,
        );
      }),

      maxDate: computed((): string | undefined => {
        if (!computes.syncedEnd.value) {
          return undefined;
        }
        return dayjs(computes.syncedEnd.value).format(
          computes.DATEPICKER_FORMAT.value,
        );
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
    const refStart = ref<null | InstanceType<typeof DatePicker>>(null);
    const refEnd = ref<null | InstanceType<typeof DatePicker>>(null);
    return { ...computes, ...methods, refStart, refEnd };
  },
});
</script>
