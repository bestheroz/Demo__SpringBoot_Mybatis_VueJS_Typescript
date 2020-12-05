<template>
  <div>
    <v-card flat>
      <button-set
        :loading="saving"
        reload-button
        :reload-disabled="!authority"
        save-button
        :save-disabled="!authority"
        @click:reload="getList"
        @click:save="saveItem"
      />
      <v-card-text class="pt-0 pb-1">
        <v-data-table
          must-sort
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
            <span
              :style="`padding-left: ${
                80 * (item.level - 1)
              }px; display: inline-flex;`"
            >
              <v-checkbox
                v-model="item.checked"
                :label="item.name"
                :prepend-icon="item.icon"
                :readonly="item.id === 1"
                :disabled="
                  item.id !== 1 &&
                  item.parentId &&
                  !items.find((value) => value.id === item.parentId).checked
                "
                :ripple="item.id !== 1"
                dense
                hide-details
                class="mt-0 pa-0"
                @click.passive="setChildrenChecked(item)"
              />
            </span>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import type {
  DataTableHeader,
  SelectItem,
  TableMenuEntity,
} from "@/common/types";
import { getApi, getCodesApi, putApi } from "@/utils/apis";
import envs from "@/constants/envs";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";

interface AdminMenuAuthorityVO extends TableMenuEntity {
  checked: boolean;
}

@Component({
  name: "MenuAuthorityList",
  components: {
    ButtonSet,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly authority!: string;
  @Prop({ required: true }) readonly height!: number | string;
  readonly envs: typeof envs = envs;
  readonly ENDPOINT_URL = "admin/menuAuthority/";
  items: AdminMenuAuthorityVO[] = [];
  loading = false;
  saving = false;

  MENU_TYPE: SelectItem[] = [];

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
        text: "메뉴 ID",
        align: "end",
        value: "id",
        width: "6rem",
      },
      {
        text: "상위 메뉴 ID",
        align: "end",
        value: "parentId",
        width: "6rem",
      },
      {
        text: "메뉴명",
        align: "start",
        value: "name",
      },
    ];
  }

  protected async beforeMount(): Promise<void> {
    this.MENU_TYPE = await getCodesApi("MENU_TYPE");
  }

  @Watch("authority")
  protected watchAuthority(val: string): void {
    if (val) {
      this.getList();
    }
  }

  protected setChildrenChecked(item: AdminMenuAuthorityVO): void {
    if (!item.checked) {
      this.items
        .filter((value) => value.parentId === item.id)
        .forEach((value) => {
          value.checked = false;
        });
    }
  }

  public async getList(): Promise<void> {
    this.items = [];
    this.loading = true;
    const response = await getApi<AdminMenuAuthorityVO[]>(
      `${this.ENDPOINT_URL}${this.authority}`,
    );
    this.loading = false;
    this.items = response?.data || [];
  }

  protected async saveItem(): Promise<void> {
    this.saving = true;
    await putApi<{ menuIdList: string }>(
      `${this.ENDPOINT_URL}${this.authority}/`,
      {
        menuIdList: this.items
          .map((value) => (value.checked ? value.id : undefined))
          .filter((value) => value !== undefined)
          .join(","),
      },
    );
    this.saving = false;
  }
}
</script>
