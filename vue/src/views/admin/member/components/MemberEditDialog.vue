<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="60%">
      <v-card :loading="loading">
        <v-alert
          border="bottom"
          colored-border
          color="success"
          :icon="
            mode === '추가' ? 'mdi-pencil-plus-outline' : 'mdi-pencil-outline'
          "
          class="title"
        >
          사용자관리 {{ mode }}
        </v-alert>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="사용자아이디"
                  rules="required|max:50"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.id"
                    label="*사용자아이디"
                    :counter="50"
                    :error-messages="errors"
                    :disabled="mode !== '추가'"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="사용자명"
                  rules="required|max:100"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.empnm"
                    label="*사용자명"
                    :counter="100"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="사용자그룹"
                  rules="required"
                  v-slot="{ errors }"
                >
                  <v-select
                    v-model="editItem.userGroupId"
                    :items="GROUP_LIST"
                    label="*사용자그룹"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="이메일"
                  rules="email|max:50"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.email"
                    type="email"
                    label="이메일"
                    :counter="50"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="연락처"
                  rules="max:100"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.smsphone"
                    label="연락처"
                    :counter="100"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="세션타임아웃시간"
                  rules="required|numeric"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model.number="editItem.timeout"
                    label="*세션타임아웃시간(초)"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="비밀번호"
                  vid="password"
                  rules="required|max:20"
                  v-slot="{ errors }"
                  v-if="mode === '추가'"
                >
                  <v-text-field
                    v-model="editItem.password"
                    label="*비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                  />
                </ValidationProvider>
                <ValidationProvider
                  name="비밀번호"
                  vid="password"
                  rules="max:20"
                  v-slot="{ errors }"
                  v-else
                >
                  <v-text-field
                    v-model="editItem.password"
                    label="*비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="비밀번호 확인"
                  rules="required|confirmed:password|max:20"
                  v-slot="{ errors }"
                  v-if="editItem.password"
                >
                  <v-text-field
                    v-model="password2"
                    label="*비밀번호 확인"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show2 ? 'text' : 'password'"
                    @click:append="show2 = !show2"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
            </v-row>
          </ValidationObserver>
        </v-card-text>
        <v-divider />
        <v-card-actions>
          <v-spacer />
          <v-btn color="button-default" text @click="syncedDialog = false">
            닫기
          </v-btn>
          <v-btn color="button-default" text @click="save" :loading="loading">
            저장
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import { SelectItem, TableSampleMemberMstVO } from '@/common/types';
import {
  deleteDataApi,
  getListApi,
  patchDataApi,
  postDataApi,
} from '@/utils/apis';
import _ from 'lodash';
import { confirmDelete } from '@/utils/alerts';
const SHA512 = require('crypto-js/sha256');

@Component({
  name: 'MemberEditDialog',
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: TableSampleMemberMstVO;
  @Prop({ required: true }) readonly mode!: string | null;

  loading: boolean = false;
  GROUP_LIST: SelectItem[] | null = null;
  password2: string | null = null;
  show1: boolean = false;
  show2: boolean = false;

  mounted() {
    this.getCodeList();
  }

  @Watch('dialog')
  watchDialog(val: boolean) {
    if (val) {
      this.password2 = '';
      this.$refs.observer && (this.$refs.observer as any).reset();
    }
  }

  async save() {
    const isValid = await (this.$refs.observer as any).validate();
    if (!isValid) {
      return;
    }
    this.mode === '수정' ? this.patch() : this.create();
  }

  async create() {
    this.loading = true;
    const params = { ...this.editItem };
    if (params.password) {
      params.password = SHA512(params.password).toString();
    }
    const response = await postDataApi<TableSampleMemberMstVO>(
      `admin/user/`,
      params,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async patch() {
    this.loading = true;
    const params = { ...this.editItem };
    if (params.password) {
      params.password = SHA512(params.password).toString();
    }
    const response = await patchDataApi<TableSampleMemberMstVO>(
      `admin/user/`,
      params,
      this.editItem.id!,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async delete() {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteDataApi<TableSampleMemberMstVO>(
        `admin/user/`,
        this.editItem.id!,
      );
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        this.$emit('finished');
      }
    }
  }

  async getCodeList() {
    this.loading = true;
    const response = await getListApi<SelectItem[]>(`admin/user/groupList`);
    this.loading = false;
    this.GROUP_LIST = response.data || [];
  }
}
</script>
