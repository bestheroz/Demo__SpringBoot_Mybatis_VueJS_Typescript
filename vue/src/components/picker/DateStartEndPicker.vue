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
import { Component, Prop, PropSync, Ref, Vue } from "vue-property-decorator";
import DatePicker from "@/components/picker/DatePicker.vue";
import dayjs from "dayjs";

@Component({
  name: "DateStartEndPicker",
  components: { DatePicker },
})
export default class extends Vue {
  @PropSync("start", { required: true }) syncedStart!:
    | Date
    | string
    | number
    | null;

  @PropSync("end", { required: true }) readonly syncedEnd!:
    | Date
    | string
    | number
    | null;

  @Prop({ type: String }) readonly startLabel!: string | null;

  @Prop({ type: String }) readonly endLabel!: string | null;

  @Prop({ type: String }) readonly startMessage!: string | null;
  @Prop({ type: String }) readonly endMessage!: string | null;
  @Prop({ type: Boolean, default: false }) readonly required!: boolean;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly startDisabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly endDisabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly dense!: boolean;
  @Prop({ type: Boolean, default: false }) readonly hideDetails!: boolean;
  @Prop({ type: Boolean, default: false }) readonly clearable!: boolean;
  @Prop({ type: Boolean, default: false }) readonly fullWidth!: boolean;
  @Prop({ type: Boolean, default: false }) readonly hideHint!: boolean;

  @Ref("refStart") readonly refStart!: DatePicker;
  @Ref("refEnd") readonly refEnd!: DatePicker;

  readonly DATEPICKER_FORMAT = "YYYY-MM-DD";

  get defaultLabelForStart(): string {
    return this.startLabel || "시작 날짜";
  }
  get defaultLabelForEnd(): string {
    return this.endLabel || "종료 날짜";
  }

  get minDate(): string | undefined {
    if (!this.syncedStart) {
      return undefined;
    }
    return dayjs(this.syncedStart).format(this.DATEPICKER_FORMAT);
  }

  get maxDate(): string | undefined {
    if (!this.syncedEnd) {
      return undefined;
    }
    return dayjs(this.syncedEnd).format(this.DATEPICKER_FORMAT);
  }

  async validate(): Promise<boolean> {
    return (await this.refStart.validate()) && (await this.refEnd.validate());
  }
}
</script>
