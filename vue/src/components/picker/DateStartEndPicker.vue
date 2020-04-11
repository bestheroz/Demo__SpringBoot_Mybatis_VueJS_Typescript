<template>
  <div>
    <v-row>
      <v-col cols="5">
        <v-dialog
          ref="refStartDialog"
          v-model="startDayDialog"
          :return-value.sync="localStartDay"
          persistent
          :width="460"
          @keydown.esc="startDayDialog = false"
          @keydown.enter="$refs.refStartDialog.save(localStartDay)"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localStartDay"
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
            v-model="localStartDay"
            :locale="envs.LOCALE"
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
              @click="
                $emit('update:start-day', dayjs().startOf('day').toDate())
              "
            >
              오늘
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="$refs.refStartDialog.save(localStartDay)"
            >
              확인
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="1" class="my-auto mx-0 text-center">
        <v-icon>mdi-tilde</v-icon>
      </v-col>
      <v-col cols="5">
        <v-dialog
          ref="refEndDialog"
          v-model="endDayDialog"
          :return-value.sync="localEndDay"
          persistent
          :width="460"
          @keydown.esc="endDayDialog = false"
          @keydown.enter="$refs.refEndDialog.save(localEndDay)"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localEndDay"
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
            v-model="localEndDay"
            :locale="envs.LOCALE"
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
              @click="$emit('update:end-day', dayjs().endOf('day').toDate())"
            >
              오늘
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="$refs.refEndDialog.save(localEndDay)"
            >
              확인
            </v-btn>
          </v-date-picker>
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

@Component({ name: 'DateStartEndPicker' })
export default class extends Vue {
  @Prop({ required: true }) readonly startDay!: string | number | Date | null;
  @Prop({ required: true }) readonly endDay!: string | number | Date | null;
  @Prop({ type: String }) readonly startDayLabel!: string | null;
  @Prop({ type: String }) readonly startDayHint!: string | null;
  @Prop({ type: String }) readonly endDayLabel!: string | null;
  @Prop({ type: String }) readonly endDayHint!: string | null;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly dense!: boolean;

  readonly dayjs: typeof dayjs = dayjs;
  readonly envs: typeof envs = envs;

  localStartDay: string | null = null;
  localEndDay: string | null = null;
  startDayDialog: boolean = false;
  endDayDialog: boolean = false;

  get localStartDayLabel(): string {
    return this.startDayLabel || '날짜선택(From)';
  }

  get localEndDayLabel(): string {
    return this.endDayLabel || '날짜선택(To)';
  }

  @Watch('startDay', { immediate: true })
  watchStartDayHandler(val: string | number | Date): void {
    if (!val || isNaN(new Date(val).getTime())) {
      this.$emit('update:start-dt', dayjs().startOf('day').toDate());
      return;
    }
    this.localStartDay = dayjs(val).format('YYYY-MM-DD');
  }

  @Watch(`endDay`, { immediate: true })
  watchEndDayHandler(val: string | number | Date): void {
    if (!val || isNaN(new Date(val).getTime())) {
      this.$emit('update:end-dt', dayjs().endOf('day').toDate());
      return;
    }
    this.localEndDay = dayjs(val).format('YYYY-MM-DD');
  }

  @Watch('localStartDay')
  @Emit('update:start-day')
  updateStartDay(): Date {
    if (
      this.localEndDay &&
      this.localStartDay &&
      this.localStartDay > this.localEndDay
    ) {
      alertWarning('시작/종료 날짜가 유효하지 않습니다.');
    }
    return dayjs(
      `${this.localStartDay}T00:00:00${envs.TIMEZONE_OFFSET_STRING}`,
    ).toDate();
  }

  @Watch('localEndDay')
  @Emit('update:end-day')
  updateEndDay(): Date {
    if (
      this.localEndDay &&
      this.localStartDay &&
      this.localEndDay < this.localStartDay
    ) {
      alertWarning('시작/종료 날짜가 유효하지 않습니다.');
    }
    return dayjs(
      `${this.localEndDay}T23:59:59.999${envs.TIMEZONE_OFFSET_STRING}`,
    ).toDate();
  }
}
</script>
