<template>
  <div>
    <v-card flat>
      <button-set reload-button @click:reload="getList" />
      <v-card-text>
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
              :loading="saving"
              :disabled="item.level === 3"
              @click="onAdd(item)"
            >
              하위메뉴입력
            </v-btn>
            <v-btn
              class="mx-1"
              tile
              color="button-edit"
              small
              :loading="saving"
              :disabled="item.name === '///'"
              @click="onEdit(item)"
            >
              수정
            </v-btn>
            <v-btn
              class="mx-1"
              tile
              color="button-delete"
              small
              :loading="saving"
              :disabled="!item.parentId"
              @click="onDelete(item)"
            >
              삭제
            </v-btn>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <menu-edit-dialog
      :item="item"
      :dialog.sync="dialog"
      @finished="getList"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import type {
  DataTableHeader,
  SelectItem,
  TableMenuEntity,
} from "@/common/types";
import { deleteApi, getApi, getCodesApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import MenuEditDialog from "@/views/admin/menu/components/MenuEditDialog.vue";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";

interface MenuVO extends TableMenuEntity {
  level: number;
}

@Component({
  name: "MenuList",
  components: { ButtonSet, MenuEditDialog },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  items: TableMenuEntity[] = [];
  loading = false;
  saving = false;
  MENU_TYPE: SelectItem[] = [];

  item: TableMenuEntity = Object.create(null);
  dialog = false;

  get headers(): DataTableHeader[] {
    return [
      {
        text: "타입",
        align: "center",
        value: "type",
        filterType: "select",
        filterSelectItem: this.MENU_TYPE,
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
  }

  protected async beforeMount(): Promise<void> {
    this.MENU_TYPE = await getCodesApi("MENU_TYPE");
  }

  public async getList(): Promise<void> {
    this.items = [];
    this.loading = true;
    const response = await getApi<MenuVO[]>("admin/menus/");
    this.loading = false;
    this.items = response?.data || [];
  }

  protected onAdd(value: TableMenuEntity): void {
    this.item = {
      parentId: value.id,
    };
    this.dialog = true;
  }

  protected onEdit(value: TableMenuEntity): void {
    this.item = { ...value };
    this.dialog = true;
  }

  protected async onDelete(value: TableMenuEntity): Promise<void> {
    const result = await confirmDelete();
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<TableMenuEntity>(
        `admin/menus/${value.id}/`,
      );
      this.saving = false;
      if (response?.code?.startsWith("S")) {
        await this.$store.dispatch("initDrawers");
        this.getList().then();
      }
    }
  }
}
</script>
