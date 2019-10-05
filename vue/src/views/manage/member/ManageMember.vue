<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="items"
      class="elevation-1"
      sort-by="calories"
    >
      <template v-slot:top>
        <v-toolbar color="white" flat>
          <v-toolbar-title>Manage Member</v-toolbar-title>
          <v-divider class="mx-4" inset vertical />
          <div class="flex-grow-1" />
          <v-dialog max-width="60%" persistent v-model="dialog">
            <template v-slot:activator="{ on }">
              <v-btn
                @click="editItem(undefined)"
                class="mb-2"
                color="primary"
                dark
              >
                New Item
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        :error-messages="getVErrors($v.item.memberId)"
                        @blur="delayTouch($v.item.memberId)"
                        @input="delayTouch($v.item.memberId)"
                        label="회원 아이디"
                        v-model.trim="item.memberId"
                      />
                    </v-col>
                    <v-col cols="12" md="6">
                      <v-text-field
                        :error-messages="getVErrors($v.item.memberPw)"
                        @blur="delayTouch($v.item.memberPw)"
                        @input="delayTouch($v.item.memberPw)"
                        label="비밀번호"
                        v-model.trim="item.memberPw"
                      />
                    </v-col>
                    <v-col cols="12" md="6">
                      <v-text-field
                        :error-messages="getVErrors($v.memberPwCheck)"
                        @blur="delayTouch($v.memberPwCheck)"
                        @input="delayTouch($v.memberPwCheck)"
                        label="비밀번호 확인"
                        v-model.trim="memberPwCheck"
                      />
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        :error-messages="getVErrors($v.item.memberName)"
                        @blur="delayTouch($v.item.memberName)"
                        @input="delayTouch($v.item.memberName)"
                        label="회원 명"
                        v-model.trim="item.memberName"
                      />
                    </v-col>
                    <v-col cols="12">
                      <v-radio-group
                        class="mt-0"
                        label="회원 타입"
                        mandatory
                        persistent-hint
                        row
                        v-model="item.memberType"
                      >
                        <v-radio
                          :key="object.code"
                          :label="object.codeNm"
                          :mandatory="true"
                          :value="object.code"
                          v-for="object in Object.values(MEMBER_TYPE)"
                        ></v-radio>
                      </v-radio-group>
                    </v-col>
                    <v-col cols="12" md="4">
                      <v-text-field
                        :error-messages="getVErrors($v.item.loginFailCnt)"
                        @blur="delayTouch($v.item.loginFailCnt)"
                        @input="delayTouch($v.item.loginFailCnt)"
                        label="로그인 실패 건수"
                        type="number"
                        v-model.number="item.loginFailCnt"
                      />
                    </v-col>
                    <v-col cols="12" md="4">
                      <v-checkbox
                        label="계정 잠김 여부"
                        v-model="item.closeTf"
                      ></v-checkbox>
                    </v-col>
                    <v-col cols="12">
                      <DatetimePicker
                        :date="item.expired"
                        @update="updateExpired"
                        dayLabel="계정 만료 날짜"
                        timeLabel="계정 만료 시간"
                      ></DatetimePicker>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <div class="flex-grow-1" />
                <v-btn @click="closeModal" color="blue darken-1" text>
                  Cancel
                </v-btn>
                <v-btn @click="save" color="blue darken-1" text>
                  Save
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:item.action="{ item }">
        <v-icon @click="editItem(item)" class="mr-2" small>
          edit
        </v-icon>
        <v-icon @click="deleteItem(item)" small>
          delete
        </v-icon>
      </template>
      <template v-slot:item.closeTf="{ item }">
        <v-checkbox :readonly="true" :value="item.closeTf"></v-checkbox>
      </template>
      <template v-slot:item.expired="{ item }">
        {{ $moment(item.expired).format('YYYY-MM-DD HH:mm:ss') }}
      </template>
      <template v-slot:no-data>
        <v-btn @click="getList" color="primary">
          Reset
        </v-btn>
      </template>
    </v-data-table>

    <Alert :options="alertOptions" @update-alert="updateAlert" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Member } from '@/views/manage/member/common/types';
import {
  createDataApi,
  deleteDataApi,
  getCodeListDataApi,
  getOnlyListDataApi,
  patchDataApi,
} from '@/utils/api';
import { DataTableHeader, SelectItem } from '@/common/types';
import DatetimePicker from '@/components/picker/DateTimePicker.vue';
import { Validation } from 'vuelidate';
import {
  delayTouch,
  getVErrors,
  maxLength,
  required,
} from '@/utils/validation-helper';
import Alert from '@/components/alert/Alert.vue';
import { AlertOptions } from '@/components/alert/common/types';
import _ from 'lodash';

