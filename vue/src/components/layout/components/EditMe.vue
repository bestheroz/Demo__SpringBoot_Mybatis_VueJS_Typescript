<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="25%">
      <v-card>
        <v-alert
          border="bottom"
          colored-border
          color="divider"
          icon="mdi-pencil-outline"
          class="title"
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
              <v-col cols="12" style="text-align: right;">
                <v-btn color="button-edit" @click="changePassword" outlined>
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
      :id="editItem.id"
      v-if="newPasswordDialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Vue } from 'vue-property-decorator';
import { TableMemberEntity } from '@/common/types';
import { patchDataApi } from '@/utils/apis';
import _ from 'lodash';
import DatetimePicker from '@/components/picker/DatetimePicker.vue';
import ChangePassword from '@/components/layout/components/ChangePassword.vue';

@Component({
  name: 'EditMe',
  components: { ChangePassword, DatetimePicker },
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: TableMemberEntity;

  readonly ENDPOINT_URL: string = `members/`;
  loading: boolean = false;
  newPasswordDialog: boolean = false;

  async patch() {
    const isValid = await (this.$refs.observer as any).validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const response = await patchDataApi<TableMemberEntity>(
      this.ENDPOINT_URL,
      { ...this.editItem },
      this.editItem.id!,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      await this.$store.dispatch('setUser');
      await this.$store.dispatch('setMemberCodes');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  changePassword() {
    this.newPasswordDialog = true;
  }
}
</script>
