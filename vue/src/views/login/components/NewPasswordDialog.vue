<template>
  <div>
    <v-dialog
      v-model="syncedDialog"
      persistent
      width="400"
      class="elevation-12"
    >
      <v-card :loading="loading">
        <v-card-title class="py-2 modal-header">
          비밀번호 초기화
        </v-card-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12">
                <ValidationProvider
                  name="비밀번호"
                  vid="password"
                  rules="required|max:20"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="password"
                    label="*비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12">
                <ValidationProvider
                  name="비밀번호 확인"
                  rules="required|confirmed:password|max:20"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="password2"
                    label="*비밀번호 확인"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show2 ? 'text' : 'password'"
                    @keyup.enter="save"
                    @click:append="show2 = !show2"
                  />
                </ValidationProvider>
              </v-col>
            </v-row>
          </ValidationObserver>
        </v-card-text>
        <v-divider />
        <v-card-actions>
          <v-spacer />
          <v-btn color="button-default" text @click="save" :loading="loading">
            저장
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Vue } from 'vue-property-decorator';
import { alertAxiosError, ApiDataResult, axiosInstance } from '@/utils/apis';
import _ from 'lodash';
import { alertError } from '@/utils/alerts';

const pbkdf2 = require('pbkdf2');

@Component({
  name: 'NewPasswordDialog',
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly id!: string;

  loading: boolean = false;
  password: string | null = null;
  password2: string | null = null;
  show1: boolean = false;
  show2: boolean = false;

  async save() {
    const inValid = await (this.$refs.observer as any).validate();
    if (!inValid) {
      return;
    }
    this.loading = true;
    try {
      const pbkdf2Password: string = pbkdf2
        .pbkdf2Sync(this.password, 'salt', 1, 32, 'sha512')
        .toString();
      const response = await axiosInstance.post<
        ApiDataResult<{
          accessToken: string;
          refreshToken: string;
        }>
      >(`api/auth/initPassword`, {
        id: this.id,
        password: pbkdf2Password,
      });
      if (_.startsWith(response.data.code, `S`)) {
        this.$toasted.info('패스워드 설정 완료, 재 로그인 해주세요.');
        this.syncedDialog = false;
      } else {
        alertError(response.data.message);
      }
    } catch (e) {
      alertAxiosError(e);
    }
    this.loading = false;
  }
}
</script>
