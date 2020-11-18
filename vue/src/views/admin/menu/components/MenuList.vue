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
        <template v-if="MENU_TYPE" #[`item.type`]="{ item }">
          {{ item.type | getCodeText(MENU_TYPE) }}
        </template>
        <template #[`item.name`]="{ item }">
          <span :style="`padding-left: ${80 * (item.level - 1)}px;`">
            <v-icon v-if="item.icon"> {{ item.icon }} </v-icon>
            {{ item.name }}
          </span>
        </template>
        <template #[`item.updated`]="{ item }">
          {{ item.updated | formatDatetime }}
        </template>
        <template #[`item.updatedBy`]="{ item }">
          {{ item.updatedBy | formatMemberNm }}
        </template>
        <template #[`item.action`]="{ item }">
          <v-btn
            class="mx-1"
            tile
            color="button-add"
            small
            :disabled="item.level === 3"
            @click="$emit('add-item', item)"
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
            :disabled="!item.parentId"
            @click="$emit('delete-item', item)"
          >
            삭제
          </v-btn>
        </template>
      </v-data-table>
    </v-card-text>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import type {
  DataTableHeader,
  SelectItem,
  TableMenuEntity,
} from "@/common/types";
import { getApi, getCodesApi } from "@/utils/apis";

interface MenuVO extends TableMenuEntity {
  level: number;
}

@Component({
  name: "MenuList",
  components: {},
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  readonly ENDPOINT_URL: string = "admin/menus/";
  items: TableMenuEntity[] = [];
  loading = false;
  MENU_TYPE: SelectItem[] = [];

  headers: DataTableHeader[] = [
    {
      text: "타입",
      align: "center",
      value: "type",
      filterType: "select",
      filterSelectItem: [],
      width: "5rem",
    },
    {
      text: "메뉴명",
      align: "start",
      value: "name",
    },
    {
      text: "메뉴 순서",
      align: "end",
      value: "displayOrder",
      width: "5rem",
    },
    {
      text: "Action",
      align: "center",
      value: "action",
      filterable: false,
      width: "18rem",
    },
    {
      text: "작업 일시",
      align: "center",
      value: "updated",
      filterable: false,
      width: "10rem",
    },
    {
      text: "작업자",
      align: "start",
      value: "updatedBy",
      filterable: false,
      width: "7rem",
    },
  ];

  async beforeMount(): Promise<void> {
    this.MENU_TYPE = await getCodesApi("MENU_TYPE");
  }

  mounted(): void {
    const find = this.headers.find((item) => item.value === "type");
    if (find) {
      this.headers[
        this.headers.indexOf(find)
      ].filterSelectItem = this.MENU_TYPE;
    }
    this.getList();
  }

  async getList(): Promise<void> {
    this.items = [];
    this.loading = true;
    const response = await getApi<MenuVO[]>(this.ENDPOINT_URL);
    this.loading = false;
    this.items = response?.data || [];
  }
}
</script>
