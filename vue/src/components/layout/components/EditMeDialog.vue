<template>
  <div>
    <modal
      name="EditMeDialog"
      draggable
      width="25%"
      height="auto"
      :shiftX="0.97"
      :shiftY="0.05"
      :clickToClose="false"
    >
      <v-card :loading="loading">
        <v-card-title class="py-2 modal-header">
          내 정보 수정
          <v-spacer />
          <v-btn text small :ripple="false" style="cursor: default;">
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
    <change-password-dialog :dialog.sync="newPasswordDialog" />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from 'vue-property-decorator';
import { TableMemberEntity } from '@/common/types';
import { getApi, patchApi } from '@/utils/apis';
import _ from 'lodash';
import DatetimePicker from '@/components/picker/DatetimePicker.vue';
import ChangePasswordDialog from '@/components/layout/components/ChangePasswordDialog.vue';

const pbkdf2 = require('pbkdf2');

@Component({
  name: 'EditMeDialog',
  components: { ChangePasswordDialog, DatetimePicker },
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;

  readonly ENDPOINT_URL: string = `members/`;
  editItem: TableMemberEntity = Object.create(null);
  loading: boolean = false;
  show1: boolean = false;
  newPasswordDialog: boolean = false;

  @Watch('syncedDialog')
  async watchDialog(val: boolean) {
    if (val) {
      this.show1 = false;
      const response = await getApi<TableMemberEntity>(
        `${this.ENDPOINT_URL}mine`,
      );
      this.editItem = response.data!;
      this.$refs.observer && (this.$refs.observer as any).reset();
      this.$modal.show('EditMeDialog');
    } else {
      this.$modal.hide('EditMeDialog');
    }
  }

  async save() {
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
