<template>
  <div id="grid-wrapper">
    <v-card-text class="pb-1">
      <button-set
        add-button
        delete-button
        reload-button
        @click:add="
          () => {
            mode = '추가';
            editItem = {
              expired: dayjs().add(1, 'year').toDate(),
              timeout: 7200,
            };
            $modal.show('MemberEditDialog');
          }
        "
        @click:delete="
          () => {
            editItem = selected[0];
          }
        "
        @click:reload="getList"
      />
      <ag-grid-vue
        :gridOptions="gridOptions"
        :style="`width: 100%; height: ${height}px;`"
        class="ag-theme-alpine"
        :suppressAutoSize="true"
        :defaultColDef="{
          filter: true,
          sortable: false,
          resizable: true,
          suppressMenu: true,
          floatingFilter: true,
          floatingFilterComponentParams: {
            suppressFilterButton: true,
          },
        }"
        :columnDefs="columnDefs"
        :rowData="items"
        :pagination="true"
        :paginationPageSize="100"
        :frameworkComponents="{
          RenderColumnCheckbox: RenderColumnCheckbox,
          FloatingFilterAuthority: FloatingFilterAuthority,
          FloatingFilterSwitch: FloatingFilterSwitch,
        }"
        :suppressCellSelection="true"
        rowSelection="single"
        @grid-size-changed="selected = gridOptions.api.getSelectedRows()"
        @row-double-clicked="
          () => {
            mode = '수정';
            editItem = {
              ...gridOptions.api.getSelectedRows()[0],
              password: undefined,
            };
            $modal.show('MemberEditDialog');
          }
        "
      >
      </ag-grid-vue>
      <!--      <v-data-table-->
      <!--        must-sort-->
      <!--        fixed-header-->
      <!--        v-model="selected"-->
      <!--        :loading="loading"-->
      <!--        :headers="headers"-->
      <!--        :items="filteredItems"-->
      <!--        :sort-by="sortBy"-->
      <!--        :sort-desc="sortDesc"-->
      <!--        item-key="id"-->
      <!--        single-select-->
      <!--        show-select-->
      <!--        dense-->
      <!--        :height="height"-->
      <!--        :footer-props="envs.FOOTER_PROPS_100"-->
      <!--      >-->
      <!--        <template v-slot:top>-->
      <!--        </template>-->
      <!--        <template v-slot:header>-->
      <!--          <data-table-filter-->
      <!--            :filter-header="headers"-->
      <!--            :filtered-items.sync="filteredItems"-->
      <!--            :original-items="items"-->
      <!--          />-->
      <!--        </template>-->
      <!--        <template v-slot:item.id="{ item }">-->
      <!--          <a-->
      <!--            :style="{ 'font-weight': 'bold' }"-->
      <!--                  @click="-->
      <!--                    () => {-->
      <!--                      mode = '수정';-->
      <!--                      editItem = { ...item, password: undefined };-->
      <!--                      $modal.show('MemberEditDialog');-->
      <!--                    }-->
      <!--            "-->
      <!--          >-->
      <!--            {{ item.id }}-->
      <!--          </a>-->
      <!--        </template>-->
      <!--        <template v-slot:item.available="{ item }">-->
      <!--          <span style="display: inline-flex">-->
      <!--            <v-checkbox-->
      <!--              readonly-->
      <!--              :input-value="item.available"-->
      <!--              :ripple="false"-->
      <!--              dense-->
      <!--              hide-details-->
      <!--              class="mt-0"-->
      <!--            />-->
      <!--          </span>-->
      <!--        </template>-->
      <!--        <template v-slot:item.authority="{ item }" v-if="AUTHORITY">-->
      <!--          {{ item.authority | getCodeText(AUTHORITY) }}-->
      <!--        </template>-->
      <!--        <template v-slot:item.timeout="{ item }">-->
      <!--          {{ item.timeout.toLocaleString() }}-->
      <!--        </template>-->
      <!--        <template v-slot:item.expired="{ item }">-->
      <!--          {{ item.expired | formatDatetime }}-->
      <!--        </template>-->
      <!--        <template v-slot:item.updated="{ item }">-->
      <!--          {{ item.updated | formatDatetime }}-->
      <!--        </template>-->
      <!--        <template v-slot:item.updatedBy="{ item }">-->
      <!--          {{ item.updatedBy | formatMemberNm }}-->
      <!--        </template>-->
      <!--      </v-data-table>-->
      <member-edit-dialog
        ref="refEditDialog"
        :edit-item="editItem"
        :mode="mode"
        @finished="getList"
      />
    </v-card-text>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { SelectItem, TableMemberEntity } from '@/common/types';
