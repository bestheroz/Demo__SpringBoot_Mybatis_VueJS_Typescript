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
                  name="메뉴명"
                  rules="required|max:50"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.name"
                    label="*메뉴명"
                    :counter="50"
                    :error-messages="errors"
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
                    :items="filterMenuType(editItem)"
                    label="*타입"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="6">
                <ValidationProvider
                  name="링크 URL"
                  rules="max:255"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model="editItem.url"
                    label="링크 URL"
                    :counter="255"
                    :disabled="editItem.type === 'G'"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="2">
                <ValidationProvider
                  name="메뉴 순서"
                  rules="required|numeric"
                  v-slot="{ errors }"
                >
                  <v-text-field
                    v-model.number="editItem.displayOrder"
                    label="*메뉴 순서"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4" v-if="editItem.level === 2">
                <v-text-field v-model="editItem.icon" label="메뉴 아이콘" />
              </v-col>
              <v-col cols="12" md="1" v-if="editItem.level === 2">
                <v-icon> {{ editItem.icon }}</v-icon>
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
import { SelectItem, TableMenuEntity } from '@/common/types';
import {
  deleteDataApi,
  getCodeListApi,
  patchDataApi,
  postDataApi,
} from '@/utils/apis';
import _ from 'lodash';
import { confirmDelete } from '@/utils/alerts';

interface MenuVO extends TableMenuEntity {
  level: number;
}

@Component({
  name: 'MenuEditDialog',
})
export default class extends Vue {
  @PropSync('dialog', { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly editItem!: MenuVO;
  @Prop({ required: true }) readonly mode!: string | null;

  readonly END_POINT = 'admin/menus/';
  loading: boolean = false;
  MENU_TYPE: SelectItem[] | null = null;

  async mounted() {
    this.MENU_TYPE = await getCodeListApi(`MENU_TYPE`);
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
    const response = await postDataApi<TableMenuEntity>(
      this.END_POINT,
      this.editItem,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      await this.$store.dispatch('clearDrawer');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async patch() {
    this.loading = true;
    const response = await patchDataApi<TableMenuEntity>(
      this.END_POINT,
      this.editItem,
      this.editItem.id!,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      await this.$store.dispatch('clearDrawer');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async delete() {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteDataApi<TableMenuEntity>(
        this.END_POINT,
        this.editItem.id!,
      );
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        await this.$store.dispatch('clearDrawer');
        this.$emit('finished');
      }
    }
  }

  filterMenuType(item: MenuVO) {
    if (item.parentId !== 1) {
      return this.MENU_TYPE!.filter((item) => item.value !== 'G');
    }
    return this.MENU_TYPE;
  }
}
</script>
