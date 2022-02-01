<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable>
      <v-card class="pb-4">
        <dialog-title
          v-model="vModel.available"
          :is-new="isNew"
          prefix="역할"
          with-switch
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <v-form :readonly="!$store.getters.writeAuthority">
            <ValidationObserver ref="observer">
              <v-row>
                <v-col cols="12" md="6">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="역할명"
                    rules="required|max:50"
                  >
                    <v-text-field
                      v-model="vModel.name"
                      label="역할명"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
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
import { postApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogTitle from "@/components/title/DialogTitle.vue";
import type { Role } from "@/definitions/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import {
  defineComponent,
  PropType,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";
import store from "@/store";
import setupEditDialog from "@/composition/setupEditDialog";

export default defineComponent({
  components: { ButtonWithIcon, CreatedUpdatedBar, DialogTitle },
  props: {
    value: {
      type: Object as PropType<Role>,
      required: true,
    },
    dialog: {
      required: true,
      type: Boolean,
    },
  },
  setup(props, { emit }) {
    const editDialog = setupEditDialog<Role>(props, emit, "roles/");
    const state = reactive({});
    const methods = {
      save: async (): Promise<void> => {
        const isValid = await observer.value?.validate();
        if (!isValid) {
          return;
        }
        editDialog.isNew.value
          ? await methods.create()
          : await editDialog.update();
      },

      create: async (): Promise<void> => {
        editDialog.loading.value = true;
        const response = await postApi<Role>(
          `roles/?parentId=${
            store.getters.isSuperAdmin ? "" : store.getters.roleId
          }`,
          editDialog.vModel.value,
        );
        editDialog.loading.value = false;
        if (response.success) {
          editDialog.syncedDialog.value = false;
          emit("created", response.data);
        }
      },
    };
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...editDialog, ...toRefs(state), ...methods, observer };
  },
});
</script>
