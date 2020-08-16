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
          시스템코드관리-DET {{ mode }}
        </v-alert>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="그룹 코드"
                  rules="required"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.codeGroup"
                    label="*그룹 코드"
                    disabled
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <v-switch
                  v-model="editItem.available"
                  :label="editItem.available | getSwitchLabel"
                />
              </v-col>
              <v-col cols="0" md="4" />
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="상세 코드"
                  rules="max:50|required"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.code"
                    label="*상세 코드"
                    :counter="50"
                    :error-messages="errors"
                    :disabled="mode !== '추가'"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  name="상세 코드명"
                  rules="max:100"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.name"
                    label="상세 코드명"
                    :counter="100"
                    :error-messages="errors"
                    clearable
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
                <ValidationProvider
                  name="정렬순서"
                  rules="required|numeric"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model.number="editItem.displayOrder"
                    label="*정렬순서"
                    :error-messages="errors"
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
import { SelectItem, TableCodeEntity } from '@/common/types';
import {
  deleteDataApi,
  getCodeListApi,
  patchDataApi,
  postDataApi,
} from '@/utils/apis';
import _ from 'lodash';
import { confirmDelete } from '@/utils/alerts';

@Component({
  name: 'CodeEditDialog',
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: TableCodeEntity;
  @Prop({ required: true }) readonly mode!: string | null;

  AUTHORITY: SelectItem[] | null = null;

  loading: boolean = false;

  async mounted() {
    this.AUTHORITY = await getCodeListApi('AUTHORITY');
  }

  @Watch('dialog')
  watchDialog(val: boolean) {
    if (val) {
      this.$refs.observer && (this.$refs.observer as any).reset();
    }
  }

  async save() {
    const isValid = await (this.$refs.observer as any).validate();
    if (!isValid) {
      return;
    }
    this.mode === '수정' ? await this.patch() : await this.create();
  }

  async create() {
    this.loading = true;
    const response = await postDataApi<TableCodeEntity>(
      `admin/codes/${this.editItem.codeGroup}`,
      this.editItem,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async patch() {
    this.loading = true;
    const response = await patchDataApi<TableCodeEntity>(
      `admin/codes/`,
      this.editItem,
      { key: this.editItem.codeGroup!, key2: this.editItem.code! },
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      this.syncedDialog = false;
      window.localStorage.removeItem(`code__${this.editItem.codeGroup}`);
      this.$emit('finished');
    }
  }

  async delete() {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteDataApi<TableCodeEntity>(`admin/codes/`, {
        key: this.editItem.codeGroup!,
        key2: this.editItem.code!,
      });
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        window.localStorage.removeItem(`code__${this.editItem.codeGroup}`);
        this.$emit('finished');
      }
    }
  }
}
</script>
