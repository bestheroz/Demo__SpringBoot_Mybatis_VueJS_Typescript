<template>
  <v-row>
    <v-col>
      <v-dialog
        :disabled="disabled"
        :return-value.sync="localDay"
        persistent
        ref="dayDialog"
        v-model="localDayDialog"
        width="290px"
      >
        <template v-slot:activator="{ on }">
          <v-text-field
            :hint="dayHint"
            :label="localDayLabel"
            :persistent-hint="dayHint !== undefined"
            prepend-icon="event"
            readonly
            v-model="localDay"
            v-on="on"
          ></v-text-field>
        </template>
        <v-date-picker :locale="envs.LANGUAGE" scrollable v-model="localDay">
          <div class="flex-grow-1"></div>
          <v-btn
            @click="
              () => {
                localDay = $moment().format(envs.DATE_FORMAT_STRING);
                $refs.dayDialog.save(localDay);
                update();
              }
            "
            color="primary"
            text
            >{{ $t('today') }}
          </v-btn>
          <v-btn @click="localDayDialog = false" color="primary" text
            >{{ $t('cancel') }}
          </v-btn>
          <v-btn
            @click="
              () => {
                $refs.dayDialog.save(localDay);
                update();
              }
            "
            color="primary"
            text
            >{{ $t('ok') }}
          </v-btn>
        </v-date-picker>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';
import envs from '@/constants/envs';

@Component
export default class DatePicker extends Vue {
  @Prop({ type: [String, Number, Date], default: () => new Date() })
  readonly date!: string | number | Date;

  @Prop({ type: String, default: undefined })
  readonly dayLabel!: string;

  @Prop({ type: String, default: undefined })
  readonly dayHint!: string;

  @Prop({ type: String, default: undefined })
  readonly timeLabel!: string;

  @Prop({ type: String, default: undefined })
  readonly timeHint!: string;

  @Prop({ type: Boolean, default: false })
  readonly disabled!: boolean;

  readonly $moment: any;
  readonly APP_LANGUAGE: string | undefined = process.env.VUE_APP_LANGUAGE;

  localDay: string = '';
  localDayDialog: boolean = false;

  created() {
    this.update();
  }

  @Watch('date', { immediate: true })
  watchStartDtHandler(value: string | number | Date): void {
    this.localDay = this.$moment(value).format(envs.DATE_FORMAT_STRING);
  }

  @Emit('update:date')
  update(): Date {
    this.localDayDialog = false;
    return this.$moment(`${this.localDay}`);
  }

  get localDayLabel(): string {
    return this.dayLabel || this.$t('dayPicker').toString();
  }
}
</script>

<style scoped></style>
