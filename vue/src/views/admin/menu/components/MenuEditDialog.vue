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
          메뉴 데이터 관리 {{ mode }}
        </v-alert>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12" md="3">
                <ValidationProvider
                  name="상위메뉴아이디"
                  rules="required"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.pMenuId"
                    label="*상위메뉴아이디"
                    :error-messages="errors"
                    disabled
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="3">
                <ValidationProvider
                  name="타입"
                  rules="required"
                  v-slot="{ errors }"
                >
                  <v-select
                    v-model="editItem.type"
                    :items="W004"
                    label="*타입"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="6">
                <ValidationProvider
                  name="화면 링크"
                  rules="max:255"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.url"
                    label="화면 링크"
                    :counter="255"
                    :disabled="editItem.type === 'G'"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="3">
                <ValidationProvider
                  name="메뉴아이디"
                  rules="required|max:100"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.id"
                    label="*메뉴아이디"
                    :counter="100"
                    :error-messages="errors"
                    :disabled="mode === '수정'"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="3">
                <ValidationProvider
                  name="메뉴명"
                  rules="required|max:50"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.menuNmKor"
                    label="*메뉴명"
                    :counter="50"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="2">
                <ValidationProvider
                  name="메뉴순차 번호"
                  rules="required|numeric"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model.number="editItem.seq"
                    label="*메뉴순차 번호"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="3" v-if="editItem.lvl === 2">
                <v-text-field v-model="editItem.menuIcon" label="메뉴 아이콘" />
              </v-col>
              <v-col cols="12" md="1" v-if="editItem.lvl === 2">
                <v-icon> {{ editItem.menuIcon }} </v-icon>
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
import { SelectItem, TableSampleMenuMstVO } from '@/common/types';
import {
  deleteDataApi,
  getCodeListApi,
  patchDataApi,
  postDataApi,
} from '@/utils/apis';
import _ from 'lodash';
import { confirmDelete } from '@/utils/alerts';

@Component({
  name: 'MenuEditDialog',
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: TableSampleMenuMstVO;
  @Prop({ required: true }) readonly mode!: string | null;

  loading: boolean = false;
  W004: SelectItem[] | null = null;

  async mounted() {
    this.W004 = await getCodeListApi(`W004`);
  }

  @Watch('dialog')
  watchDialog(val: boolean) {
    if (val) {
      this.$refs.observer && (this.$refs.observer as any).reset();
    } else {
      this.$storage.remove('drawer');
    }
  }

  async save() {
    const isValid = await (this.$refs.observer as any).validate();
    if (!isValid) {
      return;
    }
    this.mode === '수정' ? this.patch() : this.create();
  }

  async create() {
    this.loading = true;
    const response = await postDataApi<TableSampleMenuMstVO>(
      `admin/menu/`,
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
    const response = await patchDataApi<TableSampleMenuMstVO>(
      `admin/menu/`,
      this.editItem,
      this.editItem.id!,
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
      const response = await deleteDataApi<TableSampleMenuMstVO>(
        `admin/menu/`,
        this.editItem.id!,
      );
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        this.$emit('finished');
      }
    }
  }
}
</script>
