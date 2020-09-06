<template>
  <div id="grid-wrapper">
    <v-card-text class="pb-1">
      <button-set
        add-button
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
        delete-button
        :delete-disabled="!selected || selected.length < 1"
        @click:delete="deleteItem"
        reload-button
        @click:reload="getList"
      />
      <ag-grid-vue
        :gridOptions="gridOptions"
        :style="`width: 100%; height: ${height}px;`"
        class="ag-theme-alpine"
        :columnDefs="columnDefs"
        :rowData="items"
        @selection-changed="
          (event) => {
            selected = event.api.getSelectedRows();
          }
        "
        @pagination-changed="paginationChanged"
        @sort-changed="sortChanged"
      >
      </ag-grid-vue>
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
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import DataTableFilter from '@/components/datatable/DataTableFilter.vue';
import MemberEditDialog from '@/views/admin/member/components/MemberEditDialog.vue';
import dayjs from 'dayjs';
import { onGridSizeChanged } from '@/utils/ag-grid-vue';
import {
  CellClickedEvent,
  ColDef,
  GridOptions,
  PaginationChangedEvent,
  SortChangedEvent,
  ValueFormatterParams,
  ValueGetterParams,
} from 'ag-grid-community';
import FloatingFilterAuthority from '@/views/aggrid/filters/FloatingFilterAuthority.vue';
import FloatingFilterSwitch from '@/views/aggrid/filters/FloatingFilterSwitch.vue';
import RenderColumnCheckbox from '@/views/aggrid/renders/RenderColumnCheckbox.vue';
import { getText } from '@/utils/codes';
import { formatDatetime, getMemberNm } from '@/utils/formatters';

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
  readonly dayjs: typeof dayjs = dayjs;
  readonly ENDPOINT_URL: string = 'admin/members/';
  mode: string | null = null;
  items: TableMemberEntity[] = [];
  editItem: TableMemberEntity = Object.create(null);
  selected: TableMemberEntity[] = [];
  loading: boolean = false;
  AUTHORITY: SelectItem[] | null = null;
  gridOptions: GridOptions = {
    defaultColDef: {
      minWidth: 70,
      filter: true,
      sortable: true,
      resizable: true,
      suppressMenu: true,
      floatingFilter: true,
      floatingFilterComponentParams: {
        suppressFilterButton: true,
      },
    },
    pagination: true,
    paginationPageSize: 1,
    suppressCellSelection: true,
    animateRows: true,
    rowSelection: 'single',
    suppressMultiSort: true,
    frameworkComponents: {
      FloatingFilterAuthority: FloatingFilterAuthority,
      FloatingFilterSwitch: FloatingFilterSwitch,
      RenderColumnCheckbox: RenderColumnCheckbox,
    },
    onGridSizeChanged: onGridSizeChanged,
  };

  columnDefs: ColDef[] = [];

  beforeMount() {
    this.columnDefs = [
      {
        headerName: '사용자아이디',
        field: 'id',
        minWidth: 150,
        cellClass: ['text-link'],
        onCellClicked: (params: CellClickedEvent) => {
          this.mode = '수정';
          this.editItem = {
            ...params.data,
            password: undefined,
          };
          this.$modal.show('MemberEditDialog');
        },
        checkboxSelection: (params: any) => {
          return params.columnApi.getRowGroupColumns().length === 0;
        },
      },
      {
        headerName: '사용자명',
        field: 'name',
      },
      {
        headerName: '권한',
        minWidth: 130,
        field: 'authority',
        floatingFilterComponent: 'FloatingFilterAuthority',
        floatingFilterComponentParams: {
          suppressFilterButton: true,
        },
        valueFormatter: (params: ValueFormatterParams) => {
          return getText(this.AUTHORITY, params.value);
        },
      },
      {
        headerName: '만료일',
        field: 'expired',
        minWidth: 150,
        cellClass: ['text-center'],
        valueGetter: (params: ValueGetterParams) => {
          return formatDatetime(params.data.expired);
        },
      },
      {
        headerName: '사용 가능',
        field: 'available',
        minWidth: 70,
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
        valueFormatter: (params: ValueFormatterParams) => {
          return params.value.toLocaleString();
        },
      },
      {
        headerName: '작업 일시',
        field: 'updated',
        minWidth: 150,
        cellClass: ['text-center'],
        sort: 'desc',
        valueGetter: (params: ValueGetterParams) => {
          return formatDatetime(params.data.expired);
        },
      },
      {
        headerName: '작업자',
        field: 'updatedBy',
        minWidth: 130,
        type: [],
        valueFormatter: (params: ValueFormatterParams) => {
          return getMemberNm(params.value);
        },
      },
    ];
  }

  async mounted() {
    this.AUTHORITY = await getCodesApi('AUTHORITY');
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

  deleteItem() {
    this.editItem = this.selected[0];
    (this.$refs.refEditDialog as any).delete();
  }

  paginationChanged(params: PaginationChangedEvent) {
    console.log(params);
    console.log(params.api.paginationGetCurrentPage());
    console.log(params.api.paginationGetPageSize());
  }

  sortChanged(params: SortChangedEvent) {
    console.dir(params);
    console.log(params.api.getSortModel()[0].colId);
    console.log(params.api.getSortModel()[0].sort);
  }
}
</script>
