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
        :height="height"
        :footer-props="envs.FOOTER_PROPS_100"
      >
        <template v-slot:top>
          <button-set
            add-button
            @click:add="
              () => {
                editItem = {
                  expired: dayjs().add(1, 'year').toDate(),
                  timeout: 7200,
                };
                dialog = true;
              }
            "
            delete-button
            :delete-disabled="!selected || selected.length === 0"
            @click:delete="
              () => {
                editItem = selected[0];
                $refs.refEditDialog.delete();
              }
            "
            reload-button
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
                editItem = { ...item, password: undefined };
                dialog = true;
              }
            "
          >
            {{ item.id }}
          </a>
        </template>
        <template v-slot:item.available="{ item }">
          <span style="display: inline-flex;">
            <v-checkbox
              readonly
              :input-value="item.available"
              :ripple="false"
              dense
              hide-details
              class="mt-0"
            />
          </span>
        </template>
        <template v-slot:item.authority="{ item }" v-if="AUTHORITY">
          {{ item.authority | getCodeText(AUTHORITY) }}
        </template>
        <template v-slot:item.timeout="{ item }">
          {{ item.timeout.toLocaleString() }}
        </template>
        <template v-slot:item.expired="{ item }">
          {{ item.expired | formatDatetime }}
        </template>
        <template v-slot:item.updated="{ item }">
          {{ item.updated | formatDatetime }}
        </template>
        <template v-slot:item.updatedBy="{ item }">
          {{ item.updatedBy | formatMemberNm }}
        </template>
      </v-data-table>
      <member-edit-dialog
        ref="refEditDialog"
        :edit-item="editItem"
        :dialog.sync="dialog"
        @finished="getList"
      />
    </v-card-text>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { DataTableHeader, SelectItem, TableMemberEntity } from '@/common/types';
import { getApi, getCodesApi } from '@/utils/apis';
import envs from '@/constants/envs';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';
import DataTableFilter from '@/components/datatable/DataTableFilter.vue';
import MemberEditDialog from '@/views/admin/member/components/MemberEditDialog.vue';
import dayjs from 'dayjs';

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
  readonly envs: typeof envs = envs;
  readonly dayjs: typeof dayjs = dayjs;
  readonly ENDPOINT_URL: string = 'admin/members/';
  sortBy: string[] = ['authority'];
  sortDesc: boolean[] = [true];
  items: TableMemberEntity[] = [];
  filteredItems: TableMemberEntity[] = [];
  editItem: TableMemberEntity = Object.create(null);
  selected: TableMemberEntity[] = [];
  loading: boolean = false;
  dialog: boolean = false;

  AUTHORITY: SelectItem[] | null = null;

  headers: DataTableHeader[] = [
    {
      text: `사용자아이디`,
      align: `start`,
      value: `id`,
    },
    {
      text: `사용자명`,
      align: `start`,
      value: `name`,
    },
    {
      text: `권한`,
      align: `center`,
      value: `authority`,
      filterType: 'select',
      filterSelectItem: [],
    },
    {
      text: `만료일`,
      align: `center`,
      value: `expired`,
      width: '11rem',
    },
    {
      text: `사용 가능`,
      align: `center`,
      value: `available`,
      filterType: 'switch',
      width: '7rem',
    },
    {
      text: `자동로그아웃시간(초)`,
      align: `end`,
      value: `timeout`,
      width: '11rem',
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
    this.headers[2].filterSelectItem = this.AUTHORITY = await getCodesApi(
      'AUTHORITY',
    );
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
