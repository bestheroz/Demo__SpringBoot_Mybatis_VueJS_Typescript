<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="50%">
      <v-card :loading="loading">
        <v-card-title class="py-2 modal-header">
          <v-icon v-if="mode === '추가'">mdi-pencil-plus-outline</v-icon>
          <v-icon v-else>mdi-pencil-outline</v-icon>
          시스템코드관리-MST {{ mode }}
          <v-spacer />
          <v-btn text small @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12" md="6">
                <ValidationProvider
                  name="그룹코드"
                  rules="required|max:32"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.codeGroup"
                    label="*그룹코드"
                    :counter="32"
                    :error-messages="errors"
                    :disabled="mode !== '추가'"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="6">
                <ValidationProvider
                  name="그룹코드명"
                  rules="required|max:100"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.name"
                    label="그룹코드명"
                    :counter="100"
                    :error-messages="errors"
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
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Vue, Watch } from 'vue-property-decorator';
import { TableCodeGroupEntity } from '@/common/types';
import { deleteApi, patchApi, postApi } from '@/utils/apis';
import _ from 'lodash';
import { confirmDelete } from '@/utils/alerts';

@Component({
  name: 'CodeGroupEditDialog',
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: TableCodeGroupEntity;
  @Prop({ required: true }) readonly mode!: string | null;

  readonly ENDPOINT_URL = 'admin/codeGroups/';
  loading: boolean = false;

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
    const response = await postApi<TableCodeGroupEntity>(
      this.ENDPOINT_URL,
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
    const response = await patchApi<TableCodeGroupEntity>(
      `${this.ENDPOINT_URL}${this.editItem.codeGroup}/`,
      this.editItem,
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
      const response = await deleteApi<TableCodeGroupEntity>(
        `${this.ENDPOINT_URL}${this.editItem.codeGroup}/`,
      );
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        this.$emit('finished');
      }
    }
  }
}
</script>
