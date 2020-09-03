<template>
  <div id="grid-wrapper">
    <v-card-text class="pb-1">
      <ag-grid-vue
        :style="`width: 100%; height: ${height}px;`"
        class="ag-theme-alpine"
        :suppressAutoSize="true"
        :defaultColDef="{
          filter: true,
          sortable: true,
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
        @grid-size-changed="onGridSizeChanged"
      >
      </ag-grid-vue>
      <!--      <v-data-table-->
      <!--        fixed-header-->
      <!--        :loading="loading"-->
      <!--        :headers="headers"-->
      <!--        :items="items"-->
      <!--        item-key="id"-->
      <!--        disable-sort-->
      <!--        disable-filtering-->
      <!--        disable-pagination-->
      <!--        dense-->
      <!--        :height="height"-->
      <!--      >-->
      <!--        <template v-slot:top>-->
      <!--          <button-set reload-button @click:reload="getList" />-->
      <!--        </template>-->
      <!--        <template v-slot:item.type="{ item }" v-if="MENU_TYPE">-->
      <!--          {{ item.type | getCodeText(MENU_TYPE) }}-->
      <!--        </template>-->
      <!--        <template v-slot:item.name="{ item }">-->
      <!--          <span :style="`padding-left: ${80 * (item.level - 1)}px;`">-->
      <!--            <v-icon v-if="item.icon"> {{ item.icon }} </v-icon>-->
      <!--            {{ item.name }}-->
      <!--          </span>-->
      <!--        </template>-->
      <!--        <template v-slot:item.updated="{ item }">-->
      <!--          {{ item.updated | formatDatetime }}-->
      <!--        </template>-->
      <!--        <template v-slot:item.updatedBy="{ item }">-->
      <!--          {{ item.updatedBy | formatMemberNm }}-->
      <!--        </template>-->
      <!--        <template v-slot:item.action="{ item }">-->
      <!--          <v-btn-->
      <!--            class="mx-1"-->
      <!--            tile-->
      <!--            color="button-add"-->
      <!--            small-->
      <!--            @click="-->
      <!--              () => {-->
      <!--                mode = '추가';-->
      <!--                editItem = {-->
      <!--                  parentId: item.id,-->
      <!--                };-->
      <!--                $modal.show('MenuEditDialog');-->
      <!--              }-->
      <!--            "-->
      <!--            :disabled="item.level === 3"-->
      <!--          >-->
      <!--            하위메뉴입력-->
      <!--          </v-btn>-->
      <!--          <v-btn-->
      <!--            class="mx-1"-->
      <!--            tile-->
      <!--            color="button-edit"-->
      <!--            small-->
      <!--            :disabled="item.name === '///'"-->
      <!--            @click="-->
      <!--              () => {-->
      <!--                mode = '수정';-->
      <!--                editItem = Object.assign(Object.create(null), item);-->
      <!--                $modal.show('MenuEditDialog');-->
      <!--              }-->
      <!--            "-->
      <!--          >-->
      <!--            수정-->
      <!--          </v-btn>-->
      <!--          <v-btn-->
      <!--            class="mx-1"-->
      <!--            tile-->
      <!--            color="button-delete"-->
      <!--            small-->
      <!--            @click="-->
      <!--              () => {-->
      <!--                editItem = item;-->
      <!--                $refs.refEditDialog.delete();-->
      <!--              }-->
      <!--            "-->
      <!--            :disabled="!item.parentId"-->
      <!--          >-->
      <!--            삭제-->
      <!--          </v-btn>-->
      <!--        </template>-->
      <!--      </v-data-table>-->
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
  readonly envs: typeof envs = envs;
  readonly ENDPOINT_URL: string = 'admin/menus/';
  mode: string | null = null;
  items: TableMenuEntity[] = [];
  editItem: TableMenuEntity = Object.create(null);
  selected: TableMenuEntity[] = [];
  loading: boolean = false;

  MENU_TYPE: SelectItem[] | null = null;

  columnDefs = [];

  headers: DataTableHeader[] = [
    {
      text: `타입`,
      align: `center`,
      value: `type`,
      filterType: 'select',
      filterSelectItem: [],
      width: '7rem',
    },
    {
      text: `메뉴명`,
      align: `start`,
      value: `name`,
    },
    {
      text: `메뉴 순서`,
      align: `end`,
      value: `displayOrder`,
      width: '6rem',
    },
    {
      text: `Action`,
      align: `center`,
      value: `action`,
      filterable: false,
      width: '18rem',
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

  async mounted() {
    this.MENU_TYPE = await getCodesApi(`MENU_TYPE`);
    this.headers[0].filterSelectItem = this.MENU_TYPE;
    this.columnDefs = [
      {
        headerName: '타입',
        field: 'type',
        width: 100,
        // cellRendererFramework: RenderColumnMenuType,
        cellClass: ['text-center'],
        valueGetter: (params) => {
          return getText(this.MENU_TYPE, params.data.type);
        },
      },
      {
        headerName: '메뉴명',
        field: 'name',
        type: [],
        filter: 'agTextColumnFilter',
        getQuickFilterText: (params: any) => {
          return params.value.name;
        },
      },
      {
        headerName: '메뉴 순서',
        width: 110,
        field: 'displayOrder',
        type: ['rightAligned', 'numericColumn'],
        filter: 'agNumberColumnFilter',
      },
      {
        headerName: 'Action',
        field: 'action',
        sortable: false,
        filter: false,
        type: [],
        cellClass: ['text-center'],
        // cellRendererFramework: RenderColumnAction,
      },
      {
        headerName: '작업 일시',
        field: 'updated',
        width: 150,
        filter: false,
        type: ['dateColumn'],
        cellClass: ['text-center'],
        valueGetter: (params) => {
          return dayjs(params.data.updated).format('YYYY-MM-DD HH:mm:ss');
        },
      },
      { headerName: '작업자', field: 'updatedBy', width: 120, type: [] },
    ];
    await this.getList();
  }

  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getApi<MenuVO[]>(this.ENDPOINT_URL);
    this.loading = false;
    this.items = response.data || [];
  }

  onGridSizeChanged(params) {
    var gridWidth = document.getElementById('grid-wrapper').offsetWidth;
    var columnsToShow = [];
    var columnsToHide = [];
    var totalColsWidth = 0;
    var allColumns = params.columnApi.getAllColumns();
    for (var i = 0; i < allColumns.length; i++) {
      var column = allColumns[i];
      totalColsWidth += column.getMinWidth();
      if (totalColsWidth > gridWidth) {
        columnsToHide.push(column.colId);
      } else {
        columnsToShow.push(column.colId);
      }
    }
    params.columnApi.setColumnsVisible(columnsToShow, true);
    params.columnApi.setColumnsVisible(columnsToHide, false);
    params.api.sizeColumnsToFit();
  }

  editItem1(item) {
    console.log('editItem1');
    this.mode = '수정';
    this.editItem = Object.assign(Object.create(null), item);
    this.$modal.show('MenuEditDialog');
  }
}
</script>
