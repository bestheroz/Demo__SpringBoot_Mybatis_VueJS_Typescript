<template>
  <v-row>
    <v-col>
      <v-dialog
        :disabled="disabled"
        :return-value.sync="localDay"
        persistent
        ref="dayDialog"
        v-model="localDayDialog"
        width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            :hint="dayHint"
            :label="localDayLabel"
            :persistent-hint="dayHint !== undefined"
            prepend-icon="event"
            readonly
            v-model="localDay"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker :locale="APP_LANGUAGE" scrollable v-model="localDay">
          <div class="flex-grow-1"></div>
          <v-btn
            @click="
              () => {
                localDay = $moment.format('YYYY-MM-DD');
                $refs.dayDialog.save(localDay);
                update();
              }
            "
            color="primary"
            text
            >{{ $t('today') }}
          </v-btn>
          <v-btn @click="localDayDialog = false" color="primary" text
            >{{ $t('cancel') }}
          </v-btn>
          <v-btn
            @click="
              () => {
                $refs.dayDialog.save(localDay);
                update();
              }
            "
            color="primary"
            text
            >{{ $t('ok') }}
          </v-btn>
        </v-date-picker>
      </v-dialog>
    </v-col>
    <v-col>
      <v-dialog
        :disabled="disabled"
        :return-value.sync="localTime"
        persistent
        ref="timeDialog"
        v-model="localTimeDialog"
        width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            :hint="timeHint"
            :label="localTimeLabel"
            :persistent-hint="timeHint !== undefined"
            prepend-icon="access_time"
            readonly
            v-model="localTime"
            v-on="on"
          ></v-text-field>
        </template>
        <v-time-picker
          format="24hr"
          full-width
          v-if="localTimeDialog"
          v-model="localTime"
        >
          <div class="flex-grow-1"></div>
          <v-btn
            @click="
              () => {
                localTime = $moment.format('HH:mm');
                $refs.timeDialog.save(localTime);
                update();
              }
            "
            color="primary"
            text
            >{{ $t('now') }}
          </v-btn>
          <v-btn @click="localTimeDialog = false" color="primary" text
            >{{ $t('cancel') }}
          </v-btn>
          <v-btn
            @click="
              () => {
                $refs.timeDialog.save(localTime);
                update();
              }
            "
            color="primary"
            text
            >{{ $t('ok') }}
          </v-btn>
        </v-time-picker>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import {
  Component,
  Emit,
  Prop,
  PropSync,
  Vue,
  Watch,
} from 'vue-property-decorator';

@Component
export default class DatetimePicker extends Vue {
  @PropSync('date', { type: [String, Number, Date] })
  readonly syncedDate!: string | number | Date;
  @Prop({ type: String, default: undefined })
  readonly dayLabel!: string;
  @Prop({ type: String, default: undefined })
  readonly dayHint!: string;
  @Prop({ type: String, default: undefined })
  readonly timeLabel!: string;
  @Prop({ type: String, default: undefined })
  readonly timeHint!: string;
  @Prop({ type: Boolean, default: false })
  readonly disabled!: boolean;

  readonly $moment: any;
  readonly APP_LANGUAGE: string | undefined = process.env.VUE_APP_LANGUAGE;
  localDay: string = '';
  localDayDialog: boolean = false;
  localTime: string = '';
  localTimeDialog: boolean = false;

  @Watch('syncedDate', { immediate: true })
  watchSyncedDateHandler(value: string | number | Date): void {
    this.localDay = this.$moment(value).format(`YYYY-MM-DD`);
    this.localTime = this.$moment(value).format(`HH:mm`);
  }

  @Emit('update:date')
  update(): Date {
    return this.$moment(
      `${this.localDay}T${this.localTime}:00${this.$store.state.timezone}`,
    ).toDate();
  }

  get localDayLabel(): string {
    return this.dayLabel || this.$t('dayPicker').toString();
  }

  get localTimeLabel(): string {
    return this.timeLabel || this.$t('timePicker').toString();
  }
}
</script>

<style scoped></style>
