<template>
  <div>
    <v-row>
      <v-col cols="3">
        <v-dialog
          ref="startDayDialog"
          v-model="localStartDayMenu"
          :return-value.sync="localStartDay"
          persistent
          width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localStartDay"
              :label="localStartDayLabel"
              :hint="startDayHint"
              :persistent-hint="startDayHint !== undefined"
              prepend-icon="event"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="localStartDay"
            :locale="APP_LANGUAGE"
            scrollable
          >
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  localStartDay = formatNowTZ('YYYY-MM-DD');
                  $refs.startDayDialog.save(localStartDay);
                  updateStartDt();
                }
              "
              >{{ $t('today') }}
            </v-btn>
            <v-btn text color="primary" @click="localStartDayMenu = false">{{
              $t('cancel')
            }}</v-btn>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  $refs.startDayDialog.save(localStartDay);
                  updateStartDt();
                }
              "
              >{{ $t('ok') }}</v-btn
            >
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="2">
        <v-dialog
          ref="startTimeDialog"
          v-model="localStartTimeMenu"
          :return-value.sync="localStartTime"
          persistent
          width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localStartTime"
              :label="localStartTimeLabel"
              :hint="startTimeHint"
              :persistent-hint="startTimeHint !== undefined"
              prepend-icon="access_time"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            v-if="localStartTimeMenu"
            v-model="localStartTime"
            full-width
            format="24hr"
          >
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  localStartTime = formatNowTZ('HH:mm');
                  $refs.startTimeDialog.save(localStartTime);
                  updateStartDt();
                }
              "
              >{{ $t('now') }}
            </v-btn>
            <v-btn text color="primary" @click="localStartTimeMenu = false">{{
              $t('cancel')
            }}</v-btn>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  $refs.startTimeDialog.save(localStartTime);
                  updateStartDt();
                }
              "
              >{{ $t('ok') }}
            </v-btn>
          </v-time-picker>
        </v-dialog>
      </v-col>
      <v-col cols="1" class="my-auto mx-0 text-center">
        <v-icon>mdi-tilde</v-icon>
      </v-col>
      <v-col cols="3">
        <v-dialog
          ref="endDayDialog"
          v-model="localEndDayMenu"
          :return-value.sync="localEndDay"
          persistent
          width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localEndDay"
              :label="localEndDayLabel"
              :hint="endDayHint"
              :persistent-hint="endDayHint !== undefined"
              prepend-icon="access_time"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="localEndDay"
            :locale="APP_LANGUAGE"
            scrollable
          >
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  localEndDay = formatNowTZ('YYYY-MM-DD');
                  $refs.endDayDialog.save(localEndDay);
                  updateEndDt();
                }
              "
              >{{ $t('today') }}
            </v-btn>
            <v-btn text color="primary" @click="localEndDayMenu = false">{{
              $t('cancel')
            }}</v-btn>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  $refs.endDayDialog.save(localEndDay);
                  updateEndDt();
                }
              "
              >OK
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="2">
        <v-dialog
          ref="endTimeDialog"
          v-model="localEndTimeMenu"
          :return-value.sync="localEndTime"
          persistent
          width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localEndTime"
              :label="localEndTimeLabel"
              :hint="endTimeHint"
              :persistent-hint="endTimeHint !== undefined"
              prepend-icon="access_time"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            v-if="localEndTimeMenu"
            v-model="localEndTime"
            full-width
            format="24hr"
          >
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  localEndTime = formatNowTZ('HH:mm');
                  $refs.endTimeDialog.save(localEndTime);
                  updateEndDt();
                }
              "
              >{{ $t('now') }}
            </v-btn>
            <v-btn text color="primary" @click="localEndTimeMenu = false">{{
              $t('cancel')
            }}</v-btn>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  $refs.endTimeDialog.save(localEndTime);
                  updateEndDt();
                }
              "
              >{{ $t('ok') }}
            </v-btn>
          </v-time-picker>
        </v-dialog>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';

@Component
export default class DatetimeStartEndPicker extends Vue {
  @Prop({ type: [String, Number, Date], default: new Date() })
  readonly startDt!: string | number | Date;
  @Prop({
    type: [String, Number, Date],
    default: new Date(
      new Date().getFullYear(),
      new Date().getMonth() + 1,
      new Date().getDate(),
    ),
  })
  readonly endDt!: string | number | Date;
  @Prop({ type: String, default: undefined })
  readonly startDayLabel!: string;
  @Prop({ type: String, default: undefined })
  readonly startDayHint!: string;
  @Prop({ type: String, default: undefined })
  readonly endDayLabel!: string;
  @Prop({ type: String, default: undefined })
  readonly endDayHint!: string;
  @Prop({ type: String, default: undefined })
  readonly startTimeLabel!: string;
  @Prop({ type: String, default: undefined })
  readonly startTimeHint!: string;
  @Prop({ type: String, default: undefined })
  readonly endTimeLabel!: string;
  @Prop({ type: String, default: undefined })
  readonly endTimeHint!: string;
  @Prop({ type: Boolean, default: false })
  readonly disabled!: boolean;

  readonly $moment: any;
  readonly APP_LANGUAGE: string | undefined = process.env.VUE_APP_LANGUAGE;

  localStartDay: string = '';
  localStartTime: string = '';
  localEndDay: string = '';
  localEndTime: string = '';
  localStartDayLabel?: string = undefined;
  localEndDayLabel?: string = undefined;
  localStartTimeLabel?: string = undefined;
  localEndTimeLabel?: string = undefined;
  localStartDayMenu: boolean = false;
  localStartTimeMenu: boolean = false;
  localEndDayMenu: boolean = false;
  localEndTimeMenu: boolean = false;

  created(): void {
    this.localStartDayLabel =
      this.startDayLabel || this.$t('startDayPicker').toString();
    this.localEndDayLabel =
      this.endDayLabel || this.$t('endDayPicker').toString();
    this.localStartTimeLabel =
      this.endTimeLabel || this.$t('startTimePicker').toString();
    this.localEndTimeLabel =
      this.endTimeLabel || this.$t('endTimePicker').toString();
  }

  @Watch('startDt', { immediate: true })
  watchStartDtHandler(value: string | number | Date): void {
    this.localStartDay = this.$moment(value).format('YYYY-MM-DD');
    this.localStartTime = this.$moment(value).format('HH:mm');
  }

  @Watch('endDt', { immediate: true })
  watchEndDtHandler(value: string | number | Date): void {
    this.localEndDay = this.$moment(value).format('YYYY-MM-DD');
    this.localEndTime = this.$moment(value).format('HH:mm');
  }

  @Emit()
  updateStartDt(): string {
    if (
      `${this.localStartDay}${this.localStartTime}` >
      `${this.localEndDay}${this.localEndTime}`
    ) {
      this.snackbarError();
    }
    return `${this.localStartDay}T${this.localStartTime}:00${process.env.VUE_APP_TIMEZONE_OFFSET_STRING}`;
  }

  @Emit()
  updateEndDt(): string {
    if (
      `${this.localEndDay}${this.localEndTime}` <
      `${this.localStartDay}${this.localStartTime}`
    ) {
      this.snackbarError();
    }
    return `${this.localEndDay}T${this.localEndTime}:59.999999${process.env.VUE_APP_TIMEZONE_OFFSET_STRING}`;
  }

  snackbarError() {
    this.$store.commit('pushSnack', {
      color: 'warning',
      text: this.$t('checkDateFieldValidation').toString(),
    });
  }
}
</script>

<style scoped></style>
