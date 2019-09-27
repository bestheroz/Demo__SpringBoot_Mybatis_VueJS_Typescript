<template>
  <div>
    <v-row>
      <v-col cols="3">
        <v-dialog
          ref="startDialog"
          v-model="localStartDayMenu"
          :return-value.sync="localStartDay"
          persistent
          width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localStartDay"
              :label="localStartDayLabel"
              :hint="startDayHint"
              :persistent-hint="startDayHint !== undefined"
              prepend-icon="event"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="localStartDay"
            :locale="APP_LANGUAGE"
            :max="localEndDay"
            scrollable
          >
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  localStartDay = formatNowTZ('YYYY-MM-DD');
                  $refs.startDialog.save(localStartDay);
                  updateStartDay();
                }
              "
              >{{ $t('today') }}
            </v-btn>
            <v-btn text color="primary" @click="localStartDayMenu = false"
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  $refs.startDialog.save(localStartDay);
                  updateStartDay();
                }
              "
              >{{ $t('ok') }}
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
      <v-col cols="1" class="my-auto mx-0 text-center">
        <v-icon>mdi-tilde</v-icon>
      </v-col>
      <v-col cols="3">
        <v-dialog
          ref="endDialog"
          v-model="localEndDayMenu"
          :return-value.sync="localEndDay"
          persistent
          width="290px"
          :disabled="disabled"
        >
          <template v-slot:activator="{ on }">
            <v-text-field
              v-model="localEndDay"
              :label="localEndDayLabel"
              :hint="endDayHint"
              :persistent-hint="endDayHint !== undefined"
              prepend-icon="event"
              readonly
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-if="localEndDayMenu"
            v-model="localEndDay"
            :min="localStartDay"
            scrollable
          >
            <div class="flex-grow-1"></div>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  localEndDay = formatNowTZ('YYYY-MM-DD');
                  $refs.endDialog.save(localEndDay);
                  updateEndDay();
                }
              "
              >{{ $t('today') }}
            </v-btn>
            <v-btn text color="primary" @click="localEndDayMenu = false"
              >{{ $t('cancel') }}
            </v-btn>
            <v-btn
              text
              color="primary"
              @click="
                () => {
                  $refs.endDialog.save(localEndDay);
                  updateEndDay();
                }
              "
              >OK
            </v-btn>
          </v-date-picker>
        </v-dialog>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';

@Component
export default class DateStartEndPicker extends Vue {
  @Prop({ type: [String, Number, Date], default: new Date() })
  readonly startDay!: string | number | Date;
  @Prop({
    type: [String, Number, Date],
    default: new Date(
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
  localStartDayLabel?: string = undefined;
  localEndDayLabel?: string = undefined;
  localStartDayMenu: boolean = false;
  localEndDayMenu: boolean = false;

  created(): void {
    this.localStartDayLabel =
      this.startDayLabel || this.$t('startDayPicker').toString();
    this.localEndDayLabel =
      this.endDayLabel || this.$t('endDayPicker').toString();
  }

  @Watch('startDay', { immediate: true })
  watchStartDayHandler(value: string | number | Date): void {
    this.localStartDay = this.$moment(value).format('YYYY-MM-DD');
  }

  @Watch(`endDay`, { immediate: true })
  watchEndDayHandler(value: string | number | Date): void {
    this.localEndDay = this.$moment(value).format('YYYY-MM-DD');
  }

  @Emit()
  updateStartDay(): string {
    if (this.localStartDay > this.localEndDay) {
      this.snackbarError();
    }
    return `${this.localStartDay}T00:00:00${process.env.VUE_APP_TIMEZONE_OFFSET_STRING}`;
  }

  @Emit()
  updateEndDay(): string {
    if (this.localEndDay < this.localStartDay) {
      this.snackbarError();
    }
    return `${this.localEndDay}T23:59:59.999999${process.env.VUE_APP_TIMEZONE_OFFSET_STRING}`;
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