@Component({
  components: { DatetimePicker, Alert },
  validations: {
    item: {
      memberId: {
        required,
        maxLength: maxLength(20),
      },
      memberPw: {
        required(value: string) {
          if (
            !this.$data.isUpdate ||
            (this.$data.memberPwCheck !== `` &&
              this.$data.memberPwCheck !== undefined)
          ) {
            if (value === undefined || value === ``) {
              return false;
            } else {
              return true;
            }
          } else {
            return true;
          }
        },
        maxLength: maxLength(20),
      },
      memberName: {
        required,
        maxLength: maxLength(20),
      },
      loginFailCnt: {
        required,
      },
      expired: {
        required,
      },
    },
    memberPwCheck: {
      required(value: string) {
        if (
          !this.$data.isUpdate ||
          (this.$data.item.memberPw !== `` &&
            this.$data.item.memberPw !== undefined)
        ) {
          if (value === undefined || value === ``) {
            return false;
          } else {
            return true;
          }
        } else {
          return true;
        }
      },
      maxLength: maxLength(20),
    },
  },
})
export default class ManageMember extends Vue {
  readonly $moment: any;
  readonly delayTouch: typeof delayTouch = delayTouch;
  readonly getVErrors: typeof getVErrors = getVErrors;
  MEMBER_TYPE?: SelectItem[] = [];
  dialog: boolean = false;
  isUpdate: boolean = true;
  headers: DataTableHeader[] = [];
  items: Member[] = [];
  item: Member = {
    expired: this.$moment()
      .add(1, `months`)
      .endOf(`days`)
      .toDate(),
  };
  memberPwCheck: string = ``;
  alertOptions: AlertOptions = {
    color: undefined, // watch 때문에 option 값들 있어야함
    position: undefined,
    result: undefined,
    snackbar: false,
    timeout: undefined,
    text: undefined,
  };

  created() {
    this.headers = [
      {
        text: `회원 아이디`,
        align: `start`,
        value: `memberId`,
      },
      { text: `회원 명`, value: `memberName` },
      { text: `회원 타입`, value: `memberType` },
      { text: `로그인 실패 건수`, value: `loginFailCnt` },
      { text: `계정 잠김 여부`, value: `closeTf` },
      { text: `계정 만료 일시`, value: `expired` },
      { text: `Actions`, value: `action`, sortable: false },
    ];
    this.getList();
    this.getMemberType();
  }

  get formTitle() {
    return this.isUpdate ? `회원 수정` : `회원 추가`;
  }

  async getList() {
    const result = await getOnlyListDataApi<Member[]>(`/sample/admin/member/`);
    this.items = result.data || [];
  }

  async getMemberType() {
    this.MEMBER_TYPE = await getCodeListDataApi(`MEMBER_TYPE`);
  }

  editItem(item: Member) {
    (this.$v.item as Validation).$reset();
    this.isUpdate = item !== undefined;
    if (item === undefined) {
      this.item = Object.assign(
        {},
        {
          closeTf: false,
          expired: this.$moment()
            .add(1, `months`)
            .endOf(`days`)
            .toDate(),
        },
      );
      this.memberPwCheck = ``;
    } else {
      this.item = item;
    }
    this.dialog = true;
  }

  async deleteItem(item: Member) {
    if (!confirm(`Are you sure you want to delete this item?`)) {
      return;
    }

    const result = await deleteDataApi<Member>(
      `/sample/admin/member/`,
      this.item,
      this.item.memberId!,
      this.alertOptions,
    );
    if (_.startsWith(result.code, `S`)) {
      this.getList();
      this.closeModal();
    }
  }

  closeModal() {
    this.dialog = false;
  }

  async save() {
    const $vForm: Validation = this.$v.item as Validation;
    $vForm.$touch();
    const valid = !$vForm.$pending && !$vForm.$error;
    if (!valid) {
      [
        this.alertOptions.color,
        this.alertOptions.text,
        this.alertOptions.snackbar,
      ] = [`error`, `입력 검증 후 다시 시도해주세요.`, true];
      return;
    }
    this.isUpdate ? this.patch() : this.create();
  }

  async create() {
    const result = await createDataApi<Member>(
      `/sample/admin/member/`,
      this.item,
      this.alertOptions,
    );
    if (_.startsWith(result.code, `S`)) {
      this.getList();
      this.closeModal();
    }
  }

  async patch() {
    const result = await patchDataApi<Member>(
      `/sample/admin/member/`,
      this.item,
      this.item.memberId!,
      this.alertOptions,
    );
    if (_.startsWith(result.code, `S`)) {
      this.getList();
      this.closeModal();
    }
  }

  updateExpired(date: string) {
    this.item.expired = this.$moment(date).toDate();
  }

  updateAlert(snackbar: boolean) {
    this.alertOptions.snackbar = snackbar;
  }
}
</script>

<style scoped></style>
