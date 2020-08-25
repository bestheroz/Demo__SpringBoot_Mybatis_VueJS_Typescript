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
        :height="773"
      >
        <template v-slot:top>
          <button-set reload-button @click:reload="getList" />
        </template>
        <template v-slot:item.type="{ item }" v-if="MENU_TYPE">
          {{ item.type | getCodeText(MENU_TYPE) }}
        </template>
        <template v-slot:item.name="{ item }">
          <span :style="`padding-left: ${80 * (item.level - 1)}px;`">
            <v-icon v-if="item.icon"> {{ item.icon }} </v-icon>
            {{ item.name }}
          </span>
        </template>
        <template v-slot:item.updated="{ item }">
          {{ item.updated | formatDatetime }}
        </template>
        <template v-slot:item.updatedBy="{ item }">
          {{ item.updatedBy | formatMemberNm }}
        </template>
        <template v-slot:item.action="{ item }">
          <v-btn
            class="mx-1"
            tile
            color="button-add"
            small
            @click="
              () => {
                mode = '추가';
                editItem = {
                  parentId: item.id,
                };
                dialog = true;
              }
            "
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
            small
            @click="
              () => {
                editItem = item;
                $refs.refEditDialog.delete();
              }
            "
            :disabled="!item.parentId"
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
import { DataTableHeader, SelectItem, TableMenuEntity } from '@/common/types';
import { getCodeListApi, getListApi } from '@/utils/apis';
import envs from '@/constants/envs';
import MenuEditDialog from '@/views/admin/menu/components/MenuEditDialog.vue';
import ButtonSet from '@/components/speeddial/ButtonSet.vue';

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
  readonly envs: typeof envs = envs;
  readonly ENDPOINT_URL: string = 'admin/menus/';
  mode: string | null = null;
  items: TableMenuEntity[] = [];
  editItem: TableMenuEntity = Object.create(null);
  selected: TableMenuEntity[] = [];
  loading: boolean = false;
  dialog: boolean = false;

  MENU_TYPE: SelectItem[] | null = null;

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
    this.MENU_TYPE = await getCodeListApi(`MENU_TYPE`);
    this.headers[0].filterSelectItem = this.MENU_TYPE;
    await this.getList();
  }

  async getList() {
    this.selected = [];
    this.items = [];
    this.loading = true;
    const response = await getListApi<MenuVO[]>(this.ENDPOINT_URL);
    this.loading = false;
    this.items = response.data || [];
  }
}
</script>
