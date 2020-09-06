<template>
  <div>
    <modal
      name="MemberEditDialog"
      draggable
      width="50%"
      height="auto"
      :shiftX="0.2"
      :shiftY="0.05"
      :clickToClose="false"
    >
      <v-card :loading="loading">
        <v-card-title class="py-2 modal-header">
          <v-icon v-if="isNew">mdi-pencil-plus-outline</v-icon>
          <v-icon v-else>mdi-pencil-outline</v-icon>
          사용자 {{ isNew ? '추가' : '수정' }}
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
                    :disabled="!isNew"
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
                    v-model="editItem.name"
                    label="*사용자명"
                    :counter="100"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="권한"
                  rules="required"
                  v-slot="{ errors }"
                >
                  <v-select
                    v-model.number="editItem.authority"
                    :items="
                      AUTHORITY.map((item) => {
                        return { value: parseInt(item.value), text: item.text };
                      })
                    "
                    label="*권한"
                    :error-messages="errors"
                    v-if="AUTHORITY"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="editItem.available"
                  :label="editItem.available | getSwitchLabel"
                />
              </v-col>
              <v-col cols="12" md="8" class="pa-0">
                <datetime-picker :date="editItem.expired" day-label="만료일" />
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
                  rules="max:20"
                  v-slot="{ errors }"
                  v-if="isNew"
                >
                  <v-text-field
                    v-model="editItem.password"
                    label="비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="비밀번호 확인"
                  rules="required|confirmed:password|max:20"
                  v-slot="{ errors }"
                  v-if="isNew && editItem.password"
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
                <v-btn
                  color="warning"
                  @click="resetPassword"
                  v-if="!isNew"
                  outlined
                >
                  비밀번호 초기화
                </v-btn>
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
import { Component, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import { SelectItem, TableMemberEntity } from '@/common/types';
import { deleteApi, getCodesApi, patchApi, postApi } from '@/utils/apis';
import _ from 'lodash';
import DatetimePicker from '@/components/picker/DatetimePicker.vue';
import { confirmDelete } from '@/utils/alerts';

const pbkdf2 = require('pbkdf2');

@Component({
  name: 'MemberEditDialog',
  components: { DatetimePicker },
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: TableMemberEntity;

  readonly ENDPOINT_URL: string = 'admin/members/';
  loading: boolean = false;
  AUTHORITY: SelectItem[] | null = null;
  password2: string | null = null;
  show1: boolean = false;
  show2: boolean = false;
  isNew: boolean = false;

  async mounted() {
    this.AUTHORITY = await getCodesApi('AUTHORITY');
  }

  @Watch('syncedDialog', { immediate: true })
  watchDialog(val: boolean) {
    if (val) {
      this.password2 = '';
      this.isNew = !this.editItem.id;
      this.$refs.observer && (this.$refs.observer as any).reset();
      this.$modal.show('MemberEditDialog');
    } else {
      this.$modal.hide('MemberEditDialog');
    }
  }

  async save() {
    const isValid = await (this.$refs.observer as any).validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.patch();
  }

  async create() {
    this.loading = true;
    const params = { ...this.editItem };
    if (params.password) {
      params.password = pbkdf2
        .pbkdf2Sync(params.password, 'salt', 1, 32, 'sha512')
        .toString();
    }
    const response = await postApi<TableMemberEntity>(
      `${this.ENDPOINT_URL}`,
      params,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      await this.$store.dispatch('setMemberCodes');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async patch() {
    this.loading = true;
    const params = { ...this.editItem };
    if (params.password) {
      params.password = pbkdf2
        .pbkdf2Sync(params.password, 'salt', 1, 32, 'sha512')
        .toString();
    }
    const response = await patchApi<TableMemberEntity>(
      `${this.ENDPOINT_URL}${this.editItem.id}/`,
      params,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      const user = await this.$store.dispatch('getUser');
      if (this.editItem.id === user.id) {
        await this.$store.dispatch('setUser');
      }
      await this.$store.dispatch('setMemberCodes');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async delete() {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteApi<TableMemberEntity>(
        `${this.ENDPOINT_URL}${this.editItem.id}/`,
      );
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        await this.$store.dispatch('setMemberCodes');
        this.$emit('finished');
      }
    }
  }

  async resetPassword() {
    this.loading = true;
    await postApi<TableMemberEntity>(
      `${this.ENDPOINT_URL}${this.editItem.id}/resetPassword`,
      this.editItem,
    );
    this.loading = false;
  }
}
</script>
