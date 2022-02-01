<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable max-width="40vw">
      <v-card class="pb-4">
        <dialog-title text="내 정보" @click:close="syncedDialog = false" />
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12">
                <v-text-field
                  :value="item.loginId"
                  label="나의 아이디"
                  disabled
                />
              </v-col>
              <v-col cols="12">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="나의 이름"
                  rules="required|max:100"
                >
                  <v-text-field
                    v-model="item.name"
                    label="나의 이름"
                    :counter="100"
                    :error-messages="errors"
                    class="required"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  :value="item.role.name"
                  label="나의 역할"
                  disabled
                />
              </v-col>
            </v-row>
          </ValidationObserver>
          <button-with-icon
            block
            text="저장"
            icon="mdi-content-save"
            :loading="loading"
            @click="save"
          />
        </v-card-text>
        <created-updated-bar
          :created-date-time="item.created"
          :updated-date-time="item.updated"
        />
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script lang="ts">
import { getApi, patchApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogTitle from "@/components/title/DialogTitle.vue";
import { defaultAdmin } from "@/definitions/defaults";
import type { Admin } from "@/definitions/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import {
  defineComponent,
  onBeforeMount,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";
import setupSyncedDialog from "@/composition/setupSyncedDialog";
import store from "@/store";

export default defineComponent({
  components: {
    ButtonWithIcon,
    CreatedUpdatedBar,
    DialogTitle,
  },
  props: {
    dialog: {
      required: true,
      type: Boolean,
    },
    adminPassword: {
      required: true,
      type: String,
    },
  },
  setup(props, { emit }) {
    const syncedDialog = setupSyncedDialog(props, emit);
    const state = reactive({ item: defaultAdmin(), loading: false });
    const methods = {
      save: async () => {
        const isValid = await observer.value?.validate();
        if (!isValid) {
          return;
        }
        state.loading = true;
        const payload = { ...state.item, password: props.adminPassword };
        const response = await patchApi<Admin>("mine", payload);
        state.loading = false;
        if (response.success) {
          await store.dispatch("reIssueAccessToken");
          await store.dispatch("reloadAdminCodes");
          syncedDialog.syncedDialog.value = false;
        }
      },
    };
    onBeforeMount(async () => {
      const response = await getApi<Admin>("mine");
      state.item = response.data || defaultAdmin();
    });
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...syncedDialog, ...toRefs(state), ...methods, observer };
  },
});
</script>
