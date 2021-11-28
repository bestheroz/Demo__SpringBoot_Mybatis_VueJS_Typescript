<template>
  <div>
    <page-title
      @click="showAddDialog"
      :button-loading="saving"
      :more-actions="$store.getters.excelAuthority"
    >
      <template #list>
        <v-list>
          <v-list-item>
            <v-btn @click="excel" v-if="$store.getters.excelAuthority">
              <v-icon> mdi-file-excel </v-icon> 엑셀다운로드
            </v-btn>
          </v-list-item>
        </v-list>
      </template>
    </page-title>
    <v-card>
      <v-card-text>
        <v-row no-gutters>
          <v-col cols="12" sm="7">
            <data-table-filter :filters="filters" :output.sync="filterOutput" />
          </v-col>
          <v-spacer />
          <v-col cols="12" sm="3">
            <v-text-field
              v-model="search"
              solo
              label="검색 (관리자 아이디, 관리자 이름)"
              prepend-inner-icon="mdi-magnify"
              clearable
              outlined
              hide-details="auto"
              dense
            />
          </v-col>
        </v-row>
        <v-data-table
          must-sort
          fixed-header
          :loading="loading"
          :headers="headers"
          :items="items"
          :server-items-length="totalItems"
          :options.sync="pagination"
          item-key="id"
          :height="height"
          :footer-props="envs.FOOTER_PROPS_MAX_100"
        >
          <template #[`item.adminId`]="{ item }">
            <a
              class="text--anchor"
              @click="showEditDialog({ ...item, password: undefined })"
              v-text="item.adminId"
            />
          </template>
          <template #[`item.available`]="{ item }">
            <v-icon v-if="item.available" color="success">
              mdi-check-circle
            </v-icon>
            <v-icon v-else> mdi-circle-outline </v-icon>
          </template>
          <template #[`item.availableSignIn`]="{ item }">
            <v-icon
              v-if="
                item.available &&
                item.role.available &&
                dayjs(item.expired).isAfter(dayjs())
              "
              color="success"
            >
              mdi-check-circle
            </v-icon>
            <v-icon v-else> mdi-circle-outline </v-icon>
          </template>
          <template #[`item.expired`]="{ item }">
            {{ item.expired | formatDatetime }}
          </template>
          <template #[`item.updated`]="{ item }">
            {{ item.updated | formatDatetime }}
          </template>
          <template #[`item.updatedBy`]="{ item }">
            {{ item.updatedBy | formatAdminNm }}
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
    <admin-edit-dialog
      v-model="editItem"
      :dialog.sync="dialog"
      @created="onCreated"
      @updated="onUpdated"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import type { Filter, PageResult, Pagination } from "@/definitions/types";
import { SelectItem } from "@/definitions/types";
import { downloadExcelApi, getApi } from "@/utils/apis";
import envs from "@/constants/envs";
import AdminEditDialog from "@/views/management/admin/AdminEditDialog.vue";
import qs from "qs";
import { defaultAdmin } from "@/definitions/defaults";
import type { Admin, Role } from "@/definitions/models";
import { cloneDeep, debounce } from "lodash-es";
import PageTitle from "@/components/title/PageTitle.vue";
import dayjs from "dayjs";
import { BooleanTypes } from "@/definitions/selections";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import { DataTableHeader } from "vuetify";

@Component({
  components: {
    DataTableFilter,
    PageTitle,
    AdminEditDialog,
  },
})
export default class AdminList extends Vue {
  @Prop() readonly height!: number | string;

  readonly envs: typeof envs = envs;
  readonly dayjs = dayjs;

  pagination: Pagination = {
    page: 1,
    sortBy: ["updated"],
    sortDesc: [true],
    itemsPerPage: 10,
  };

  items: Admin[] = [];
  totalItems = 0;
  loading = false;
  saving = false;
  editItem: Admin = defaultAdmin();
  dialog = false;

  search = "";
  roles: SelectItem<number>[] = [];
  filterOutput: Record<string, string | number | boolean[]> = {};
  get filters(): Filter[] {
    return [
      {
        type: "checkbox",
        text: "권한",
        key: "roleIdList",
        items: this.roles.map((v) => {
          return { ...v, checked: false };
        }),
      },
      {
        type: "checkbox",
        text: "사용 가능",
        key: "availableList",
        items: BooleanTypes.map((v) => {
          return { ...v, checked: false };
        }),
        single: true,
      },
    ];
  }

  get headers(): DataTableHeader[] {
    return [
      {
        text: "#key",
        align: "start",
        value: "id",
      },
      {
        text: "관리자 아이디",
        align: "start",
        value: "adminId",
      },
      {
        text: "관리자 이름",
        align: "start",
        value: "name",
      },
      {
        text: "역할",
        align: "center",
        value: "role.name",
      },
      {
        text: "만료일",
        align: "center",
        value: "expired",
        width: "11.5rem",
      },
      {
        text: "사용 가능",
        align: "center",
        value: "available",
        width: "6rem",
      },
      {
        text: "로그인 가능",
        align: "center",
        value: "availableSignIn",
        width: "6rem",
        sortable: false,
      },
      {
        text: "작업 일시",
        align: "center",
        value: "updated",
        width: "11.5rem",
      },
      {
        text: "작업자",
        align: "start",
        value: "updatedBy",
        width: "8rem",
      },
    ];
  }

  get queryString(): string {
    return qs.stringify({
      search: this.search,
      ...this.filterOutput,
      ...this.pagination,
    });
  }

  get queryStringForExcel(): string {
    return qs.stringify({
      search: this.search,
      ...this.filterOutput,
      ...this.pagination,
      page: 1,
      itemsPerPage: 99999999,
    });
  }

  protected async created(): Promise<void> {
    const response = await getApi<Role[]>("roles/selections/");
    this.roles = response.data.map((v) => {
      return { value: v.id || 0, text: v.name };
    });
  }

  @Watch("queryString", { immediate: true })
  public async getList(): Promise<void> {
    this.fetchList();
  }

  protected fetchList = debounce(async function (this: AdminList) {
    this.items = [];
    this.totalItems = 0;
    this.loading = true;
    const response = await getApi<PageResult<Admin>>(
      `admins/?${this.queryString}`,
    );
    this.loading = false;
    this.items = response.data.content || [];
    this.totalItems = response.data.totalElements;
  }, 300);

  protected onCreated(value: Admin): void {
    this.saving = true;
    this.items = [value, ...this.items].slice(0, this.pagination.itemsPerPage);
    this.totalItems++;
    this.saving = false;
  }

  protected onUpdated(value: Admin): void {
    this.saving = true;
    this.items.splice(
      this.items.findIndex((item) => item.id === this.editItem.id),
      1,
      value,
    );
    this.saving = false;
  }
  protected showAddDialog(): void {
    this.editItem = defaultAdmin();
    this.dialog = true;
  }

  protected showEditDialog(value: Admin): void {
    this.editItem = cloneDeep(value);
    this.dialog = true;
  }

  protected async excel(): Promise<void> {
    this.saving = true;
    await downloadExcelApi(`excel/admins?${this.queryStringForExcel}`);
    this.saving = false;
  }
}
</script>
