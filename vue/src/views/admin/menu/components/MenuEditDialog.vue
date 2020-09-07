<template>
  <div>
    <modal
      name="MenuEditDialog"
      draggable
      width="50%"
      height="auto"
      :shiftX="0.4"
      :shiftY="0.15"
      :clickToClose="false"
    >
      <v-card :loading="loading">
        <v-card-title class="py-2 modal-header">
          <v-icon v-if="isNew">mdi-pencil-plus-outline</v-icon>
          <v-icon v-else>mdi-pencil-outline</v-icon>
          메뉴 {{ isNew ? '추가' : '수정' }}
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
import { SelectItem, TableMenuEntity } from '@/common/types';
import { deleteApi, getCodesApi, patchApi, postApi } from '@/utils/apis';
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

  readonly ENDPOINT_URL = 'admin/menus/';
  loading: boolean = false;
  MENU_TYPE: SelectItem[] | null = null;
  isNew: boolean = false;

  async mounted() {
    this.MENU_TYPE = await getCodesApi(`MENU_TYPE`);
  }

  @Watch('syncedDialog')
  watchDialog(val: boolean) {
    if (val) {
      this.isNew = !this.editItem.id;
      this.$refs.observer && (this.$refs.observer as any).reset();
      this.$modal.show('MenuEditDialog');
    } else {
      this.$modal.hide('MenuEditDialog');
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
    const response = await postApi<TableMenuEntity>(
      this.ENDPOINT_URL,
      this.editItem,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      await this.$store.dispatch('setDrawers');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async patch() {
    this.loading = true;
    const response = await patchApi<TableMenuEntity>(
      `${this.ENDPOINT_URL}${this.editItem.id}/`,
      this.editItem,
    );
    this.loading = false;
    if (_.startsWith(response.code, `S`)) {
      await this.$store.dispatch('setDrawers');
      this.syncedDialog = false;
      this.$emit('finished');
    }
  }

  async delete() {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteApi<TableMenuEntity>(
        `${this.ENDPOINT_URL}${this.editItem.id}/`,
      );
      this.loading = false;
      if (_.startsWith(response.code, `S`)) {
        await this.$store.dispatch('setDrawers');
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
