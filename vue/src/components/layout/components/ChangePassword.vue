<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="25%">
      <v-card>
        <v-alert
          border="bottom"
          colored-border
          color="divider"
          icon="mdi-pencil-outline"
          class="title mb-0"
        >
          비밀번호 변경
        </v-alert>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12">
                <ValidationProvider
                  name="이전 비밀번호"
                  vid="oldPassword"
                  rules="required|max:20"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="oldPassword"
                    label="*이전 비밀번호"
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
                    :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show2 ? 'text' : 'password'"
                    @click:append="show2 = !show2"
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
                    :append-icon="show3 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show3 ? 'text' : 'password'"
                    @keyup.enter="save"
                    @click:append="show3 = !show3"
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
import { Component, Prop, PropSync, Vue } from 'vue-property-decorator';
import { postApi } from '@/utils/apis';
import _ from 'lodash';

const pbkdf2 = require('pbkdf2');

@Component({
  name: 'ChangePassword',
  components: {},
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly id!: string;

  readonly ENDPOINT_URL: string = `members/`;
  loading: boolean = false;
  oldPassword: string | null = null;
  password: string | null = null;
  password2: string | null = null;
  show1: boolean = false;
  show2: boolean = false;
  show3: boolean = false;

  async save() {
    const isValid = await (this.$refs.observer as any).validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const response = await postApi<{
      oldPassword: string;
      newPassword: string;
    }>(`${this.ENDPOINT_URL}${this.id}/changePassword/`, {
      oldPassword: pbkdf2
        .pbkdf2Sync(this.oldPassword, 'salt', 1, 32, 'sha512')
        .toString(),
      newPassword: pbkdf2
        .pbkdf2Sync(this.password, 'salt', 1, 32, 'sha512')
        .toString(),
    });
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }
}
</script>
