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
          시스템코드관리-MST {{ mode }}
        </v-alert>
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
import { TableCodeGroupEntity } from '@/common/types';
import { deleteDataApi, patchDataApi, postDataApi } from '@/utils/apis';
import _ from 'lodash';
import { confirmDelete } from '@/utils/alerts';

@Component({
  name: 'CodeGroupEditDialog',
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: TableCodeGroupEntity;
  @Prop({ required: true }) readonly mode!: string | null;

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
    const response = await postDataApi<TableCodeGroupEntity>(
      `admin/codeGroups/`,
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
    const response = await patchDataApi<TableCodeGroupEntity>(
      `admin/codeGroups/`,
      this.editItem,
      this.editItem.codeGroup!,
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
      const response = await deleteDataApi<TableCodeGroupEntity>(
        `admin/codeGroups/`,
        this.editItem.codeGroup!,
      );
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        this.$emit('finished');
      }
    }
  }
}
</script>
