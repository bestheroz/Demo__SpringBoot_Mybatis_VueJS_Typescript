<template>
  <div>
    <v-layout wrap>
      <v-flex md3>
        <v-menu
          v-model="datepickerFrom"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              :label="fromLabel"
              v-model="fromDay"
              prepend-inner-icon="event"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="fromDay"
            @change="updateStartDt"
            @input="datepickerFrom = false"
            :max="toDay"
            :locale="APP_LANGUAGE"
          >
            <v-spacer></v-spacer>
            <v-btn
              text
              color="primary"
              @click="updateStartDt(formatNowTZ('YYYY-MM-DD'))"
              >{{ $t("msg.today") }}
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-flex>
      <v-flex md2>
        <v-menu
          v-model="timepickerFrom"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="fromTime"
              prepend-inner-icon="access_time"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-time-picker
            v-model="fromTime"
            @change="updateStartDt"
            format="24hr"
          >
            <v-btn
              text
              color="primary"
              @click="updateStartDt(formatNowTZ('HH:mm'))"
              >{{ $t("msg.now") }}
            </v-btn>
          </v-time-picker>
        </v-menu>
      </v-flex>
      <v-flex md1 style="margin:auto 0;">
        <v-layout align-center justify-center>
          <v-icon>mdi-tilde</v-icon>
        </v-layout>
      </v-flex>
      <v-flex md3>
        <v-menu
          v-model="datepickerTo"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              :label="toLabel"
              v-model="toDay"
              prepend-inner-icon="event"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="toDay"
            @change="updateEndDt"
            @input="datepickerTo = false"
            :min="fromDay"
            :locale="APP_LANGUAGE"
          >
            <v-btn
              text
              color="primary"
              @click="updateEndDt(formatNowTZ('YYYY-MM-DD'))"
              >{{ $t("msg.today") }}
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-flex>
      <v-flex md2>
        <v-menu
          v-model="timepickerTo"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="toTime"
              prepend-inner-icon="access_time"
              readonly
              v-on="on"
            >
            </v-text-field>
          </template>
          <v-time-picker v-model="toTime" @change="updateEndDt" format="24hr">
            <v-spacer></v-spacer>
            <v-btn
              text
              color="primary"
              @click="updateEndDt(formatNowTZ('HH:mm'))"
              >{{ $t("msg.now") }}
            </v-btn>
          </v-time-picker>
        </v-menu>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 md11 lg10>
        <v-snackbar v-model="snackbar" color="error" :timeout="2000">
          '날짜 범위의 값이 유효한지 확인해주세요.'
          <v-btn dark text @click="snackbar = false">닫기</v-btn>
        </v-snackbar>
      </v-flex>
    </v-layout>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";

@Component
export default class DatetimeStartEndPicker extends Vue {
  @Prop({ type: [String, Number, Date], default: new Date() })
  readonly startDt!: string | number | Date;
  @Prop({
    type: [String, Number, Date]
  })
  readonly endDt!: string | number | Date;
  @Prop({ type: String, default: "시작 날짜 선택" })
  readonly startDayLabel!: string;
  @Prop({ type: String, default: "시작 시간 선택" })
  readonly endDayLabel!: string;
  @Prop({ type: String, default: "종료 날짜 선택" })
  readonly startTimeLabel!: string;
  @Prop({ type: String, default: "종료 시간 선택" })
  readonly endTimeLabel!: string;
  @Prop({ type: Boolean, default: false })
  readonly disabled!: boolean;

  readonly $moment: any;
  readonly APP_LANGUAGE: string = process.env.VUE_APP_LANGUAGE;
  fromDay: string = "";
  fromTime: string = "";
  toDay: string = "";
  toTime: string = "";
  datepickerFrom: boolean = false;
  timepickerFrom: boolean = false;
  datepickerTo: boolean = false;
  timepickerTo: boolean = false;
  snackbar: boolean = false;

  @Watch("startDt", { immediate: true })
  watchStartDtHandler(value: string | number | Date): void {
    this.fromDay = this.$moment(value).format("YYYY-MM-DD");
    this.fromTime = this.$moment(value).format("HH:mm");
  }

  @Watch("endDt", { immediate: true })
  watchEndDtHandler(value: string | number | Date): void {
    this.toDay = this.$moment(value).format("YYYY-MM-DD");
    this.toTime = this.$moment(value).format("HH:mm");
  }

  @Emit()
  updateStartDt(value: string): string {
    let startDay;
    let startTime;
    if (value.includes("-")) {
      startDay = value;
      startTime = this.fromTime;
    } else {
      startDay = this.fromDay;
      startTime = value;
    }
    if (`${startDay}${startTime}` > `${this.toDay}${this.toTime}`) {
      this.snackbarError();
    }
    this.datepickerFrom = false;
    this.timepickerFrom = false;
    return `${startDay}T${startTime}:00`;
  }

  @Emit()
  updateEndDt(value: string): string {
    let endDay;
    let endTime;
    if (value.includes("-")) {
      endDay = value;
      endTime = this.toTime;
    } else {
      endDay = this.toDay;
      endTime = value;
    }
    if (`${endDay}${endTime}` < `${this.fromDay}${this.fromTime}`) {
      this.snackbarError();
    }
    this.datepickerTo = false;
    this.timepickerTo = false;
    return `${endDay}T${endTime}:59.999999`;
  }

  snackbarError() {
    this.snackbar = true;
  }
}
</script>

<style scoped></style>
