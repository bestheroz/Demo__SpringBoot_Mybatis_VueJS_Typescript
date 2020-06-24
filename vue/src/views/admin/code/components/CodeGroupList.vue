<template>
  <div>
    <v-card>
      <v-card-text class="py-1">
        <v-alert
          border="bottom"
          colored-border
          color="success"
          icon="mdi-format-list-checkbox"
          dense
          text
          class="mb-0"
        >
          코드 관리 - Master
        </v-alert>
        <v-data-table
          must-sort
          fixed-header
          v-model="selected"
          :loading="loading"
          :headers="headers"
          :items="filteredItems"
          :sort-by="sortBy"
          :sort-desc="sortDesc"
          item-key="codeGroup"
          single-select
          show-select
          dense
          :height="326"
          :footer-props="envs.FOOTER_PROPS_100"
        >
          <template v-slot:top>
            <button-set
              add-button
              delete-button
              reload-button
              :delete-disabled="!selected || selected.length === 0"
              @click:add="
                () => {
                  mode = '추가';
                  editItem = Object.create(null);
                  dialog = true;
                }
              "
              @click:delete="
                () => {
                  editItem = selected[0];
                  $refs.refEditDialog.delete();
                }
              "
              @click:reload="getList"
            />
          </template>
          <template v-slot:header>
            <data-table-filter
              :filter-header="headers"
              :filtered-items.sync="filteredItems"
              :original-items="items"
            />
          </template>
          <template v-slot:item.codeGroup="{ item }">
            <a
              :style="{ 'font-weight': 'bold' }"
              @click="
                () => {
                  mode = '수정';
                  editItem = Object.assign({}, item);
                  dialog = true;
                }
              "
            >
              {{ item.codeGroup }}
            </a>
          </template>
          <template v-slot:item.updated="{ item }">
            {{ item.updated | formatDatetime }}
          </template>
          <template v-slot:item.updatedBy="{ item }">
            {{ item.updatedBy | formatEmpNm }}
          </template>
        </v-data-table>
        <code-group-edit-dialog
          ref="refEditDialog"
          :edit-item="editItem"
          :dialog.sync="dialog"
          :mode="mode"
          @finished="getList"
        />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Vue, Watch } from 'vue-property-decorator';
import { DataTableHeader, TableCodeGroupVO } from '@/common/types';
import { getListApi } from '@/utils/apis';
import envs from '@/constants/envs';
import dayjs from 'dayjs';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import CodeGroupEditDialog from '@/views/admin/code/components/CodeGroupEditDialog.vue';
import DataTableFilter from '@/components/datatable/DataTableFilter.vue';

@Component({
  name: 'CodeGroupList',
  components: {
    DataTableFilter,
    ButtonSet,
    CodeGroupEditDialog,
  },
})
export default class extends Vue {
  readonly dayjs: typeof dayjs = dayjs;
  readonly envs: typeof envs = envs;
  mode: string | null = null;
  sortBy: string[] = ['codeGroup'];
  sortDesc: boolean[] = [false];
  items: TableCodeGroupVO[] = [];
  filteredItems: TableCodeGroupVO[] = [];
  editItem: TableCodeGroupVO = Object.create(null);
  selected: TableCodeGroupVO[] = [];
  dialog: boolean = false;
  loading: boolean = false;

  headers: DataTableHeader[] = [
    {
      text: `그룹코드`,
      align: `start`,
      value: `codeGroup`,
    },
    {
      text: `그룹코드명`,
      align: `start`,
      value: `name`,
    },
    {
      text: `작업 일시`,
      align: `center`,
      value: `updated`,
      filterable: false,
      width: '11rem',
    },
    {
      text: `작업자`,
      align: `start`,
      value: `updatedBy`,
      filterable: false,
      width: '8rem',
    },
  ];

  mounted() {
    this.getList();
  }

  @Watch('selected')
  @Emit('select')
  watchSelected(val: TableCodeGroupVO[]) {
    return val;
  }

  @Emit('updated')
  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getListApi<TableCodeGroupVO[]>(`admin/codeGroups/`);
    this.loading = false;
    this.items = response.data || [];
  }
}
</script>
