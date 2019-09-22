<template>
  <v-row>
    <v-col>
      <v-dialog
        ref="dialog"
        v-model="dayModal"
        :return-value.sync="day"
        persistent
        width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            v-model="day"
            :label="dayLabel"
            prepend-icon="event"
            readonly
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker v-model="day" :locale="APP_LANGUAGE" scrollable>
          <div class="flex-grow-1"></div>
          <v-btn text color="primary" @click="dayModal = false">Cancel</v-btn>
          <v-btn text color="primary" @click="updateDt">OK</v-btn>
        </v-date-picker>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from "vue-property-decorator";

@Component
export default class DatetimePicker extends Vue {
  @Prop({ type: [String, Number, Date], default: new Date() })
  readonly date!: string | number | Date;
  @Prop({ type: String, default: "날짜 선택" })
  readonly dayLabel!: string;
  @Prop({ type: String, default: "시간 선택" })
  readonly timeLabel!: string;
  @Prop({ type: Boolean, default: false })
  readonly disabled!: boolean;

  readonly $moment: any;
  readonly APP_LANGUAGE: string = process.env.VUE_APP_LANGUAGE;

  day: string = "";
  dayModal: boolean = false;

  @Watch("date", { immediate: true })
  watchStartDtHandler(value: string | number | Date): void {
    this.day = this.$moment(value).format("YYYY-MM-DD");
  }

  @Emit()
  updateDt(): string {
    this.dayModal = false;
    return `${this.day}`;
  }
}
</script>

<style scoped></style>
