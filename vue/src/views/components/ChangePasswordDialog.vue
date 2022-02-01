<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable max-width="25vw">
      <v-card class="pb-4">
        <dialog-title
          text="비밀번호 변경"
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="이전 비밀번호"
                  vid="oldPassword"
                  rules="required|max:20"
                >
                  <v-text-field
                    v-model="oldPassword"
                    label="이전 비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
                    class="required"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="비밀번호"
                  vid="password"
                  rules="required|max:20"
                >
                  <v-text-field
                    v-model="password"
                    label="비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show2 ? 'text' : 'password'"
                    @click:append="show2 = !show2"
                    class="required"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="비밀번호 확인"
                  rules="required|confirmed:password|max:20"
                >
                  <v-text-field
                    v-model="password2"
                    label="비밀번호 확인"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show3 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show3 ? 'text' : 'password'"
                    @keyup.enter="save"
                    @click:append="show3 = !show3"
                    class="required"
                  />
                </ValidationProvider>
              </v-col>
            </v-row>
          </ValidationObserver>
        </v-card-text>
        <button-with-icon
          block
          text="저장"
          icon="mdi-content-save"
          :loading="loading"
          @click="save"
        />
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script lang="ts">
import { patchApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";
import DialogTitle from "@/components/title/DialogTitle.vue";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import { defineComponent, reactive, ref, toRefs } from "@vue/composition-api";
import setupSyncedDialog from "@/composition/setupSyncedDialog";

export default defineComponent({
  components: { ButtonWithIcon, DialogTitle },
  props: {
    dialog: { required: true, type: Boolean },
  },
  setup(props, { emit }) {
    const syncedDialog = setupSyncedDialog(props, emit);
    const state = reactive({
      loading: false,
      oldPassword: "",
      password: "",
      password2: "",
      show1: false,
      show2: false,
      show3: false,
    });
    const methods = {
      save: async (): Promise<void> => {
        const isValid = await observer.value?.validate();
        if (!isValid) {
          return;
        }

        state.loading = true;
        const response = await patchApi<{
          oldPassword: string;
          newPassword: string;
        }>("mine/password", {
          oldPassword: pbkdf2
            .pbkdf2Sync(state.oldPassword, "salt", 1, 32, "sha512")
            .toString(),
          newPassword: pbkdf2
            .pbkdf2Sync(state.password, "salt", 1, 32, "sha512")
            .toString(),
        });
        state.loading = false;
        if (response.success) {
          syncedDialog.syncedDialog.value = false;
        }
      },
    };
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...syncedDialog, ...toRefs(state), ...methods, observer };
  },
});
</script>
