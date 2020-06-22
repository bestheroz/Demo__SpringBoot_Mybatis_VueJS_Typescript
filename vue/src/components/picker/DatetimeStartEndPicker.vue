<template>
  <div>
    <v-row>
      <v-col cols="3" :class="dense ? 'pb-0' : ''">
        <v-dialog
          ref="refStartDayDialog"
          v-model="startDayDialog"
          :return-value.sync="startDay"
          persistent
          :width="460"
          @keydown.esc="startDayDialog = false"
          @keydown.enter="$refs.refStartDayDialog.save(startDay)"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="startDay"
              :label="localStartDayLabel"
              :messages="startDayHint"
              prepend-icon="mdi-calendar"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="dense"
              v-on="on"
            />
          </template>
          <v-date-picker
            v-model="startDay"
            :locale="envs.LOCALE"
            :max="endDay"
            landscape
            reactive
            scrollable
          >
            <v-btn text color="primary" @click="startDayDialog = false">
              취소
            </v-btn>
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="startDay = dayjs().format('YYYY-MM-DD')"
            >
              오늘
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="$refs.refStartDayDialog.save(startDay)"
            >
              확인
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="2" :class="dense ? 'pb-0' : ''">
        <v-dialog
          ref="refStartTimeDialog"
          v-model="startTimeDialog"
          :return-value.sync="startTime"
          persistent
          :width="460"
          @keydown.esc="startTimeDialog = false"
          @keydown.enter="$refs.refStartTimeDialog.save(startTime)"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="startTime"
              :label="localStartTimeLabel"
              :messages="startTimeHint"
              prepend-icon="mdi-clock-outline"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="dense"
              v-on="on"
            />
          </template>
          <v-time-picker
            v-model="startTime"
            format="24hr"
            :max="startDay === endDay ? endTime : ''"
            landscape
          >
            <v-btn text color="primary" @click="startTimeDialog = false">
              취소
            </v-btn>
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="$emit('update:start-dt', new Date())"
            >
              지금
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="$refs.refStartTimeDialog.save(startTime)"
            >
              확인
            </v-btn>
          </v-time-picker>
        </v-dialog>
      </v-col>
      <v-col
        cols="1"
        :class="
          dense ? 'pb-0 my-auto mx-0 text-center' : 'my-auto mx-0 text-center'
        "
      >
        <v-icon>mdi-tilde</v-icon>
      </v-col>
      <v-col cols="3" :class="dense ? 'pb-0' : ''">
        <v-dialog
          ref="refEndDayDialog"
          v-model="endDayDialog"
          :return-value.sync="endDay"
          persistent
          :width="460"
          @keydown.esc="endDayDialog = false"
          @keydown.enter="$refs.refEndDayDialog.save(endDay)"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="endDay"
              :label="localEndDayLabel"
              :messages="endDayHint"
              prepend-icon="mdi-calendar"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="dense"
              v-on="on"
            />
          </template>
          <v-date-picker
            v-model="endDay"
            :locale="envs.LOCALE"
            :min="startDay"
            landscape
            reactive
            scrollable
          >
            <v-btn text color="primary" @click="endDayDialog = false">
              취소
            </v-btn>
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="endDay = dayjs().format('YYYY-MM-DD')"
            >
              오늘
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="$refs.refEndDayDialog.save(endDay)"
            >
              확인
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="2" :class="dense ? 'pb-0' : ''">
        <v-dialog
          ref="refEndTimeDialog"
          v-model="endTimeDialog"
          :return-value.sync="endTime"
          persistent
          :width="460"
          @keydown.esc="endTimeDialog = false"
          @keydown.enter="$refs.refEndTimeDialog.save(endTime)"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="endTime"
              :label="localEndTimeLabel"
              :messages="endTimeHint"
              prepend-icon="mdi-clock-outline"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="dense"
              v-on="on"
            />
          </template>
          <v-time-picker
            v-model="endTime"
            format="24hr"
            :min="startDay === endDay ? startTime : ''"
            landscape
          >
            <v-btn text color="primary" @click="endTimeDialog = false">
              취소
            </v-btn>
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="$emit('update:end-dt', new Date())"
            >
              지금
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="$refs.refEndTimeDialog.save(endTime)"
              >확인
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
import dayjs from 'dayjs';
import { alertWarning } from '@/utils/alerts';

@Component({ name: 'DatetimeStartEndPicker' })
export default class extends Vue {
  @Prop({ required: true }) readonly startDt!: string | number | Date | null;
  @Prop({ required: true }) readonly endDt!: string | number | Date | null;
  @Prop({ type: String }) readonly startDayLabel!: string | null;
  @Prop({ type: String }) readonly startDayHint!: string | null;
  @Prop({ type: String }) readonly endDayLabel!: string | null;
  @Prop({ type: String }) readonly endDayHint!: string | null;
  @Prop({ type: String }) readonly startTimeLabel!: string | null;
  @Prop({ type: String }) readonly startTimeHint!: string | null;
  @Prop({ type: String }) readonly endTimeLabel!: string | null;
  @Prop({ type: String }) readonly endTimeHint!: string | null;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly dense!: boolean;

  readonly dayjs: typeof dayjs = dayjs;
  readonly envs: typeof envs = envs;

  startDay: string | null = null;
  startDayDialog: boolean = false;
  startTime: string | null = null;
  startTimeDialog: boolean = false;
  endDay: string | null = null;
  endDayDialog: boolean = false;
  endTime: string | null = null;
  endTimeDialog: boolean = false;

  get localStartDayLabel(): string {
    return this.startDayLabel || '날짜선택(From)';
  }

  get localEndDayLabel(): string {
    return this.endDayLabel || '날짜선택(To)';
  }

  get localStartTimeLabel(): string {
    return this.endTimeLabel || '시간선택(From)';
  }

  get localEndTimeLabel(): string {
    return this.endTimeLabel || '시간선택(To)';
  }

  @Watch('startDt', { immediate: true })
  watchStartDtHandler(val: string | number | Date): void {
    if (!val || isNaN(new Date(val).getTime())) {
      this.$emit('update:start-dt', dayjs().startOf('day').toDate());
      return;
    }
    this.startDay = dayjs(val).format('YYYY-MM-DD');
    this.startTime = dayjs(val).format('HH:mm');
  }

  @Watch('endDt', { immediate: true })
  watchEndDtHandler(val: string | number | Date): void {
    if (!val || isNaN(new Date(val).getTime())) {
      this.$emit('update:end-dt', dayjs().endOf('day').toDate());
      return;
    }
    this.endDay = dayjs(val).format('YYYY-MM-DD');
    this.endTime = dayjs(val).format('HH:mm');
  }

  @Watch('startDay')
  @Watch('startTime')
  @Emit('update:start-dt')
  updateStartDt(): Date {
    if (`${this.startDay}${this.startTime}` > `${this.endDay}${this.endTime}`) {
      alertWarning('시작/종료 날짜가 유효하지 않습니다.');
    }
    return dayjs(`${this.startDay}T${this.startTime}:00`).toDate();
  }

  @Watch('endDay')
  @Watch('endTime')
  @Emit('update:end-dt')
  updateEndDt(): Date {
    if (`${this.endDay}${this.endTime}` < `${this.startDay}${this.startTime}`) {
      alertWarning('시작/종료 날짜가 유효하지 않습니다.');
    }
    return dayjs(`${this.endDay}T${this.endTime}:59.999`).toDate();
  }
}
</script>
