<template>
  <div id="grid-wrapper">
    <v-card-text class="pb-1">
      <ag-grid-vue
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
          FloatingFilterMenuType: FloatingFilterMenuType,
        }"
        @grid-size-changed="onGridSizeChanged"
      >
      </ag-grid-vue>
    </v-card-text>
    <menu-edit-dialog
      ref="refEditDialog"
      :edit-item="editItem"
      :mode="mode"
      @finished="getList"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { DataTableHeader, SelectItem, TableMenuEntity } from '@/common/types';
import { getApi, getCodesApi } from '@/utils/apis';
import envs from '@/constants/envs';
import MenuEditDialog from '@/views/admin/menu/components/MenuEditDialog.vue';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import { getText } from '@/utils/codes';
import dayjs from 'dayjs';
import FloatingFilterMenuType from '@/views/aggrid/filters/FloatingFilterMenuType.vue';
import { GridOptionsWrapper, ValueGetterParams } from 'ag-grid-community';
import RenderColumnAction from '@/views/admin/menu/renders/RenderColumnAction.vue';
import { onGridSizeChanged } from '@/utils/ag-grid-vue';
import { formatDatetime, getMemberNm } from '@/utils/formatters';

interface MenuVO extends TableMenuEntity {
  level: number;
}

@Component({
  name: 'MenuList',
  components: {
    ButtonSet,
    MenuEditDialog,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number;
  readonly FloatingFilterMenuType: typeof FloatingFilterMenuType = FloatingFilterMenuType;
  readonly onGridSizeChanged: typeof onGridSizeChanged = onGridSizeChanged;
  readonly envs: typeof envs = envs;
  readonly ENDPOINT_URL: string = 'admin/menus/';
  mode: string | null = null;
  items: TableMenuEntity[] = [];
  editItem: TableMenuEntity = Object.create(null);
  loading: boolean = false;

  MENU_TYPE: SelectItem[] | null = null;

  columnDefs = [];

  async mounted() {
    this.MENU_TYPE = await getCodesApi(`MENU_TYPE`);
    this.columnDefs = [
      {
        headerName: '타입',
        field: 'type',
        minWidth: 120,
        maxWidth: 120,
        cellClass: ['text-center'],
        floatingFilterComponent: 'FloatingFilterMenuType',
        floatingFilterComponentParams: {
          suppressFilterButton: true,
        },
        valueFormatter: (params: ValueGetterParams) => {
          return getText(this.MENU_TYPE, params.value);
        },
      },
      {
        headerName: '메뉴명',
        field: 'name',
      },
      {
        headerName: '메뉴 순서',
        minWidth: 110,
        maxWidth: 110,
        field: 'displayOrder',
        type: ['rightAligned', 'numericColumn'],
      },
      {
        headerName: 'Action',
        field: 'action',
        minWidth: 270,
        maxWidth: 270,
        sortable: false,
        filter: false,
        cellClass: ['text-center'],
        cellRendererFramework: RenderColumnAction,
      },
      {
        headerName: '작업 일시',
        field: 'updated',
        minWidth: 180,
        maxWidth: 180,
        filter: false,
        cellClass: ['text-center'],
        valueFormatter: (params: ValueGetterParams) => {
          return formatDatetime(params.value);
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
    this.items = [];
    this.loading = true;
    const response = await getApi<MenuVO[]>(this.ENDPOINT_URL);
    this.loading = false;
    this.items = response.data || [];
  }
}
</script>
