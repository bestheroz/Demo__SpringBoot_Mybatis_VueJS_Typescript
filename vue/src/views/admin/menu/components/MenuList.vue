<template>
  <div>
    <v-card-text class="pb-1">
      <v-data-table
        fixed-header
        :loading="loading"
        :headers="headers"
        :items="items"
        item-key="id"
        disable-sort
        disable-filtering
        disable-pagination
        dense
        hide-default-footer
        :height="height"
      >
        <template v-slot:[`item.type`]="{ item }" v-if="MENU_TYPE">
          {{ item.type | getCodeText(MENU_TYPE) }}
        </template>
        <template v-slot:[`item.name`]="{ item }">
          <span :style="`padding-left: ${80 * (item.level - 1)}px;`">
            <v-icon v-if="item.icon"> {{ item.icon }} </v-icon>
            {{ item.name }}
          </span>
        </template>
        <template v-slot:[`item.updated`]="{ item }">
          {{ item.updated | formatDatetime }}
        </template>
        <template v-slot:[`item.updatedBy`]="{ item }">
          {{ item.updatedBy | formatMemberNm }}
        </template>
        <template v-slot:[`item.action`]="{ item }">
          <v-btn
            class="mx-1"
            tile
            color="button-add"
            small
            @click="$emit('add-item', item)"
            :disabled="item.level === 3"
          >
            하위메뉴입력
          </v-btn>
          <v-btn
            class="mx-1"
            tile
            color="button-edit"
            small
            :disabled="item.name === '///'"
            @click="$emit('edit-item', item)"
          >
            수정
          </v-btn>
          <v-btn
            class="mx-1"
            tile
            color="button-delete"
            small
            @click="$emit('delete-item', item)"
            :disabled="!item.parentId"
          >
            삭제
          </v-btn>
        </template>
      </v-data-table>
    </v-card-text>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { DataTableHeader, SelectItem, TableMenuEntity } from '@/common/types';
import { getApi, getCodesApi } from '@/utils/apis';

interface MenuVO extends TableMenuEntity {
  level: number;
}

@Component({
  name: 'MenuList',
  components: {},
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  readonly ENDPOINT_URL: string = 'admin/menus/';
  items: TableMenuEntity[] = [];
  loading: boolean = false;
  MENU_TYPE: SelectItem[] = [];

  headers: DataTableHeader[] = [
    {
      text: `타입`,
      align: `center`,
      value: `type`,
      filterType: 'select',
      filterSelectItem: [],
      width: '5rem',
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
      width: '5rem',
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

  async mounted() {
    this.MENU_TYPE = await getCodesApi(`MENU_TYPE`);
    this.headers[
      this.headers.indexOf(this.headers.find((item) => item.value === 'type')!)
    ].filterSelectItem = this.MENU_TYPE;
    await this.getList();
  }

  async getList() {
    this.items = [];
    this.loading = true;
    const response = await getApi<MenuVO[]>(this.ENDPOINT_URL);
    this.loading = false;
    this.items = response?.data || [];
  }
}
</script>
