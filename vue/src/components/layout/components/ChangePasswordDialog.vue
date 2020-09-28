<template>
  <div>
    <modal
      name="ChangePasswordDialog"
      draggable
      width="25%"
      height="auto"
      :shiftX="0.84"
      :shiftY="0.2"
      :clickToClose="false"
    >
      <v-card>
        <v-card-title class="py-2 modal-header">
          비밀번호 변경
          <v-spacer />
          <v-btn text small :ripple="false" style="cursor: default">
            <v-icon> mdi-cursor-move</v-icon>
          </v-btn>
          <v-btn text small @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
          </v-btn>
        </v-card-title>
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
        <v-card-actions class="py-1">
          <v-spacer />
          <v-btn text @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
            닫기
          </v-btn>
          <v-btn text @click="save" :loading="loading">
            <v-icon> mdi-content-save-settings-outline</v-icon>
            저장
          </v-btn>
        </v-card-actions>
      </v-card>
    </modal>
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from 'vue-property-decorator';
import { postApi } from '@/utils/apis';
import { ValidationObserver } from 'vee-validate';

const pbkdf2 = require('pbkdf2');

@Component({
  name: 'ChangePasswordDialog',
  components: {},
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;

  readonly ENDPOINT_URL: string = `members/`;
  loading: boolean = false;
  oldPassword: string | null = null;
  password: string | null = null;
  password2: string | null = null;
  show1: boolean = false;
  show2: boolean = false;
  show3: boolean = false;

  beforeDestroy() {
    this.syncedDialog = false;
  }

  @Watch('syncedDialog')
  watchDialog(val: boolean) {
    if (val) {
      this.oldPassword = null;
      this.password = null;
      this.password2 = null;
      this.show1 = false;
      this.show2 = false;
      this.show3 = false;
      this.$refs.observer &&
        (this.$refs.observer as InstanceType<
          typeof ValidationObserver
        >).reset();
      this.$modal.show('ChangePasswordDialog');
    } else {
      this.$modal.hide('ChangePasswordDialog');
    }
  }

  async save() {
    const isValid = await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const response = await postApi<{
      oldPassword: string;
      newPassword: string;
    }>(`${this.ENDPOINT_URL}mine/changePassword/`, {
      oldPassword: pbkdf2
        .pbkdf2Sync(this.oldPassword, 'salt', 1, 32, 'sha512')
        .toString(),
      newPassword: pbkdf2
        .pbkdf2Sync(this.password, 'salt', 1, 32, 'sha512')
        .toString(),
    });
    this.loading = false;
    if (response?.code?.startsWith(`S`)) {
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }
}
</script>