import { getApi, getCodesApi } from '@/utils/apis';
import envs from '@/constants/envs';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import DataTableFilter from '@/components/datatable/DataTableFilter.vue';
import MemberEditDialog from '@/views/admin/member/components/MemberEditDialog.vue';
import dayjs from 'dayjs';
import { onGridSizeChanged } from '@/utils/ag-grid-vue';
import { GridOptions, ValueGetterParams } from 'ag-grid-community';
import { getText } from '@/utils/codes';
import { formatDatetime, getMemberNm } from '@/utils/formatters';
import FloatingFilterAuthority from '@/views/aggrid/filters/FloatingFilterAuthority.vue';
import FloatingFilterSwitch from '@/views/aggrid/filters/FloatingFilterSwitch.vue';
import RenderColumnCheckbox from '@/views/aggrid/renders/RenderColumnCheckbox.vue';

@Component({
  name: 'MemberList',
  components: {
    MemberEditDialog,
    DataTableFilter,
    ButtonSet,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number;
  readonly onGridSizeChanged: typeof onGridSizeChanged = onGridSizeChanged;
  readonly RenderColumnCheckbox: typeof RenderColumnCheckbox = RenderColumnCheckbox;
  readonly FloatingFilterAuthority: typeof FloatingFilterAuthority = FloatingFilterAuthority;
  readonly FloatingFilterSwitch: typeof FloatingFilterSwitch = FloatingFilterSwitch;
  readonly envs: typeof envs = envs;
  readonly dayjs: typeof dayjs = dayjs;
  readonly ENDPOINT_URL: string = 'admin/members/';
  mode: string | null = null;
  sortBy: string[] = ['authority'];
  sortDesc: boolean[] = [true];
  items: TableMemberEntity[] = [];
  editItem: TableMemberEntity = Object.create(null);
  selected: TableMemberEntity[] = [];
  loading: boolean = false;
  AUTHORITY: SelectItem[] | null = null;
  gridOptions: GridOptions = {};

  columnDefs = [];

  // headers: DataTableHeader[] = [
  //   {
  //     text: `사용자아이디`,
  //     align: `start`,
  //     value: `id`,
  //   },
  //   {
  //     text: `사용자명`,
  //     align: `start`,
  //     value: `name`,
  //   },
  //   {
  //     text: `권한`,
  //     align: `center`,
  //     value: `authority`,
  //     filterType: 'select',
  //     filterSelectItem: [],
  //   },
  //   {
  //     text: `만료일`,
  //     align: `center`,
  //     value: `expired`,
  //     width: '11rem',
  //   },
  //   {
  //     text: `사용 가능`,
  //     align: `center`,
  //     value: `available`,
  //     filterType: 'switch',
  //     width: '7rem',
  //   },
  //   {
  //     text: `자동로그아웃시간(초)`,
  //     align: `end`,
  //     value: `timeout`,
  //     width: '11rem',
  //   },
  //   {
  //     text: `작업 일시`,
  //     align: `center`,
  //     value: `updated`,
  //     filterable: false,
  //     width: '11rem',
  //   },
  //   {
  //     text: `작업자`,
  //     align: `start`,
  //     value: `updatedBy`,
  //     filterable: false,
  //     width: '8rem',
  //   },
  // ];

  async mounted() {
    this.AUTHORITY = await getCodesApi('AUTHORITY');
    this.columnDefs = [
      {
        headerName: '사용자아이디',
        field: 'id',
        minWidth: 150,
        maxWidth: 150,
      },
      {
        headerName: '사용자명',
        field: 'name',
      },
      {
        headerName: '권한',
        minWidth: 110,
        maxWidth: 110,
        field: 'authority',
        floatingFilterComponent: 'FloatingFilterAuthority',
        floatingFilterComponentParams: {
          suppressFilterButton: true,
        },
        valueFormatter: (params: ValueGetterParams) => {
          return getText(this.AUTHORITY, params.value);
        },
      },
      {
        headerName: '만료일',
        field: 'expired',
        minWidth: 180,
        maxWidth: 180,
        cellClass: ['text-center'],
        valueGetter: (params: ValueGetterParams) => {
          return formatDatetime(params.data.expired);
        },
      },
      {
        headerName: '사용 가능',
        field: 'available',
        minWidth: 120,
        maxWidth: 120,
        floatingFilterComponent: 'FloatingFilterSwitch',
        floatingFilterComponentParams: {
          suppressFilterButton: true,
        },
        cellRendererFramework: RenderColumnCheckbox,
      },
      {
        headerName: '자동로그아웃시간(초)',
        field: 'timeout',
        cellClass: ['text-right'],
        sortable: false,
        filter: false,
      },
      {
        headerName: '작업 일시',
        field: 'updated',
        minWidth: 180,
        maxWidth: 180,
        cellClass: ['text-center'],
        valueGetter: (params: ValueGetterParams) => {
          return formatDatetime(params.data.expired);
        },
      },
      {
        headerName: '작업자',
        field: 'updatedBy',
        minWidth: 150,
        maxWidth: 150,
        type: [],
        valueFormatter: (params: ValueGetterParams) => {
          return getMemberNm(params.value);
        },
      },
    ];
    await this.getList();
  }

  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<TableMemberEntity[]>(this.ENDPOINT_URL);
    this.loading = false;
    this.items = response.data || [];
  }
}
</script>
