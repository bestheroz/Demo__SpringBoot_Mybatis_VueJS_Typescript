<template>
  <div>
    <v-row>
      <v-col cols="3">
        <v-dialog
          :disabled="disabled"
          :return-value.sync="localStartDay"
          persistent
          ref="startDayDialog"
          v-model="localStartDayMenu"
          width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              :hint="startDayHint"
              :label="localStartDayLabel"
              :persistent-hint="startDayHint !== undefined"
              prepend-icon="event"
              readonly
              v-model="localStartDay"
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            :locale="envs.LANGUAGE"
            :max="localEndDay"
            scrollable
            v-model="localStartDay"
          >
            <div class="flex-grow-1"></div>
            <v-btn
              @click="
                () => {
                  localStartDay = $moment().format(envs.DATE_FORMAT_STRING);
                  $refs.startDayDialog.save(localStartDay);
                  updateStartDt();
                }
              "
              color="success"
              text
              >{{ $t('today') }}
            </v-btn>
            <v-btn @click="localStartDayMenu = false" color="success" text
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              @click="
                () => {
                  $refs.startDayDialog.save(localStartDay);
                  updateStartDt();
                }
              "
              color="success"
              text
              >{{ $t('ok') }}
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="2">
        <v-dialog
          :disabled="disabled"
          :return-value.sync="localStartTime"
          persistent
          ref="startTimeDialog"
          v-model="localStartTimeMenu"
          width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              :hint="startTimeHint"
              :label="localStartTimeLabel"
              :persistent-hint="startTimeHint !== undefined"
              prepend-icon="access_time"
              readonly
              v-model="localStartTime"
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            :max="localStartDay === localEndDay ? localEndTime : ''"
            format="24hr"
            full-width
            v-if="localStartTimeMenu"
            v-model="localStartTime"
          >
            <div class="flex-grow-1"></div>
            <v-btn
              @click="
                () => {
                  localStartTime = $moment().format(envs.TIME_FORMAT_STRING);
                  $refs.startTimeDialog.save(localStartTime);
                  updateStartDt();
                }
              "
              color="success"
              text
              >{{ $t('now') }}
            </v-btn>
            <v-btn @click="localStartTimeMenu = false" color="success" text
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              @click="
                () => {
                  $refs.startTimeDialog.save(localStartTime);
                  updateStartDt();
                }
              "
              color="success"
              text
              >{{ $t('ok') }}
            </v-btn>
          </v-time-picker>
        </v-dialog>
      </v-col>
      <v-col class="my-auto mx-0 text-center" cols="1">
        <v-icon>mdi-tilde</v-icon>
      </v-col>
      <v-col cols="3">
        <v-dialog
          :disabled="disabled"
          :return-value.sync="localEndDay"
          persistent
          ref="endDayDialog"
          v-model="localEndDayMenu"
          width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              :hint="endDayHint"
              :label="localEndDayLabel"
              :persistent-hint="endDayHint !== undefined"
              prepend-icon="access_time"
              readonly
              v-model="localEndDay"
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            :locale="envs.LANGUAGE"
            :min="localStartDay"
            scrollable
            v-model="localEndDay"
          >
            <div class="flex-grow-1"></div>
            <v-btn
              @click="
                () => {
                  localEndDay = $moment().format(envs.DATE_FORMAT_STRING);
                  $refs.endDayDialog.save(localEndDay);
                  updateEndDt();
                }
              "
              color="success"
              text
              >{{ $t('today') }}
            </v-btn>
            <v-btn @click="localEndDayMenu = false" color="success" text
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              @click="
                () => {
                  $refs.endDayDialog.save(localEndDay);
                  updateEndDt();
                }
              "
              color="success"
              text
              >{{ $t('ok') }}
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="2">
        <v-dialog
          :disabled="disabled"
          :return-value.sync="localEndTime"
          persistent
          ref="endTimeDialog"
          v-model="localEndTimeMenu"
          width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              :hint="endTimeHint"
              :label="localEndTimeLabel"
              :persistent-hint="endTimeHint !== undefined"
              prepend-icon="access_time"
              readonly
              v-model="localEndTime"
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            :min="localStartDay === localEndDay ? localStartTime : ''"
            format="24hr"
            full-width
            v-if="localEndTimeMenu"
            v-model="localEndTime"
          >
            <div class="flex-grow-1"></div>
            <v-btn
              @click="
                () => {
                  localEndTime = $moment().format(envs.TIME_FORMAT_STRING);
                  $refs.endTimeDialog.save(localEndTime);
                  updateEndDt();
                }
              "
              color="success"
              text
              >{{ $t('now') }}
            </v-btn>
            <v-btn @click="localEndTimeMenu = false" color="success" text
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              @click="
                () => {
                  $refs.endTimeDialog.save(localEndTime);
                  updateEndDt();
                }
              "
              color="success"
              text
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
import envs from '@/constants/envs';

@Component
export default class DatetimeStartEndPicker extends Vue {
  readonly envs: typeof envs = envs;
  @Prop({ type: [String, Number, Date], default: () => new Date() })
  readonly startDt!: string | number | Date;

  @Prop({
    type: [String, Number, Date],
    default: () =>
      new Date(
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
  localStartDayMenu: boolean = false;
  localStartTimeMenu: boolean = false;
  localEndDayMenu: boolean = false;
  localEndTimeMenu: boolean = false;

  created() {
    this.updateStartDt();
    this.updateEndDt();
  }

  @Watch('startDt', { immediate: true })
  watchStartDtHandler(value: string | number | Date): void {
    this.localStartDay = this.$moment(value).format(
      this.envs.DATE_FORMAT_STRING,
    );
    this.localStartTime = this.$moment(value).format(
      this.envs.TIME_FORMAT_STRING,
    );
  }

  @Watch('endDt', { immediate: true })
  watchEndDtHandler(value: string | number | Date): void {
    this.localEndDay = this.$moment(value).format(this.envs.DATE_FORMAT_STRING);
    this.localEndTime = this.$moment(value).format(
      this.envs.TIME_FORMAT_STRING,
    );
  }

  @Emit('update:startDt')
  updateStartDt(): Date {
    if (
      `${this.localStartDay}${this.localStartTime}` >
      `${this.localEndDay}${this.localEndTime}`
    ) {
      this.snackbarError();
    }
    return this.$moment(
      `${this.localStartDay}T${this.localStartTime}:00`,
    ).toDate();
  }

  @Emit('update:endDt')
  updateEndDt(): Date {
    if (
      `${this.localEndDay}${this.localEndTime}` <
      `${this.localStartDay}${this.localStartTime}`
    ) {
      this.snackbarError();
    }
    return this.$moment(
      `${this.localEndDay}T${this.localEndTime}:59.999999`,
    ).toDate();
  }

  get localStartDayLabel(): string {
    return this.startDayLabel || this.$t('startDayPicker').toString();
  }

  get localEndDayLabel(): string {
    return this.endDayLabel || this.$t('endDayPicker').toString();
  }

  get localStartTimeLabel(): string {
    return this.endTimeLabel || this.$t('startTimePicker').toString();
  }

  get localEndTimeLabel(): string {
    return this.endTimeLabel || this.$t('endTimePicker').toString();
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
