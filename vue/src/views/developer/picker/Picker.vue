<template>
  <div>
    <v-card>
      <v-card-text>
        <v-row align="center">
          <v-col cols="12">
            <h2>DatePicker.vue</h2>
          </v-col>
          <v-col cols="3">
            <v-subheader>
              parse Date
              <br />
              {{ new Date() }}}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <date-picker v-model="date" clearable required />
          </v-col>
          <v-col cols="5">
            <span v-if="date">
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
              {{ dayjs().toISOString() }}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <date-picker v-model="dateParseISOString" />
          </v-col>
          <v-col cols="5">
            <span v-if="dateParseISOString">
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
              {{ dayjs().toDate().getTime() }}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <date-picker v-model="dateParseNumber" />
          </v-col>
          <v-col cols="5">
            <span v-if="dateParseNumber">
              typeof =>
              {{ getType(dateParseNumber) }}
              <br />
              {{ dateParseNumber }}
            </span>
          </v-col>
          <v-divider class="mr-1" />
          <v-col cols="12">
            <h2>DatetimePicker.vue</h2>
          </v-col>
          <v-col cols="3">
            <v-subheader>
              parse Date
              <br />
              {{ new Date() }}}
            </v-subheader>
          </v-col>
          <v-col cols="4" class="text-right">
            <datetime-picker v-model="date2" />
          </v-col>
          <v-col cols="5">
            <span v-if="date2">
              typeof =>
              {{ getType(date2) }}
              <br />
              {{ date2 }}
            </span>
          </v-col>
          <v-divider class="mr-1" />
          <v-col cols="12">
            <h2>DateStartEndPicker.vue</h2>
          </v-col>
          <v-col cols="4" class="text-right" offset="3">
            <date-start-end-picker :start.sync="start1" :end.sync="end1" />
          </v-col>
          <v-col cols="5">
            {{ start1 }}
            <br />
            {{ end1 }}
          </v-col>
          <v-divider class="mr-1" />
          <v-col cols="12">
            <h2>DatetimeStartEndPicker.vue</h2>
          </v-col>
          <v-col cols="4" class="text-right" offset="3">
            <datetime-start-end-picker :start.sync="start2" :end.sync="end2" />
          </v-col>
          <v-col cols="5">
            {{ start2 }}
            <br />
            {{ end2 }}
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import DatePicker from '@/components/picker/DatePicker.vue';
import dayjs from 'dayjs';
import DatetimePicker from '@/components/picker/DatetimePicker.vue';
import DateStartEndPicker from '@/components/picker/DateStartEndPicker.vue';
import DatetimeStartEndPicker from '@/components/picker/DatetimeStartEndPicker.vue';

@Component({
  name: 'Picker',
  components: {
    DatetimeStartEndPicker,
    DateStartEndPicker,
    DatetimePicker,
    DatePicker,
  },
})
export default class extends Vue {
  dayjs: typeof dayjs = dayjs;

  date: Date = new Date();
  dateParseISOString: string = dayjs().toISOString();
  dateParseNumber: number = dayjs().toDate().getTime();
  date2: Date = new Date();
  start1: Date = dayjs().add(-1, 'day').toDate();
  end1: Date = dayjs().add(1, 'day').toDate();
  start2: Date = dayjs().add(-1, 'day').toDate();
  end2: Date = dayjs().add(1, 'day').endOf('day').toDate();

  getType(val: any): string {
    return typeof val === 'object'
      ? val instanceof Date
        ? 'Date'
        : 'object'
      : typeof val;
  }
}
</script>
