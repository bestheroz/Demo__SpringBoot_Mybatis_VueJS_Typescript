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
              prepend-inner-icon="event"
              readonly
              v-on="on"
              v-model="fromDay"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="fromDay"
            @change="updateStartDay"
            @input="datepickerFrom = false"
            :max="toDay"
          >
            <v-spacer></v-spacer>
            <v-btn
              text
              color="primary"
              @click="updateStartDay(formatNowTZ('YYYY-MM-DD'))"
              >{{ $t("msg.today") }}
            </v-btn>
          </v-date-picker>
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
              prepend-inner-icon="event"
              readonly
              v-on="on"
              v-model="toDay"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="toDay"
            @change="updateEndDay"
            @input="datepickerTo = false"
            :min="fromDay"
          >
            <v-spacer></v-spacer>
            <v-btn
              text
              color="primary"
              @click="updateEndDay(formatNowTZ('YYYY-MM-DD'))"
              >{{ $t("msg.today") }}
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-flex>
    </v-layout>
    <v-layout row wrap>
      <v-flex xs12 md11 lg10>
        <v-snackbar v-model="snackbar" color="error" :timeout="2000">
          {{ $t("msg.askCheckDateFieldValidation") }}
          <v-btn dark text @click="snackbar = false">{{
            $t("msg.close")
          }}</v-btn>
        </v-snackbar>
      </v-flex>
    </v-layout>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";

@Component
export default class DateStartEndPicker extends Vue {
  @Prop({ type: [String, Number, Date], default: new Date() })
  readonly startDay!: string | number | Date;
  @Prop({
    type: [String, Number, Date]
  })
  readonly endDay!: string | number | Date;
  @Prop({ type: String, default: "시작 날짜 선택" })
  readonly startDayLabel!: string;
  @Prop({ type: String, default: "시작 시간 선택" })
  readonly endDayLabel!: string;
  @Prop({ type: Boolean, default: false })
  readonly disabled!: boolean;

  readonly $moment: any;
  readonly APP_LANGUAGE: string = process.env.VUE_APP_LANGUAGE;
  fromDay: string = "";
  toDay: string = "";
  datepickerFrom: boolean = false;
  datepickerTo: boolean = false;
  snackbar: boolean = false;

  @Watch("startDay", { immediate: true })
  watchStartDayHandler(value: string | number | Date): void {
    this.fromDay = this.$moment(value).format("YYYY-MM-DD");
  }

  @Watch("endDay", { immediate: true })
  watchEndDayHandler(value: string | number | Date): void {
    this.toDay = this.$moment(value).format("YYYY-MM-DD");
  }

  @Emit()
  updateStartDay(startDay: string): string {
    if (startDay > this.toDay) {
      this.snackbarError();
    }
    this.datepickerFrom = false;
    return `${startDay}T00:00:00`;
  }

  @Emit()
  updateEndDay(endDay: string): string {
    if (endDay < this.fromDay) {
      this.snackbarError();
    }
    this.datepickerTo = false;
    return `${endDay}T23:59:59.999999`;
  }

  snackbarError() {
    this.snackbar = true;
  }
}
</script>

<style scoped></style>
