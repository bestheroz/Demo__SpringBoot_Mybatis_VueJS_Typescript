<template>
  <div>
    <v-card>
      <button-set
        :disabled="!codeGroup"
        add-button
        delete-button
        reload-button
        :delete-disabled="!selected || selected.length === 0"
        @click:add="dialog = true"
        @click:delete="deleteItem"
        @click:reload="getList"
      />
      <v-system-bar class="secondary">
        <v-icon> mdi-format-list-checkbox </v-icon>
        코드 관리 - Detail
      </v-system-bar>
      <v-card-text>
        <v-data-table
          v-model="selected"
          must-sort
          fixed-header
          :loading="loading"
          :headers="headers"
          :items="filteredItems"
          :sort-by="sortBy"
          :sort-desc="sortDesc"
          item-key="code"
          single-select
          show-select
          dense
          :height="height"
          :footer-props="envs.FOOTER_PROPS_MAX_1000"
        >
          <template #header>
            <data-table-client-side-filter
              :headers="headers"
              :output.sync="filteredItems"
              :input="items"
            />
          </template>
          <template #[`item.code`]="{ item }">
            <a class="text--anchor" @click="editItem(item)">
              {{ item.code }}
            </a>
          </template>
          <template #[`item.available`]="{ item }">
            <span style="display: inline-flex">
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
          <template v-if="AUTHORITY" #[`item.authority`]="{ item }">
            {{ item.authority | getCodeText(AUTHORITY) }}
          </template>
          <template #[`item.updatedBy`]="{ item }">
            {{ item.updatedBy | formatMemberNm }}
          </template>
          <template #[`item.updated`]="{ item }">
            {{ item.updated | formatDatetime }}
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <code-edit-dialog
      v-model="item"
      :dialog.sync="dialog"
      @finished="getList"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import type {
  DataTableHeader,
  SelectItem,
  TableCodeEntity,
} from "@/common/types";
import { deleteApi, getApi, getCodesApi } from "@/utils/apis";
import envs from "@/constants/envs";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import { confirmDelete } from "@/utils/alerts";
import CodeEditDialog from "@/views/admin/code/components/CodeEditDialog.vue";
import DataTableClientSideFilter from "@/components/datatable/DataTableClientSideFilter.vue";
import { defaultTableCodeEntity } from "@/common/values";

@Component({
  name: "CodeList",
  components: {
    DataTableClientSideFilter,
    CodeEditDialog,
    ButtonSet,
    DataTableFilter,
  },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;
  @Prop({ required: true }) readonly codeGroup!: string;

  readonly envs: typeof envs = envs;
  AUTHORITY: SelectItem[] = [];

  loading = false;
  sortBy: string[] = ["displayOrder"];
  sortDesc: boolean[] = [false];
  items: TableCodeEntity[] = [];
  filteredItems: TableCodeEntity[] = [];
  selected: TableCodeEntity[] = [];

  dialog = false;
  item: TableCodeEntity = defaultTableCodeEntity();

  get headers(): DataTableHeader[] {
    return [
      {
        text: "상세 코드",
        align: "start",
        value: "code",
      },
      {
        text: "상세 코드명",
        align: "start",
        value: "name",
      },
      {
        text: "사용 가능",
        align: "center",
        value: "available",
        filterType: "switch",
        width: "6rem",
      },
      {
        text: "출력 순서",
        align: "end",
        value: "displayOrder",
        width: "6rem",
      },
      {
        text: "권한(~부터 볼수 있음)",
        align: "center",
        value: "authority",
        filterType: "select",
        filterSelectItem: this.AUTHORITY,
        width: "11rem",
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
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  @Watch("codeGroup")
  protected async getList(): Promise<void> {
    this.selected = [];
    this.items = [];
    if (!this.codeGroup) {
      return;
    }
    this.loading = true;
    const response = await getApi<TableCodeEntity[]>(
      `admin/codes/${this.codeGroup}`,
    );
    this.loading = false;
    this.items = response?.data || [];
  }

  protected editItem(value: TableCodeEntity): void {
    this.item = { ...value };
    this.dialog = true;
  }

  protected async deleteItem(): Promise<void> {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteApi<TableCodeEntity>(
        `admin/codes/${this.codeGroup}/${this.selected[0].code}/`,
      );
      this.loading = false;
      if (response?.code?.startsWith("S")) {
        window.localStorage.removeItem(`code__${this.codeGroup}`);
        this.getList().then();
      }
    }
  }
}
</script>
