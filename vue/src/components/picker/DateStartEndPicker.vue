<template>
  <div>
    <v-row>
      <v-col cols="3">
        <v-dialog
          :disabled="disabled"
          :return-value.sync="localStartDay"
          persistent
          ref="startDialog"
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
                  $refs.startDialog.save(localStartDay);
                  updateStartDay();
                }
              "
              color="primary"
              text
              >{{ $t('today') }}
            </v-btn>
            <v-btn @click="localStartDayMenu = false" color="primary" text
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              @click="
                () => {
                  $refs.startDialog.save(localStartDay);
                  updateStartDay();
                }
              "
              color="primary"
              text
              >{{ $t('ok') }}
            </v-btn>
          </v-date-picker>
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
          ref="endDialog"
          v-model="localEndDayMenu"
          width="290px"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              :hint="endDayHint"
              :label="localEndDayLabel"
              :persistent-hint="endDayHint !== undefined"
              prepend-icon="event"
              readonly
              v-model="localEndDay"
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            :min="localStartDay"
            scrollable
            v-if="localEndDayMenu"
            v-model="localEndDay"
          >
            <div class="flex-grow-1"></div>
            <v-btn
              @click="
                () => {
                  localEndDay = $moment().format(envs.DATE_FORMAT_STRING);
                  $refs.endDialog.save(localEndDay);
                  updateEndDay();
                }
              "
              color="primary"
              text
              >{{ $t('today') }}
            </v-btn>
            <v-btn @click="localEndDayMenu = false" color="primary" text
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              @click="
                () => {
                  $refs.endDialog.save(localEndDay);
                  updateEndDay();
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
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';
import envs from '@/constants/envs';

@Component
export default class DateStartEndPicker extends Vue {
  readonly envs: typeof envs = envs;
  @Prop({ type: [String, Number, Date], default: () => new Date() })
  readonly startDay!: string | number | Date;
  @Prop({
    type: [String, Number, Date],
    default: () =>
      new Date(
        new Date().getFullYear(),
        new Date().getMonth() + 1,
        new Date().getDate(),
      ),
  })
  readonly endDay!: string | number | Date;
  @Prop({ type: String, default: undefined })
  readonly startDayLabel!: string;
  @Prop({ type: String, default: undefined })
  readonly startDayHint!: string;
  @Prop({ type: String, default: undefined })
  readonly endDayLabel!: string;
  @Prop({ type: Boolean, default: false })
  readonly endDayHint!: string;
  @Prop({ type: Boolean, default: false })
  readonly disabled!: boolean;

  readonly $moment: any;
  readonly APP_LANGUAGE: string | undefined = process.env.VUE_APP_LANGUAGE;

  localStartDay: string = '';
  localEndDay: string = '';
  localStartDayMenu: boolean = false;
  localEndDayMenu: boolean = false;

  created() {
    this.updateStartDay();
    this.updateEndDay();
  }

  @Watch('startDay', { immediate: true })
  watchStartDayHandler(value: string | number | Date): void {
    this.localStartDay = this.$moment(value).format(
      this.envs.DATE_FORMAT_STRING,
    );
  }

  @Watch(`endDay`, { immediate: true })
  watchEndDayHandler(value: string | number | Date): void {
    this.localEndDay = this.$moment(value).format(this.envs.DATE_FORMAT_STRING);
  }

  @Emit('update:start-day')
  updateStartDay(): Date {
    if (this.localStartDay > this.localEndDay) {
      this.snackbarError();
    }
    return this.$moment(`${this.localStartDay}`).toDate();
  }

  @Emit('update:end-day')
  updateEndDay(): Date {
    if (this.localEndDay < this.localStartDay) {
      this.snackbarError();
    }
    return this.$moment(`${this.localEndDay}T23:59:59.999999`).toDate();
  }

  get localStartDayLabel(): string {
    return this.startDayLabel || this.$t('startDayPicker').toString();
  }

  get localEndDayLabel(): string {
    return this.endDayLabel || this.$t('endDayPicker').toString();
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
