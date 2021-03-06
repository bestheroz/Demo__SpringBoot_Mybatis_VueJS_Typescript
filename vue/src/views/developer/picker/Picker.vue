<template>
  <div>
    <v-card flat>
      <v-card-title> DatePicker.vue </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="3">
            <v-subheader>
              parse Date
              <br />
              {{ now }}}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <date-picker v-model="date" clearable required />
          </v-col>
          <v-col cols="5">
            <span>
              typeof =>
              {{ getType(date) }}
              <br />
              {{ date }}
            </span>
          </v-col>
          <v-col cols="3">
            <v-subheader>
              parse string
              <br />
              {{ now.toISOString() }}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <date-picker v-model="dateParseISOString" />
          </v-col>
          <v-col cols="5">
            <span>
              typeof =>
              {{ getType(dateParseISOString) }}
              <br />
              {{ dateParseISOString }}
            </span>
          </v-col>
          <v-col cols="3">
            <v-subheader>
              parse number
              <br />
              {{ now.getTime() }}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <date-picker v-model="dateParseNumber" />
          </v-col>
          <v-col cols="5">
            <span>
              typeof =>
              {{ getType(dateParseNumber) }}
              <br />
              {{ dateParseNumber }}
            </span>
          </v-col>
          <v-divider class="mr-1" />
        </v-row>
      </v-card-text>
      <v-card-title> DatetimePicker.vue </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="3">
            <v-subheader>
              parse Date
              <br />
              {{ now }}}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <datetime-picker v-model="date2" required clearable />
          </v-col>
          <v-col cols="5">
            <span>
              typeof =>
              {{ getType(date2) }}
              <br />
              {{ date2 }}
            </span>
          </v-col>
          <v-col cols="3">
            <v-subheader>
              parse Date
              <br />
              {{ now }}}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <datetime-picker v-model="date3" required clearable use-seconds />
          </v-col>
          <v-col cols="5">
            <span>
              typeof =>
              {{ getType(date3) }}
              <br />
              {{ date3 }}
            </span>
          </v-col>
          <v-divider class="mr-1" />
        </v-row>
      </v-card-text>
      <v-card-title> DateStartEndPicker.vue </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="4" class="text-right" offset="3">
            <date-start-end-picker :start.sync="start1" :end.sync="end1" />
          </v-col>
          <v-col cols="5">
            {{ start1 }}
            <br />
            {{ end1 }}
          </v-col>
          <v-divider class="mr-1" />
        </v-row>
      </v-card-text>
      <v-card-title> DatetimeStartEndPicker.vue </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="4" class="text-right" offset="3">
            <datetime-start-end-picker :start.sync="start2" :end.sync="end2" />
          </v-col>
          <v-col cols="5">
            {{ start2 }}
            <br />
            {{ end2 }}
          </v-col>
          <v-col cols="4" class="text-right" offset="3">
            <datetime-start-end-picker
              :start.sync="start3"
              :end.sync="end3"
              use-seconds
              required
              clearable
            />
          </v-col>
          <v-col cols="5">
            {{ start3 }}
            <br />
            {{ end3 }}
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import DatePicker from "@/components/picker/DatePicker.vue";
import dayjs from "dayjs";
import DatetimePicker from "@/components/picker/DatetimePicker.vue";
import DateStartEndPicker from "@/components/picker/DateStartEndPicker.vue";
import DatetimeStartEndPicker from "@/components/picker/DatetimeStartEndPicker.vue";
import { DateTime } from "@/common/types";

@Component({
  name: "Picker",
  components: {
    DatetimeStartEndPicker,
    DateStartEndPicker,
    DatetimePicker,
    DatePicker,
  },
})
export default class extends Vue {
  date: DateTime = new Date();
  dateParseISOString: string = dayjs().toISOString();
  dateParseNumber: number = dayjs().toDate().getTime();
  date2: DateTime = new Date();
  date3: DateTime = new Date();
  start1: DateTime = dayjs().add(-1, "day").toDate();
  end1: DateTime = dayjs().add(1, "day").toDate();
  start2: DateTime = dayjs().add(-1, "day").startOf("day").toDate();
  end2: DateTime = dayjs().add(1, "day").endOf("day").toDate();
  start3: DateTime = dayjs().add(-1, "day").startOf("day").toDate();
  end3: DateTime = dayjs().add(1, "day").endOf("day").toDate();

  get now(): Date {
    return new Date();
  }

  protected getType(val: DateTime): string {
    return typeof val === "object"
      ? val instanceof Date
        ? "Date"
        : "object"
      : typeof val;
  }
}
</script>
