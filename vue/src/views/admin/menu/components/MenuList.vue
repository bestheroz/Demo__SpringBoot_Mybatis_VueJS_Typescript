<template>
  <div>
    <v-card-text class="pb-1">
      <v-data-table
        must-sort
        fixed-header
        :loading="loading"
        :headers="headers"
        :items="filteredItems"
        item-key="id"
        disable-sort
        disable-filtering
        disable-pagination
        dense
        :height="773"
      >
        <template v-slot:top>
          <button-set reload-button @click:reload="getList" />
        </template>
        <template v-slot:header>
          <data-table-filter
            :filter-header="headers"
            :filtered-items.sync="filteredItems"
            :original-items="items"
            :filter-first-column="true"
          />
        </template>
        <template v-slot:item.type="{ item }" v-if="W004">
          {{ item.type | getCodeText(W004) }}
        </template>
        <template v-slot:item.menuNmKor="{ item }">
          <span :style="`padding-left: ${50 * (item.lvl - 1)}px;`">
            {{ item.menuNmKor }}
          </span>
        </template>
        <template v-slot:item.seq="{ item }">
          {{ item.seq.toLocaleString() }}
        </template>
        <template v-slot:item.updDt="{ item }">
          {{ item.updDt | formatDatetime }}
        </template>
        <template v-slot:item.action="{ item }">
          <v-btn
            class="mx-1"
            tile
            color="button-add"
            x-small
            @click="
              () => {
                mode = '추가';
                editItem = {
                  pMenuId: item.id,
                  id: `${item.id}_00`,
                };
                dialog = true;
              }
            "
            :disabled="item.lvl === 3"
          >
            하위메뉴입력
          </v-btn>
          <v-btn
            class="mx-1"
            tile
            color="button-edit"
            x-small
            :disabled="item.id === 'MENUID_0000'"
            @click="
              () => {
                mode = '수정';
                editItem = Object.assign(Object.create(null), item);
                dialog = true;
              }
            "
          >
            수정
          </v-btn>
          <v-btn
            class="mx-1"
            tile
            color="button-delete"
            x-small
            @click="
              () => {
                editItem = item;
                $refs.refEditDialog.delete();
              }
            "
            :disabled="!item.pMenuId"
          >
            삭제
          </v-btn>
        </template>
      </v-data-table>
      <menu-edit-dialog
        ref="refEditDialog"
        :edit-item="editItem"
        :dialog.sync="dialog"
        :mode="mode"
        @finished="getList"
      />
    </v-card-text>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {
  DataTableHeader,
  SelectItem,
  TableSampleMenuMstVO,
} from '@/common/types';
import { getCodeListApi, getListApi } from '@/utils/apis';
import envs from '@/constants/envs';
import MenuEditDialog from '@/views/admin/menu/components/MenuEditDialog.vue';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import DataTableFilter from '@/components/datatable/DataTableFilter.vue';

@Component({
  name: 'MenuList',
  components: {
    DataTableFilter,
    ButtonSet,
    MenuEditDialog,
  },
})
export default class extends Vue {
  readonly envs: typeof envs = envs;
  mode: string | null = null;
  items: TableSampleMenuMstVO[] = [];
  filteredItems: TableSampleMenuMstVO[] = [];
  editItem: TableSampleMenuMstVO = Object.create(null);
  selected: TableSampleMenuMstVO[] = [];
  loading: boolean = false;
  dialog: boolean = false;

  W004: SelectItem[] | null = null;

  headers: DataTableHeader[] = [
    {
      text: `타입`,
      align: `center`,
      value: `type`,
      filterType: 'select',
      filterSelectItem: [],
    },
    {
      text: `메뉴 ID`,
      align: `start`,
      value: `id`,
    },
    {
      text: `상위 메뉴 ID`,
      align: `start`,
      value: `pMenuId`,
    },
    {
      text: `메뉴명`,
      align: `start`,
      value: `menuNmKor`,
    },
    {
      text: `메뉴 순서`,
      align: `end`,
      value: `seq`,
      width: 100,
    },
    {
      text: `Action`,
      align: `center`,
      value: `action`,
      filterable: false,
      width: 220,
    },
  ];

  async mounted() {
    this.W004 = await getCodeListApi(`W004`);
    this.headers[0].filterSelectItem = this.W004;
    this.getList();
  }

  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getListApi<TableSampleMenuMstVO[]>(`admin/menu/`);
    this.loading = false;
    this.items = response.data || [];
  }
}
</script>
