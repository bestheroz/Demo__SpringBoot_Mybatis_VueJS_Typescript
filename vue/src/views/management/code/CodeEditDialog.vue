<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable>
      <v-card class="pb-4">
        <dialog-title
          v-model="vModel.available"
          :is-new="isNew"
          prefix="코드"
          with-switch
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <v-form :readonly="!$store.getters.writeAuthority">
            <ValidationObserver ref="observer">
              <v-row>
                <v-col cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="그룹 코드"
                    rules="max:50|required"
                  >
                    <v-text-field
                      v-model="vModel.type"
                      label="그룹 코드"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="코드"
                    rules="max:50|required"
                  >
                    <v-text-field
                      v-model="vModel.value"
                      label="코드"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="코드명"
                    rules="max:100|required"
                  >
                    <v-text-field
                      v-model="vModel.text"
                      label="코드명"
                      :counter="100"
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
import { putApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogTitle from "@/components/title/DialogTitle.vue";
import type { Code } from "@/definitions/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import { defineComponent, PropType, ref } from "@vue/composition-api";
import setupEditDialog from "@/composition/setupEditDialog";

export default defineComponent({
  components: { ButtonWithIcon, CreatedUpdatedBar, DialogTitle },
  props: {
    value: {
      type: Object as PropType<Code>,
      required: true,
    },
    dialog: {
      required: true,
      type: Boolean,
    },
  },
  setup(props, { emit }) {
    const editDialog = setupEditDialog<Code>(props, emit, "codes/");
    const methods = {
      save: async (): Promise<void> => {
        const isValid = await observer.value?.validate();
        if (!isValid) {
          return;
        }
        editDialog.isNew.value
          ? await editDialog.create()
          : await methods.update();
      },

      update: async (): Promise<void> => {
        editDialog.loading.value = true;
        const response = await putApi<Code>(
          `codes/${editDialog.vModel.value.id}`,
          editDialog.vModel.value,
        );
        editDialog.loading.value = false;
        if (response.success) {
          editDialog.syncedDialog.value = false;
          window.sessionStorage.removeItem(
            `code__${editDialog.vModel.value.type}`,
          );
          emit("updated", response.data);
        }
      },
    };
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...editDialog, ...methods, observer };
  },
});
</script>
