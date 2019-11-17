<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="members"
      class="elevation-1"
      sort-by="calories"
    >
      <template v-slot:top>
        <v-toolbar flat>
          <v-toolbar-title>Manage Member</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-dialog max-width="60%" persistent v-model="dialog">
            <template v-slot:activator="{ on }">
              <v-btn
                v-on="on"
                @click="editItem(undefined)"
                class="mb-2"
                color="success"
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
                  <ValidationObserver ref="observer">
                    <v-row>
                      <v-col cols="12">
                        <ValidationProvider
                          name="회원 아이디"
                          ref="memberId"
                          rules="required"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            label="회원 아이디"
                            v-model.trim="member.memberId"
                          />
                          <span class="red--text">{{ errors[0] }}</span>
                        </ValidationProvider>
                      </v-col>
                      <v-col md="6" sm="12">
                        <v-text-field
                          label="비밀번호"
                          v-model.trim="member.memberPw"
                        />
                      </v-col>
                      <v-col md="6" sm="12">
                        <v-text-field
                          label="비밀번호 확인"
                          v-model.trim="memberPwCheck"
                        />
                      </v-col>
                      <v-col cols="12">
                        <ValidationProvider
                          name="회원 명"
                          ref="memberName"
                          rules="required"
                          v-slot="{ errors }"
                        >
                          <v-text-field
                            label="회원 명"
                            v-model.trim="member.memberName"
                          />
                          <span class="red--text">{{ errors[0] }}</span>
                        </ValidationProvider>
                      </v-col>
                      <v-col cols="12">
                        <ValidationProvider
                          name="회원 타입"
                          ref="memberType"
                          rules="required"
                          v-slot="{ errors }"
                        >
                          <v-radio-group
                            class="mt-0"
                            label="회원 타입"
                            mandatory
                            row
                            v-model="member.memberType"
                          >
                            <v-radio
                              :key="object.code"
                              :label="object.codeName"
                              :mandatory="true"
                              :value="object.code"
                              v-for="object in Object.values(MEMBER_TYPE)"
                            ></v-radio>
                          </v-radio-group>
                          <span class="red--text">{{ errors[0] }}</span>
                        </ValidationProvider>
                      </v-col>
                      <v-col md="4">
                        <v-text-field
                          label="로그인 실패 건수"
                          type="number"
                          v-model.number="member.loginFailCnt"
                        />
                      </v-col>
                      <v-col md="4">
                        <v-checkbox
                          label="계정 잠김 여부"
                          v-model="member.closeTf"
                        ></v-checkbox>
                      </v-col>
                      <v-col cols="12">
                        <DatetimePicker
                          :date.sync="member.expired"
                          day-label="계정 만료 날짜"
                          time-label="계정 만료 시간"
                        ></DatetimePicker>
                      </v-col>
                    </v-row>
                  </ValidationObserver>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <div class="flex-grow-1" />
                <v-btn @click="closeModal" color="success" text>
                  {{ $t('$vuetify.close') }}
                </v-btn>
                <v-btn @click="save" color="success" text>
                  {{ $t('save') }}
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:item.memberType="{ item }">
        {{
          MEMBER_TYPE.filter(value => {
            return item.memberType === value.code;
          })[0].codeName
        }}
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
        <v-checkbox :readonly="true" v-model="item.closeTf"></v-checkbox>
      </template>
      <template v-slot:item.expired="{ item }">
        {{ $moment(item.expired).format(envs.DATE_TIME_FORMAT_STRING) }}
      </template>
    </v-data-table>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { Member } from '@/views/manage/member/common/types';
import {
  deleteDataApi,
  getCodeListDataApi,
  getOnlyListDataApi,
  patchDataApi,
  postDataApi,
} from '@/utils/apis';
import { DataTableHeader, SelectItem } from '@/common/types';
import DatetimePicker from '@/components/picker/DatetimePicker.vue';
import _ from 'lodash';
import envs from '@/constants/envs';

@Component({
  components: { DatetimePicker },
})
export default class ManageMember extends Vue {
  readonly envs: typeof envs = envs;
  readonly $moment: any;
  readonly $toasted: any;
  MEMBER_TYPE?: SelectItem[] = [];
  dialog: boolean = false;
  isUpdate: boolean = true;
  headers: DataTableHeader[] = [];
  members: Member[] = [];
  member: Member = {
    expired: this.$moment()
      .add(1, 'months')
      .endOf(`day`)
      .toDate(),
  };
  memberPwCheck: string = ``;

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
    const response = await getOnlyListDataApi<Member[]>(`sample/admin/member/`);
    this.members = response.responseData || [];
  }

  async getMemberType() {
    this.MEMBER_TYPE = await getCodeListDataApi(`MEMBER_TYPE`);
  }

  editItem(member: Member) {
    this.isUpdate = member !== undefined;
    if (member === undefined) {
      this.member = Object.assign(
        {},
        {
          closeTf: false,
          expired: this.$moment()
            .add(1, 'months')
            .endOf(`day`)
            .toDate(),
        },
      );
      this.memberPwCheck = ``;
    } else {
      this.member = Object.assign({}, member);
    }
    this.dialog = true;
  }

  async deleteItem(member: Member) {
    if (!confirm(`Are you sure you want to delete this member?`)) {
      return;
    }

    const response = await deleteDataApi<Member>(
      `sample/admin/member/`,
      member.memberId!,
      this,
    );
    if (_.startsWith(response.responseCode, `S`)) {
      this.getList();
      this.closeModal();
    }
  }

  closeModal() {
    this.dialog = false;
  }

  async save() {
    // @ts-ignore
    const isValid = await this.$refs.observer.validate();
    if (!isValid) {
      this.$swal({
        title: '입력 검증 후 다시 시도해주세요.',
        type: 'error',
      });
      return;
    }
    this.isUpdate ? this.patch() : this.create();
  }

  async create() {
    const response = await postDataApi<Member>(
      `sample/admin/member/`,
      this.member,
      this,
    );
    if (_.startsWith(response.responseCode, `S`)) {
      this.getList();
      this.closeModal();
    }
  }

  async patch() {
    const response = await patchDataApi<Member>(
      `sample/admin/member/`,
      this.member,
      this.member.memberId!,
      this,
    );
    if (_.startsWith(response.responseCode, `S`)) {
      this.getList();
      this.closeModal();
    }
  }
}
</script>

<style scoped></style>
