<template>
  <div>
    <v-card>
      <v-alert
        border="bottom"
        colored-border
        color="success"
        icon="mdi-format-list-checkbox"
        dense
        class="mb-0"
      >
        코드 관리 - Detail
      </v-alert>
      <v-card-text class="py-1">
        <v-data-table
          must-sort
          fixed-header
          v-model="selected"
          :loading="loading"
          :headers="headers"
          :items="filteredItems"
          :sort-by="sortBy"
          :sort-desc="sortDesc"
          item-key="code"
          single-select
          show-select
          dense
          :height="327"
          :footer-props="envs.FOOTER_PROPS_100"
        >
          <template v-slot:top>
            <button-set
              :disabled="!parentItem.groupCode"
              add-button
              delete-button
              reload-button
              :delete-disabled="!selected || selected.length === 0"
              @click:add="
                () => {
                  mode = '추가';
                  editItem = { groupCode: parentItem.groupCode };
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
          <template v-slot:item.code="{ item }">
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
              {{ item.code }}
            </a>
          </template>
          <template v-slot:item.isUsing="{ item }">
            <span style="display: inline-flex;">
              <v-checkbox
                readonly
                :input-value="item.isUsing"
                true-value="Y"
                false-value="N"
                :ripple="false"
                dense
                hide-details
                class="mt-0"
              />
            </span>
          </template>
          <template v-slot:item.levelcod="{ item }" v-if="LEVELCOD">
            {{ item.levelcod | getCodeText(LEVELCOD) }}
          </template>
          <template v-slot:item.updId="{ item }">
            {{ item.updId | formatEmpNm }}
          </template>
          <template v-slot:item.updDt="{ item }">
            {{ item.updDt | formatDatetime }}
          </template>
        </v-data-table>
        <code-det-edit-dialog
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
import { Component, Prop, Vue, Watch } from 'vue-property-decorator';
import {
  DataTableHeader,
  SelectItem,
  TableSampleCodeMstVO,
  TableSampleCodeDetVO,
} from '@/common/types';
import { getListApi } from '@/utils/apis';
import envs from '@/constants/envs';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import CodeDetEditDialog from '@/views/admin/code/components/CodeDetEditDialog.vue';
import DataTableFilter from '@/components/datatable/DataTableFilter.vue';

@Component({
  name: 'CodeDetList',
  components: {
    DataTableFilter,
    CodeDetEditDialog,
    ButtonSet,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly parentItem!: TableSampleCodeMstVO;

  readonly envs: typeof envs = envs;

  LEVELCOD: SelectItem[] | null = null;
  mode: string | null = null;

  loading: boolean = false;
  sortBy: string[] = ['sortseq'];
  sortDesc: boolean[] = [false];
  items: TableSampleCodeDetVO[] = [];
  filteredItems: TableSampleCodeDetVO[] = [];
  editItem: TableSampleCodeDetVO = {};
  selected: TableSampleCodeDetVO[] = [];
  dialog: boolean = false;
  headers: DataTableHeader[] = [
    {
      text: `상세코드`,
      align: `start`,
      value: `code`,
    },
    {
      text: `상세코드명`,
      align: `start`,
      value: `codenm`,
    },
    {
      text: `사용여부`,
      align: `center`,
      value: `isUsing`,
      filterType: 'switch',
      width: 100,
    },
    {
      text: `출력순서`,
      align: `end`,
      value: `sortseq`,
      width: 100,
    },
    {
      text: `권한`,
      align: `start`,
      value: `levelcod`,
      filterType: 'select',
      filterSelectItem: [],
    },
    {
      text: `작업일시`,
      align: `center`,
      value: `updDt`,
      filterable: false,
      width: 160,
    },
    {
      text: `작업자`,
      align: `start`,
      value: `updId`,
      filterable: false,
      width: 100,
    },
  ];

  mounted() {
    this.getCodeList();
  }

  @Watch('parentItem')
  watchParentItem(val: TableSampleCodeMstVO): void {
    this.items = [];
    if (val && val.groupCode) {
      this.getList();
    }
  }

  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getListApi<TableSampleCodeDetVO[]>(
      `admin/codeDet/${this.parentItem.groupCode}`,
    );
    this.loading = false;
    this.items = response.data || [];
  }

  async getCodeList() {
    this.loading = true;
    const response = await getListApi<SelectItem[]>(`admin/codeDet/levelList`);
    this.loading = false;
    this.headers[4].filterSelectItem = this.LEVELCOD = response.data || [];
  }
}
</script>
