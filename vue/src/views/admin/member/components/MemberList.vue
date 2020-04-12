<template>
  <div>
    <v-card-text class="pb-1">
      <v-data-table
        must-sort
        fixed-header
        v-model="selected"
        :loading="loading"
        :headers="headers"
        :items="filteredItems"
        :sort-by="sortBy"
        :sort-desc="sortDesc"
        item-key="id"
        single-select
        show-select
        dense
        :height="773"
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
                editItem = {};
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
        <template v-slot:item.id="{ item }">
          <a
            :style="{ 'font-weight': 'bold' }"
            @click="
              () => {
                mode = '수정';
                editItem = Object.assign({}, item);
                editItem.password = undefined;
                dialog = true;
              }
            "
          >
            {{ item.id }}
          </a>
        </template>
        <template v-slot:item.userGroupId="{ item }" v-if="GROUP_LIST">
          {{ item.userGroupId | getCodeText(GROUP_LIST) }}
        </template>
        <template v-slot:item.updDt="{ item }">
          {{ item.updDt | formatDatetime }}
        </template>
        <template v-slot:item.updId="{ item }">
          {{ item.updId | formatEmpNm }}
        </template>
      </v-data-table>
      <member-edit-dialog
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
  TableSampleMemberMstVO,
} from '@/common/types';
import { getListApi } from '@/utils/apis';
import envs from '@/constants/envs';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import DataTableFilter from '@/components/datatable/DataTableFilter.vue';
import MemberEditDialog from '@/views/admin/member/components/MemberEditDialog.vue';

@Component({
  name: 'MemberList',
  components: {
    MemberEditDialog,
    DataTableFilter,
    ButtonSet,
  },
})
export default class extends Vue {
  readonly envs: typeof envs = envs;
  mode: string | null = null;
  sortBy: string[] = ['updDt'];
  sortDesc: boolean[] = [true];
  items: TableSampleMemberMstVO[] = [];
  filteredItems: TableSampleMemberMstVO[] = [];
  editItem: TableSampleMemberMstVO = {};
  selected: TableSampleMemberMstVO[] = [];
  loading: boolean = false;
  dialog: boolean = false;

  GROUP_LIST: SelectItem[] | null = null;

  headers: DataTableHeader[] = [
    {
      text: `사용자아이디`,
      align: `start`,
      value: `id`,
    },
    {
      text: `사용자명`,
      align: `start`,
      value: `empnm`,
    },
    {
      text: `사용자그룹`,
      align: `center`,
      value: `userGroupId`,
      filterType: 'select',
      filterSelectItem: [],
    },
    {
      text: `이메일`,
      align: `start`,
      value: `email`,
    },
    {
      text: `연락처`,
      align: `start`,
      value: `smsphone`,
    },
    {
      text: `세션타임아웃시간(초)`,
      align: `end`,
      value: `timeout`,
      width: 170,
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
    this.getList();
  }

  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getListApi<TableSampleMemberMstVO[]>(`admin/user/`);
    this.loading = false;
    this.items = response.data || [];
  }

  async getCodeList() {
    this.loading = true;
    const response = await getListApi<SelectItem[]>(`admin/user/groupList`);
    this.loading = false;
    this.headers[2].filterSelectItem = this.GROUP_LIST = response.data || [];
  }
}
</script>
