<template>
  <div class="w-full">
    <page-title
      @click="showAddDialog"
      :button-loading="saving"
      :more-actions="$store.getters.writeAuthority"
    >
      <template #list>
        <v-list>
          <v-list-item>
            <v-btn @click="saveItems" :loading="saving">
              <v-icon class="drag-handle"> mdi-sort </v-icon> 순서저장
            </v-btn>
          </v-list-item>
        </v-list>
      </template>
    </page-title>
    <v-card>
      <v-card-text>
        <data-table-filter :filters="filters" :output.sync="filterOutput" />
        <code-list
          ref="refCodeList"
          :type="type"
          class="pt-1"
          @created="onCreated"
          @removed="onRemoved"
        />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import CodeList from "@/views/management/code/CodeList.vue";
import PageTitle from "@/components/title/PageTitle.vue";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import { Filter } from "@/definitions/types";
import { getApi } from "@/utils/apis";
import { Code } from "@/definitions/models";

@Component({
  components: {
    DataTableFilter,
    PageTitle,
    CodeList,
  },
})
export default class extends Vue {
  @Ref() readonly refCodeList!: CodeList;

  types: string[] = [];
  saving = false;

  filterOutput: Record<string, string | number | boolean[]> = {};
  get filters(): Filter[] {
    return [
      {
        type: "checkbox",
        text: "타입",
        key: "type",
        single: true,
        required: true,
        items: this.types.map((v, index) => {
          return { value: v, text: v, checked: index === 0 };
        }),
      },
    ];
  }

  get type(): string {
    return this.filterOutput.type as string;
  }

  protected created(): void {
    this.getTypes().then();
  }

  protected async saveItems(): Promise<void> {
    this.saving = true;
    await this.refCodeList.saveItems();
    this.saving = false;
  }
  protected async showAddDialog(): Promise<void> {
    await this.refCodeList.showAddDialog();
  }

  protected async getTypes(): Promise<void> {
    this.types = [];
    const response = await getApi<string[]>("codes/types/");
    this.types = response.data ? response.data : [];
  }
  protected onCreated(value: Code): void {
    this.types = [value.type, ...this.types];
  }
  protected onRemoved(value: Code): void {
    this.types = this.types.filter((item) => item !== value.type);
  }
}
</script>
