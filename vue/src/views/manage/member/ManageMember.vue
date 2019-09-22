<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="items"
      sort-by="calories"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar color="white" flat>
          <v-toolbar-title>Manage Member</v-toolbar-title>
          <v-divider class="mx-4" inset vertical />
          <div class="flex-grow-1" />
          <v-dialog v-model="dialog" persistent max-width="60%">
            <template v-slot:activator="{ on }">
              <v-btn
                class="mb-2"
                color="primary"
                dark
                @click="editItem(undefined)"
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
                        v-model.trim="item.memberId"
                        label="회원 아이디"
                        @input="delayTouch($v.item.memberId)"
                        @blur="delayTouch($v.item.memberId)"
                        :error-messages="getVErrors($v.item.memberId)"
                      />
                    </v-col>
                    <v-col cols="12" md="6">
                      <v-text-field
                        v-model.trim="item.memberPw"
                        label="비밀번호"
                        @input="delayTouch($v.item.memberPw)"
                        @blur="delayTouch($v.item.memberPw)"
                        :error-messages="getVErrors($v.item.memberPw)"
                      />
                    </v-col>
                    <v-col cols="12" md="6">
                      <v-text-field
                        v-model.trim="memberPwCheck"
                        label="비밀번호 확인"
                        @input="delayTouch($v.memberPwCheck)"
                        @blur="delayTouch($v.memberPwCheck)"
                        :error-messages="getVErrors($v.memberPwCheck)"
                      />
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        v-model.trim="item.memberNm"
                        label="회원 명"
                        @input="delayTouch($v.item.memberNm)"
                        @blur="delayTouch($v.item.memberNm)"
                        :error-messages="getVErrors($v.item.memberNm)"
                      />
                    </v-col>
                    <v-col cols="12">
                      <v-radio-group
                        v-model="item.memberTyp"
                        row
                        class="mt-0"
                        label="회원 타입"
                        persistent-hint
                        mandatory
                      >
                        <v-radio
                          v-for="object in Object.values(MEMBER_TYP)"
                          :key="object.code"
                          :label="object.codeNm"
                          :value="object.code"
                          :mandatory="true"
                        ></v-radio>
                      </v-radio-group>
                    </v-col>
                    <v-col cols="12" md="4">
                      <v-text-field
                        v-model.number="item.loginFailCnt"
                        type="number"
                        label="로그인 실패 건수"
                        @input="delayTouch($v.item.loginFailCnt)"
                        @blur="delayTouch($v.item.loginFailCnt)"
                        :error-messages="getVErrors($v.item.loginFailCnt)"
                      />
                    </v-col>
                    <v-col cols="12" md="4">
                      <v-checkbox
                        v-model="item.isClosed"
                        label="계정 잠김 여부"
                      ></v-checkbox>
                    </v-col>
                    <v-col cols="12">
                      <DatetimePicker
                        :date="item.expireDt"
                        dayLabel="계정 만료 날짜"
                        timeLabel="계정 만료 시간"
                        @updateDt="updateDt"
                      ></DatetimePicker>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <div class="flex-grow-1" />
                <v-btn color="blue darken-1" text @click="closeModal">
                  Cancel
                </v-btn>
                <v-btn color="blue darken-1" text @click="save">
                  Save
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:item.action="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)">
          edit
        </v-icon>
        <v-icon small @click="deleteItem(item)">
          delete
        </v-icon>
      </template>
      <template v-slot:item.isClosed="{ item }">
        <v-checkbox :readonly="true" :value="item.isClosed"></v-checkbox>
      </template>
      <template v-slot:item.expireDt="{ item }">
        {{ $moment(item.expireDt).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="getList">
          Reset
        </v-btn>
      </template>
    </v-data-table>

    <Alert :options="alertOptions" @update-alert="updateAlert" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { Member } from "@/views/manage/member/common/types";
import {
  createDataApi,
  deleteDataApi,
  getCodeListDataApi,
  getOnlyListDataApi,
  patchDataApi
} from "@/utils/api";
import { DataTableHeader, SelectItem } from "@/common/types";
import DatetimePicker from "@/components/picker/DateTimePicker.vue";
import { Validation } from "vuelidate";
import {
  delayTouch,
  getVErrors,
  maxLength,
  required
} from "@/utils/validation-helper";
import Alert from "@/components/alert/Alert.vue";
import { AlertOptions } from "@/components/alert/common/types";
import _ from "lodash";

@Component({
  components: { DatetimePicker, Alert },
  validations: {
    item: {
      memberId: {
        required,
        maxLength: maxLength(20)
      },
      memberPw: {
        required(value: string) {
          if (
            !this.$data.isUpdate ||
            (this.$data.memberPwCheck !== "" &&
              this.$data.memberPwCheck !== undefined)
          ) {
            if (value === undefined || value === "") {
              return false;
            } else {
              return true;
            }
          } else {
            return true;
          }
        },
        maxLength: maxLength(20)
      },
      memberNm: {
        required,
        maxLength: maxLength(20)
      },
      loginFailCnt: {
        required
      },
      expireDt: {
        required
      }
    },
    memberPwCheck: {
      required(value: string) {
        if (
          !this.$data.isUpdate ||
          (this.$data.item.memberPw !== "" &&
            this.$data.item.memberPw !== undefined)
        ) {
          if (value === undefined || value === "") {
            return false;
          } else {
            return true;
          }
        } else {
          return true;
        }
      },
      maxLength: maxLength(20)
    }
  }
})
export default class ManageMember extends Vue {
  readonly $moment: any;
  readonly delayTouch: typeof delayTouch = delayTouch;
  readonly getVErrors: typeof getVErrors = getVErrors;
  MEMBER_TYP?: SelectItem[] = [];
  dialog: boolean = false;
  isUpdate: boolean = true;
  headers: DataTableHeader[] = [];
  items: Member[] = [];
  item: Member = {
    expireDt: this.$moment()
      .add(1, "months")
      .endOf("days")
      .toDate()
  };
  memberPwCheck: string = "";
  alertOptions: AlertOptions = {
    color: undefined, // watch 때문에 option 값들 있어야함
    position: undefined,
    result: undefined,
    snackbar: false,
    timeout: undefined,
    text: undefined
  };

  created() {
    this.headers = [
      {
        text: "회원 아이디",
        align: "start",
        value: "memberId"
      },
      { text: "회원 명", value: "memberNm" },
      { text: "회원 타입", value: "memberTyp" },
      { text: "로그인 실패 건수", value: "loginFailCnt" },
      { text: "계정 잠김 여부", value: "isClosed" },
      { text: "계정 만료 일시", value: "expireDt" },
      { text: "Actions", value: "action", sortable: false }
    ];
    this.getList();
    this.getMEMBER_TYP();
  }

  get formTitle() {
    return this.isUpdate ? "회원 수정" : "회원 추가";
  }

  async getList() {
    const result = await getOnlyListDataApi<Member[]>(`/sample/admin/member/`);
    this.items = result.data || [];
  }

  async getMEMBER_TYP() {
    this.MEMBER_TYP = await getCodeListDataApi("MEMBER_TYP");
  }

  editItem(item: Member) {
    (this.$v.item as Validation).$reset();
    this.isUpdate = item !== undefined;
    if (item === undefined) {
      this.item = Object.assign(
        {},
        {
          isClosed: false,
          expireDt: this.$moment()
            .add(1, "months")
            .endOf("days")
            .toDate()
        }
      );
      this.memberPwCheck = "";
    } else {
      this.item = item;
    }
    this.dialog = true;
  }

  async deleteItem(item: Member) {
    const index = this.items.indexOf(item);
    if (!confirm("Are you sure you want to delete this item?")) {
      return;
    }

    const result = await deleteDataApi<Member>(
      `/sample/admin/member/`,
      this.item,
      this.item.memberId!,
      this.alertOptions
    );
    if (_.startsWith(result.code, "S")) {
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
        this.alertOptions.snackbar
      ] = ["error", "입력 검증 후 다시 시도해주세요.", true];
      return;
    }
    this.isUpdate ? this.patch() : this.create();
  }

  async create() {
    const result = await createDataApi<Member>(
      `/sample/admin/member/`,
      this.item,
      this.alertOptions
    );
    if (_.startsWith(result.code, "S")) {
      this.getList();
      this.closeModal();
    }
  }

  async patch() {
    const result = await patchDataApi<Member>(
      `/sample/admin/member/`,
      this.item,
      this.item.memberId!,
      this.alertOptions
    );
    if (_.startsWith(result.code, "S")) {
      this.getList();
      this.closeModal();
    }
  }

  updateDt(date: string) {
    this.item.expireDt = this.$moment(date).toDate();
  }

  updateAlert(snackbar: boolean) {
    console.info(snackbar)
    this.alertOptions.snackbar = snackbar;
  }
}
</script>

<style scoped></style>
