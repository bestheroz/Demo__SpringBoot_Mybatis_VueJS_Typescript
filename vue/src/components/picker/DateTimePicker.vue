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
                localDay = formatNowTZ('YYYY-MM-DD');
                $refs.dayDialog.save(localDay);
                updated();
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
                updated();
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
                localTime = formatNowTZ('HH:mm');
                $refs.timeDialog.save(localTime);
                updated();
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
                updated();
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
  update(): string {
    return `${this.localDay}T${this.localTime}:00${process.env.VUE_APP_TIMEZONE_OFFSET_STRING}`;
  }
}
</script>

<style scoped></style>
