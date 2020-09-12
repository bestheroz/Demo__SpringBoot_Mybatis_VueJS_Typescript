<template>
  <div>
    <ValidationObserver ref="observer">
      <v-row no-gutters>
        <v-col cols="6">
          <date-picker
            ref="refStart"
            v-model="syncedStart"
            :label="startLabel"
            :message="startMessage"
            :required="required"
            :disabled="disabled || startDisabled"
            :dense="dense"
            :hide-details="hideDetails"
            :clearable="clearable"
            :max="maxDate"
            start-type
          />
        </v-col>
        <v-col cols="6">
          <date-picker
            ref="refEnd"
            v-model="syncedEnd"
            :label="endLabel"
            :message="endMessage"
            :required="required"
            :disabled="disabled || endDisabled"
            :dense="dense"
            :hide-details="hideDetails"
            :clearable="clearable"
            :min="minDate"
            end-type
            end-of-day
          />
        </v-col>
      </v-row>
    </ValidationObserver>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Vue } from 'vue-property-decorator';
import envs from '@/constants/envs';
import dayjs from 'dayjs';
import DatePicker from '@/components/picker/DatePicker.vue';

@Component({
  name: 'DateStartEndPicker',
  components: { DatePicker },
})
export default class extends Vue {
  @PropSync('start', { required: true }) syncedStart!:
    | Date
    | string
    | number
    | null;

  @PropSync('end', { required: true }) readonly syncedEnd!:
    | Date
    | string
    | number
    | null;

  @Prop({ type: String, default: '시작날짜' }) readonly startLabel!:
    | string
    | null;

  @Prop({ type: String, default: '종료날짜' }) readonly endLabel!:
    | string
    | null;

  @Prop({ type: String }) readonly startMessage!: string | null;
  @Prop({ type: String }) readonly endMessage!: string | null;
  @Prop({ type: Boolean, default: false }) readonly required!: boolean;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly startDisabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly endDisabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly dense!: boolean;
  @Prop({ type: Boolean, default: false }) readonly hideDetails!: boolean;
  @Prop({ type: Boolean, default: false }) readonly clearable!: boolean;

  readonly envs: typeof envs = envs;
  startDialog: boolean = false;
  endDialog: boolean = false;

  get minDate() {
    if (!this.syncedStart) {
      return undefined;
    }
    return dayjs(this.syncedStart).format(envs.DATE_FORMAT_STRING);
  }

  get maxDate() {
    if (!this.syncedEnd) {
      return undefined;
    }
    return dayjs(this.syncedEnd).format(envs.DATE_FORMAT_STRING);
  }

  async validate() {
    return (
      (await (this.$refs.refStart as any).validate()) &&
      (await (this.$refs.refEnd as any).validate())
    );
  }
}
</script>
