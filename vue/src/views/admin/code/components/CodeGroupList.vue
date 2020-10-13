<template>
  <div>
    <v-card>
      <v-card-text class="py-1">
        <v-alert icon="mdi-format-list-checkbox" dense tile class="mb-0">
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
          :height="height"
          :footer-props="envs.FOOTER_PROPS_MAX_1000"
        >
          <template v-slot:top>
            <button-set
              add-button
              delete-button
              reload-button
              :delete-disabled="!selected || selected.length === 0"
              @click:add="
                () => {
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
              :header="headers"
              :output.sync="filteredItems"
              :input="items"
            />
          </template>
          <template v-slot:[`item.codeGroup`]="{ item }">
            <a
              :style="{ 'font-weight': 'bold' }"
              @click="
                () => {
                  editItem = Object.assign({}, item);
                  dialog = true;
                }
              "
            >
              {{ item.codeGroup }}
            </a>
          </template>
          <template v-slot:[`item.updated`]="{ item }">
            {{ item.updated | formatDatetime }}
          </template>
          <template v-slot:[`item.updatedBy`]="{ item }">
            {{ item.updatedBy | formatMemberNm }}
          </template>
        </v-data-table>
        <code-group-edit-dialog
          ref="refEditDialog"
          :edit-item="editItem"
          :dialog.sync="dialog"
          @finished="getList"
        />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Prop, Vue, Watch } from 'vue-property-decorator';
import { DataTableHeader, TableCodeGroupEntity } from '@/common/types';
import { getApi } from '@/utils/apis';
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
  @Prop({ required: true }) readonly height!: number | string;
  readonly dayjs: typeof dayjs = dayjs;
  readonly envs: typeof envs = envs;
  sortBy: string[] = ['codeGroup'];
  sortDesc: boolean[] = [false];
  items: TableCodeGroupEntity[] = [];
  filteredItems: TableCodeGroupEntity[] = [];
  editItem: TableCodeGroupEntity = Object.create(null);
  selected: TableCodeGroupEntity[] = [];
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
      width: '10rem',
    },
    {
      text: `작업자`,
      align: `start`,
      value: `updatedBy`,
      filterable: false,
      width: '7rem',
    },
  ];

  mounted() {
    this.getList();
  }

  @Watch('selected')
  @Emit('select')
  watchSelected(val: TableCodeGroupEntity[]) {
    return val;
  }

  @Emit('updated')
  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableCodeGroupEntity[]>(`admin/codeGroups/`);
    this.loading = false;
    this.items = response?.data || [];
  }
}
</script>
