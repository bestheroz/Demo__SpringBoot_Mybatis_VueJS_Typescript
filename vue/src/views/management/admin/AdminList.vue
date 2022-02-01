<template>
  <div>
    <page-title @click="showAddDialog" :button-loading="saving">
      <template #more-buttons>
        <v-btn
          @click="excel"
          color="primary"
          outlined
          x-large
          v-if="$store.getters.excelAuthority"
        >
          <v-icon> mdi-file-excel</v-icon>
          엑셀다운로드
        </v-btn>
        <v-btn @click="fetchList" color="primary" outlined x-large>
          <v-icon> mdi-refresh</v-icon>
          새로고침
        </v-btn>
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
          <template #[`item.loginId`]="{ item }">
            <a
              class="text--anchor"
              @click="showEditDialog({ ...item, password: undefined })"
              v-text="item.loginId"
            />
          </template>
          <template #[`item.available`]="{ item }">
            <v-icon v-if="item.available" color="success">
              mdi-check-circle
            </v-icon>
            <v-icon v-else> mdi-circle-outline</v-icon>
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
            <v-icon v-else> mdi-circle-outline</v-icon>
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
import type { Filter, SelectItem } from "@/definitions/types";
import { downloadExcelApi, getApi } from "@/utils/apis";
import AdminEditDialog from "@/views/management/admin/AdminEditDialog.vue";
import { defaultAdmin } from "@/definitions/defaults";
import type { Admin, Role } from "@/definitions/models";
import PageTitle from "@/components/title/PageTitle.vue";
import { BooleanTypes } from "@/definitions/selections";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import {
  computed,
  defineComponent,
  onMounted,
  reactive,
  toRefs,
  watch,
} from "@vue/composition-api";
import setupReadonly from "@/composition/setupReadonly";
import setupListDialog from "@/composition/setupListDialog";
import { DataTableHeader } from "vuetify";
import setupDatatable from "@/composition/setupDatatable";
import qs from "qs";

export default defineComponent({
  components: { DataTableFilter, PageTitle, AdminEditDialog },
  props: {
    height: {
      type: [Number, String],
      default: undefined,
    },
  },

  setup() {
    const listDialog = setupListDialog<Admin>(defaultAdmin);
    const datatable = setupDatatable<Admin>("admins/");

    const state = reactive({
      saving: false,
      roles: [] as SelectItem<number>[],
    });

    const computes = {
      headers: computed((): DataTableHeader[] => [
        {
          text: "#key",
          align: "start",
          value: "id",
        },
        {
          text: "관리자 아이디",
          align: "start",
          value: "loginId",
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
      ]),
      filters: computed((): Filter[] => [
        {
          type: "checkbox",
          text: "권한",
          key: "roleIdList",
          items: state.roles.map((v) => {
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
      ]),
      queryStringForExcel: computed((): string =>
        qs.stringify({
          search: datatable.search.value,
          ...datatable.filterOutput.value,
          ...datatable.pagination.value,
          page: 1,
          itemsPerPage: 99999999,
        }),
      ),
    };
    const methods = {
      excel: async (): Promise<void> => {
        state.saving = true;
        await downloadExcelApi(
          `excel/admins?${computes.queryStringForExcel.value}`,
        );
        state.saving = false;
      },
    };
    onMounted(async () => {
      const response = await getApi<Role[]>("roles/selections/");
      state.roles = response.data.map((v) => {
        return { value: v.id || 0, text: v.name };
      });
    });

    watch(
      () => datatable.queryString.value,
      () => datatable.fetchList.value(),
      {
        immediate: true,
      },
    );

    return {
      ...datatable,
      ...listDialog,
      ...setupReadonly(),
      ...toRefs(state),
      ...computes,
      ...methods,
    };
  },
});
</script>
