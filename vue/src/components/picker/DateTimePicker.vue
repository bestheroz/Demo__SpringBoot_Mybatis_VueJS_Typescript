<template>
  <v-row>
    <v-col>
      <v-dialog
        ref="dayDialog"
        v-model="localDayDialog"
        :return-value.sync="localDay"
        persistent
        width="290px"
        :disabled="disabled"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            v-model="localDay"
            :label="localDayLabel"
            :hint="dayHint"
            :persistent-hint="dayHint !== undefined"
            prepend-icon="event"
            readonly
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker v-model="localDay" :locale="APP_LANGUAGE" scrollable>
          <div class="flex-grow-1"></div>
          <v-btn
            text
            color="primary"
            @click="
              () => {
                localDay = formatNowTZ('YYYY-MM-DD');
                $refs.dayDialog.save(localDay);
                updateDt();
              }
            "
            >{{ $t('today') }}
          </v-btn>
          <v-btn text color="primary" @click="localDayDialog = false"
            >{{ $t('cancel') }}
          </v-btn>
          <v-btn
            text
            color="primary"
            @click="
              () => {
                $refs.dayDialog.save(localDay);
                updateDt();
              }
            "
            >{{ $t('ok') }}
          </v-btn>
        </v-date-picker>
      </v-dialog>
    </v-col>
    <v-col>
      <v-dialog
        ref="timeDialog"
        v-model="localTimeDialog"
        :return-value.sync="localTime"
        persistent
        width="290px"
        :disabled="disabled"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            v-model="localTime"
            :label="localTimeLabel"
            :hint="timeHint"
            :persistent-hint="timeHint !== undefined"
            prepend-icon="access_time"
            readonly
            v-on="on"
          ></v-text-field>
        </template>
        <v-time-picker
          v-if="localTimeDialog"
          v-model="localTime"
          full-width
          format="24hr"
        >
          <div class="flex-grow-1"></div>
          <v-btn
            text
            color="primary"
            @click="
              () => {
                localTime = formatNowTZ('HH:mm');
                $refs.timeDialog.save(localTime);
                updateDt();
              }
            "
            >{{ $t('now') }}
          </v-btn>
          <v-btn text color="primary" @click="localTimeDialog = false"
            >{{ $t('cancel') }}
          </v-btn>
          <v-btn
            text
            color="primary"
            @click="
              () => {
                $refs.timeDialog.save(localTime);
                updateDt();
              }
            "
            >{{ $t('ok') }}
          </v-btn>
        </v-time-picker>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';

@Component
export default class DatetimePicker extends Vue {
  @Prop({ type: [String, Number, Date], default: new Date() })
  readonly date!: string | number | Date;
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
  localDayLabel: string = '';
  localDay: string = '';
  localDayDialog: boolean = false;
  localTimeLabel: string = '';
  localTime: string = '';
  localTimeDialog: boolean = false;

  created(): void {
    this.localDayLabel = this.dayLabel || this.$t('dayPicker').toString();
    this.localTimeLabel = this.timeLabel || this.$t('timePicker').toString();
  }

  @Watch('date', { immediate: true })
  watchStartDtHandler(value: string | number | Date): void {
    this.localDay = this.$moment(value).format(`YYYY-MM-DD`);
    this.localTime = this.$moment(value).format(`HH:mm`);
  }

  @Emit()
  updateDt(): string {
    return `${this.localDay}T${this.localTime}:00${process.env.VUE_APP_TIMEZONE_OFFSET_STRING}`;
  }
}
</script>

<style scoped></style>
