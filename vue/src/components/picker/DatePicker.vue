<template>
  <div>
    <v-row>
      <v-col>
        <v-dialog
          ref="refDayDialog"
          v-model="dayDialog"
          :return-value.sync="day"
          persistent
          :width="460"
          @keydown.esc="dayDialog = false"
          @keydown.enter="$refs.refDayDialog.save(day)"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="day"
              :label="localDayLabel"
              :messages="dayHint"
              prepend-icon="mdi-calendar"
              readonly
              :disabled="disabled"
              :dense="dense"
              :hide-details="dense"
              v-on="on"
            />
          </template>
          <v-date-picker
            v-model="day"
            :locale="envs.LOCALE"
            landscape
            reactive
            scrollable
          >
            <v-btn text color="primary" @click="dayDialog = false">
              취소
            </v-btn>
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="$emit('update:date', dayjs().startOf('day').toDate())"
            >
              오늘
            </v-btn>
            <v-btn text color="primary" @click="$refs.refDayDialog.save(day)">
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

@Component({ name: 'DatePicker' })
export default class extends Vue {
  @Prop({ required: true }) readonly date!: string | number | Date | null;
  @Prop({ type: String }) readonly dayLabel!: string | null;
  @Prop({ type: String }) readonly dayHint!: string | null;
  @Prop({ type: String }) readonly timeLabel!: string | null;
  @Prop({ type: String }) readonly timeHint!: string | null;
  @Prop({ type: Boolean, default: false }) readonly disabled!: boolean;
  @Prop({ type: Boolean, default: false }) readonly dense!: boolean;

  readonly dayjs: typeof dayjs = dayjs;
  readonly envs: typeof envs = envs;

  day: string | null = null;
  dayDialog: boolean = false;

  get localDayLabel(): string {
    return this.dayLabel || '날짜선택';
  }

  @Watch('date', { immediate: true })
  watchStartDtHandler(val: string | number | Date): void {
    if (!val || isNaN(new Date(val).getTime())) {
      this.$emit('update:day', new Date());
      return;
    }
    this.day = dayjs(val).format(`YYYY-MM-DD`);
  }

  @Watch('day')
  @Emit('update:date')
  update(val: string): Date {
    return dayjs(val).toDate();
  }
}
</script>
