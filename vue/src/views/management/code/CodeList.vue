<template>
  <div>
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
          :animation="200"
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
                  <v-icon class="drag-handle"> mdi-sort</v-icon>
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
                <v-icon v-else> mdi-circle-outline</v-icon>
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
                    <v-icon color="error"> mdi-delete-outline</v-icon>
                  </v-btn>
                </div>
              </v-list-item-action>
            </v-list-item>
          </transition-group>
        </draggable>
      </v-list>
    </v-card>
    <code-edit-dialog
      v-model="editItem"
      :dialog.sync="dialog"
      @created="onCreated"
      @updated="onUpdated"
      v-if="dialog"
    />
  </div>
</template>

<script lang="ts">
import { deleteApi, postApi } from "@/utils/apis";
import CodeEditDialog from "@/views/management/code/CodeEditDialog.vue";
import { confirmDelete } from "@/utils/alerts";
import { defaultCode } from "@/definitions/defaults";
import type { Code } from "@/definitions/models";
import draggable from "vuedraggable";
import { defineComponent, watch } from "@vue/composition-api";
import setupListDialog from "@/composition/setupListDialog";
import setupListPage from "@/composition/setupList";

export default defineComponent({
  components: { CodeEditDialog, draggable },
  props: {
    type: {
      type: String,
      required: true,
    },
  },
  setup(props, { emit }) {
    const listPage = setupListPage<Code>(`codes/?type=${props.type}`);
    const listDialog = setupListDialog<Code>(() => ({
      ...defaultCode(),
      type: props.type,
      displayOrder: listPage.items.value.length + 1,
    }));

    const methods = {
      saveItems: async (): Promise<void> => {
        listPage.loading.value = true;
        const response = await postApi<Code[]>(
          "codes/save-all/",
          listPage.items.value.map((item, index) => {
            return {
              ...item,
              displayOrder: index + 1,
            };
          }),
        );
        listPage.loading.value = false;
        if (response.success) {
          window.sessionStorage.removeItem(`code__${props.type}`);
          listPage.items.value = response.data || [];
        }
      },
      remove: async (value: Code): Promise<void> => {
        const result = await confirmDelete(`코드: ${value.value}`);
        if (result.value) {
          listPage.loading.value = true;
          const response = await deleteApi<Code>(`codes/${value.id}`);
          listPage.loading.value = false;
          if (response.success) {
            window.sessionStorage.removeItem(`code__${value.type}`);
            listPage.items.value = listPage.items.value.filter(
              (item) => item.id !== value.id,
            );
            if (listPage.items.value.length === 0) {
              emit("removed", value);
            }
          }
        }
      },
      onCreated: (value: Code): void => {
        if (props.type === value.type) {
          listPage.items.value = [value, ...listPage.items.value];
        } else {
          emit("created", value);
        }
      },
      onUpdated: (value: Code): void => {
        listPage.items.value.splice(
          listPage.items.value.findIndex(
            (item) => item.id === listDialog.editItem.value.id,
          ),
          1,
          value,
        );
      },
      getList: (): void => {
        listPage.fetchList.value();
      },
    };
    watch(
      () => props.type,
      () => {
        listPage.url.value = `codes/?type=${props.type}`;
        listPage.fetchList.value();
      },
    );
    return { ...listDialog, ...listPage, ...methods };
  },
});
</script>
