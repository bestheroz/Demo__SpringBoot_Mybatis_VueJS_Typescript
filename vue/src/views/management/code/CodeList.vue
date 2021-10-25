<template>
  <div>
    <page-title
      @click="showAddDialog"
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
    <v-card :loading="saving">
      <v-card-text>
        <code-type ref="refCodeType" @selected="(value) => (type = value)" />
        <v-card :loading="loading">
          <v-list>
            <div>
              <span>
                <v-list-item dense class="elevation-1 bottom-solid">
                  <v-list-item-content style="display: inline-block">
                    코드
                  </v-list-item-content>
                  <v-list-item-content style="display: inline-block">
                    코드명
                  </v-list-item-content>
                  <v-list-item-content style="display: inline-block">
                    사용 가능
                  </v-list-item-content>
                  <v-list-item-content style="display: inline-block">
                    작업 일시
                  </v-list-item-content>
                  <v-list-item-content style="display: inline-block">
                    작업자
                  </v-list-item-content>
                  <v-list-item-action
                    style="display: inline-block"
                    class="my-0"
                    v-if="$store.getters.deleteAuthority"
                  >
                    <div class="actions" style="visibility: hidden">
                      <v-btn icon>
                        <v-icon color="error"> mdi-delete-outline </v-icon>
                      </v-btn>
                    </div>
                  </v-list-item-action>
                </v-list-item>
              </span>
            </div>
            <draggable
              tag="div"
              v-model="items"
              v-bind="dragOptions"
              handle=".drag-handle"
            >
              <transition-group type="transition" name="flip-list">
                <v-list-item
                  dense
                  :key="item.value"
                  v-for="item in items"
                  class="elevation-1 bottom-solid"
                >
                  <v-list-item-content style="display: inline-block">
                    <v-btn icon v-if="$store.getters.writeAuthority">
                      <v-icon class="drag-handle"> mdi-sort </v-icon>
                    </v-btn>
                    <a
                      class="text--anchor"
                      @click="showEditDialog(item)"
                      v-text="item.value"
                    />
                  </v-list-item-content>
                  <v-list-item-content
                    style="display: inline-block"
                    v-text="item.text"
                  />
                  <v-list-item-content style="display: inline-block">
                    <v-icon v-if="item.available" color="success">
                      mdi-check-circle
                    </v-icon>
                    <v-icon v-else> mdi-circle-outline </v-icon>
                  </v-list-item-content>
                  <v-list-item-content style="display: inline-block">
                    {{ item.updated | formatDatetime }}
                  </v-list-item-content>
                  <v-list-item-content style="display: inline-block">
                    {{ item.updatedBy | formatAdminNm }}
                  </v-list-item-content>
                  <v-list-item-action
                    style="display: inline-block"
                    class="my-0"
                    v-if="$store.getters.deleteAuthority"
                  >
                    <div class="actions">
                      <v-btn icon @click="remove(item)">
                        <v-icon color="error"> mdi-delete-outline </v-icon>
                      </v-btn>
                    </div>
                  </v-list-item-action>
                </v-list-item>
              </transition-group>
            </draggable>
          </v-list>
        </v-card>
      </v-card-text>
      <code-edit-dialog
        v-model="editItem"
        :dialog.sync="dialog"
        @created="onCreated"
        @updated="onUpdated"
        v-if="dialog"
      />
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue, Watch } from "vue-property-decorator";
import { deleteApi, getApi, postApi } from "@/utils/apis";
import envs from "@/constants/envs";
import CodeEditDialog from "@/views/management/code/CodeEditDialog.vue";
import { confirmDelete } from "@/utils/alerts";
import { defaultCode } from "@/definitions/defaults";
import type { Code } from "@/definitions/models";
import { cloneDeep, debounce } from "lodash-es";
import draggable from "vuedraggable";
import PageTitle from "@/components/title/PageTitle.vue";
import CodeType from "@/views/management/code/CodeType.vue";

@Component({
  components: {
    CodeType,
    PageTitle,
    CodeEditDialog,
    draggable,
  },
})
export default class CodeList extends Vue {
  @Ref("refCodeType") readonly refCodeType!: CodeType;
  readonly envs: typeof envs = envs;

  type = "";
  saving = false;
  loading = false;
  items: Code[] = [];
  dialog = false;
  editItem: Code = defaultCode();

  get dragOptions(): { animation: number } {
    return {
      animation: 200,
    };
  }

  @Watch("type", { immediate: true })
  public async getList(): Promise<void> {
    this.items = [];
    this.loading = true;
    this.fetchList();
  }

  protected fetchList = debounce(async function (this: CodeList) {
    if (this.type) {
      const response = await getApi<Code[]>(`codes/?type=${this.type}`);
      this.loading = false;
      this.items = response.data || [];
    }
  }, 300);

  protected onCreated(value: Code): void {
    if (this.type === value.type) {
      this.items = [value, ...this.items];
    } else {
      this.refCodeType.addNewItem(value);
    }
  }

  protected onUpdated(value: Code): void {
    this.items.splice(
      this.items.findIndex((item) => item.id === this.editItem.id),
      1,
      value,
    );
  }
  public showAddDialog(): void {
    this.editItem = {
      ...defaultCode(),
      type: this.type,
      displayOrder: this.items.length + 1,
    };
    this.dialog = true;
  }

  protected showEditDialog(value: Code): void {
    this.editItem = cloneDeep(value);
    this.dialog = true;
  }

  protected async remove(value: Code): Promise<void> {
    const result = await confirmDelete(`코드: ${value.value}`);
    if (result.value) {
      this.saving = true;
      const response = await deleteApi<Code>(`codes/${value.id}`);
      this.saving = false;
      if (response.code.startsWith("S")) {
        window.localStorage.removeItem(`code__${value.type}`);
        this.items = this.items.filter((item) => item.id !== value.id);
      }
    }
  }

  public async saveItems(): Promise<void> {
    this.saving = true;
    const response = await postApi<Code[]>(
      "codes/save-all/",
      this.items.map((item, index) => {
        return {
          ...item,
          displayOrder: index + 1,
        };
      }),
    );
    this.saving = false;
    if (response.code.startsWith("S")) {
      window.localStorage.removeItem(`code__${this.type}`);
      this.items = response.data || [];
    }
  }
}
</script>
