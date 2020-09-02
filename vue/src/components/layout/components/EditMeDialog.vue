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
          내 정보 수정
        </v-alert>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="editItem.id"
                  label="사용자아이디"
                  :counter="50"
                  disabled
                />
              </v-col>
              <v-col cols="12">
                <ValidationProvider
                  name="사용자명"
                  rules="required|max:100"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.name"
                    label="*사용자명"
                    :counter="100"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="7">
                <ValidationProvider
                  name="비빌번호"
                  rules="required|max:20"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.password"
                    label="*비빌번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="5" class="text-right pt-7">
                <v-btn
                  color="button-edit"
                  @click="newPasswordDialog = true"
                  outlined
                  small
                >
                  비밀번호 변경
                </v-btn>
              </v-col>
              <v-col cols="12">
                <datetime-picker
                  :date="editItem.created"
                  disabled
                  day-label="가입일"
                />
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
          <v-btn color="button-default" text @click="patch" :loading="loading">
            저장
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <change-password
      :dialog.sync="newPasswordDialog"
      v-if="newPasswordDialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue } from 'vue-property-decorator';
import { TableMemberEntity } from '@/common/types';
import { getApi, patchApi } from '@/utils/apis';
import _ from 'lodash';
import DatetimePicker from '@/components/picker/DatetimePicker.vue';
import ChangePassword from '@/components/layout/components/ChangePassword.vue';

const pbkdf2 = require('pbkdf2');

@Component({
  name: 'EditMeDialog',
  components: { ChangePassword, DatetimePicker },
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  readonly ENDPOINT_URL: string = `members/`;
  editItem: TableMemberEntity = Object.create(null);
  loading: boolean = false;
  newPasswordDialog: boolean = false;
  show1: boolean = false;

  async mounted() {
    const response = await getApi<TableMemberEntity>(
      `${this.ENDPOINT_URL}mine`,
    );
    this.editItem = response.data;
  }

  async patch() {
    const isValid = await (this.$refs.observer as any).validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const payload = { ...this.editItem };
    payload.password = pbkdf2
      .pbkdf2Sync(this.editItem.password, 'salt', 1, 32, 'sha512')
      .toString();
    const response = await patchApi<TableMemberEntity>(
      `${this.ENDPOINT_URL}mine`,
      payload,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      await this.$store.dispatch('setUser');
      await this.$store.dispatch('setMemberCodes');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }
}
</script>
