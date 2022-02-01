<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable>
      <v-card class="pb-4">
        <dialog-title
          :is-new="isNew"
          prefix="메뉴"
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <v-form :readonly="!$store.getters.writeAuthority">
            <ValidationObserver ref="observer">
              <v-row>
                <v-col cols="12" md="3">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="메뉴명"
                    rules="required|max:50"
                  >
                    <v-text-field
                      v-model="vModel.name"
                      label="메뉴명"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="3">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="타입"
                    rules="required"
                  >
                    <v-select
                      v-model="vModel.type"
                      :items="MenuTypes"
                      label="타입"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="6" v-if="vModel.type !== MENU_TYPE.GROUP">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="링크 URL"
                    rules="max:255"
                  >
                    <v-text-field
                      v-model="vModel.url"
                      label="링크 URL"
                      :counter="255"
                      :error-messages="errors"
                      clearable
                    />
                  </ValidationProvider>
                </v-col>
                <v-col v-if="vModel.type === MENU_TYPE.GROUP" cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="메뉴 아이콘"
                    rules="max:50|required"
                  >
                    <v-text-field
                      v-model="vModel.icon"
                      label="메뉴 아이콘"
                      append-icon="mdi-open-in-new"
                      :counter="50"
                      :error-messages="errors"
                      @click:append="linkIconSite"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col v-if="vModel.type === MENU_TYPE.GROUP" cols="12" md="1">
                  <v-icon v-text="vModel.icon" size="3.5rem" />
                </v-col>
              </v-row>
            </ValidationObserver>
            <button-with-icon
              block
              text="저장"
              icon="mdi-content-save"
              :loading="loading"
              @click="save"
              v-if="$store.getters.writeAuthority"
            />
          </v-form>
        </v-card-text>
        <created-updated-bar
          :created-date-time="vModel.created"
          :updated-date-time="vModel.updated"
        />
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script lang="ts">
import { postApi, putApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogTitle from "@/components/title/DialogTitle.vue";
import type { Menu } from "@/definitions/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import { MENU_TYPE, MenuTypes } from "@/definitions/selections";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import setupEditDialog from "@/composition/setupEditDialog";
import {
  defineComponent,
  PropType,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";
import store from "@/store";

export default defineComponent({
  components: { ButtonWithIcon, CreatedUpdatedBar, DialogTitle },
  props: {
    value: {
      type: Object as PropType<Menu>,
      required: true,
    },
    dialog: {
      required: true,
      type: Boolean,
    },
  },
  setup(props, { emit }) {
    const editDialog = setupEditDialog<Menu>(props, emit, "menus/");
    const state = reactive({
      MenuTypes: MenuTypes,
      MENU_TYPE: MENU_TYPE,
    });
    const methods = {
      save: async (): Promise<void> => {
        const isValid = await observer.value?.validate();
        if (!isValid) {
          return;
        }
        editDialog.isNew.value
          ? await methods.create()
          : await methods.update();
      },

      create: async (): Promise<void> => {
        editDialog.loading.value = true;
        const response = await postApi<Menu>("menus/", editDialog.vModel.value);
        editDialog.loading.value = false;
        if (response.success) {
          store.dispatch("reloadRole").then();
          editDialog.syncedDialog.value = false;
          emit("created", response.data);
        }
      },

      update: async (): Promise<void> => {
        editDialog.loading.value = true;
        const response = await putApi<Menu>(
          `menus/${editDialog.vModel.value.id}`,
          editDialog.vModel.value,
        );
        editDialog.loading.value = false;
        if (response.success) {
          store.dispatch("reloadRole").then();
          editDialog.syncedDialog.value = false;
          emit("updated", response.data);
        }
      },

      linkIconSite: (): void => {
        window.open(
          "https://pictogrammers.github.io/@mdi/font/6.5.95/",
          "_blank",
        );
      },
    };
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...editDialog, ...toRefs(state), ...methods, observer };
  },
});
</script>
