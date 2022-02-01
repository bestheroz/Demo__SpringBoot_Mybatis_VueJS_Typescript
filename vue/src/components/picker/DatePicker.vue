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
              :class="classSet"
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
          <v-btn outlined @click="dialog = false"> 취소</v-btn>
          <v-btn outlined @click="$refs.refDialog.save(pickerString)">
            확인
          </v-btn>
        </v-date-picker>
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
    startType: { type: Boolean },
    endType: { type: Boolean },
    fullWidth: { type: Boolean },
    hideHint: { type: Boolean },
    max: { type: String, default: undefined },
    min: { type: String, default: undefined },
  },
  setup(props, { emit }) {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    const vModel = setupVModel<DateTime>(props, emit);
    const state = reactive({
      pickerString: null as string | null,
      dialog: false,
      valid: false,
    });
    const computes = {
      envs: computed((): typeof envs => envs),
      DATEPICKER_FORMAT: computed((): string => "YYYY-MM-DD"),
      defaultLabel: computed((): string => props.label || "날짜 선택"),

      style: computed((): string | undefined => {
        if (props.fullWidth) {
          return undefined;
        }
        let defaultWidth = 9.5;
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
        vModel.vModel.value && dayjs(vModel.vModel.value).isValid()
          ? dayjs(vModel.vModel.value).toISOString()
          : undefined,
      ),

      disableToday: computed((): boolean => {
        if (!props.min && !props.max) {
          return false;
        }
        return props.endType
          ? dayjs().isBefore(dayjs(props.min, computes.DATEPICKER_FORMAT.value))
          : dayjs().isAfter(dayjs(props.max, computes.DATEPICKER_FORMAT.value));
      }),

      textFieldString: computed((): string => {
        if (
          !state.pickerString ||
          !dayjs(state.pickerString, computes.DATEPICKER_FORMAT.value).isValid()
        ) {
          return "";
        }
        return dayjs(
          state.pickerString,
          computes.DATEPICKER_FORMAT.value,
        ).format(computes.DATEPICKER_FORMAT.value);
      }),
    };
    const methods = {
      setToday: (): void => {
        state.pickerString = dayjs().format(computes.DATEPICKER_FORMAT.value);
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
        state.pickerString = dayjs(val).format(
          computes.DATEPICKER_FORMAT.value,
        );
      },
      { immediate: true },
    );
    watch(
      () => computes.textFieldString.value,
      (val: string) => {
        if (dayjs(val, envs.DATE_FORMAT_STRING).isValid()) {
          emit(
            "input",
            props.endType
              ? dayjs(val, envs.DATE_FORMAT_STRING)?.endOf("day")?.toISOString()
              : dayjs(val, envs.DATE_FORMAT_STRING)
                  ?.startOf("day")
                  ?.toISOString(),
          );
        } else {
          emit("input", null);
        }
      },
      { immediate: true },
    );
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...vModel, ...toRefs(state), ...computes, ...methods, observer };
  },
});
</script>
