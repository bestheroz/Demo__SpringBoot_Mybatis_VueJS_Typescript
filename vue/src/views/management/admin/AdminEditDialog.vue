<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable>
      <v-card class="pb-4">
        <dialog-title
          v-model="vModel.available"
          :is-new="isNew"
          prefix="관리자"
          :suffix="noneWriteAuthority ? '보기' : ''"
          with-switch
          :disabled-switch="noneWriteAuthority"
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <v-form :readonly="noneWriteAuthority">
            <ValidationObserver ref="observer">
              <v-row>
                <v-col cols="12" md="3">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="관리자 아이디"
                    rules="required|max:50"
                  >
                    <v-text-field
                      v-model="vModel.loginId"
                      label="관리자 아이디"
                      :counter="50"
                      :error-messages="loginIdErrorText || errors"
                      :success-messages="loginIdSuccessText"
                      :append-icon="loginIdAppendIcon"
                      class="required"
                      :loading="loading"
                      @input="debounceCheckExistsLoginId"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="3">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="관리자 이름"
                    rules="required|max:100"
                  >
                    <v-text-field
                      v-model="vModel.name"
                      label="관리자 이름"
                      :counter="100"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="3">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="역할"
                    rules="required"
                  >
                    <v-input :value="vModel.role.id" class="d-none" />
                    <role-selections
                      v-model="vModel.role"
                      :error-messages="errors"
                      :param-available="true"
                      :disabled="noneWriteAuthority"
                      required
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="3">
                  <datetime-picker
                    v-model="vModel.expired"
                    label="만료일"
                    full-width
                    :disabled="noneWriteAuthority"
                    required
                  />
                </v-col>
                <v-col cols="12" md="3" v-if="isNew">
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
                      :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                      :type="show1 ? 'text' : 'password'"
                      @click:append="show1 = !show1"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="3" v-if="!noneWriteAuthority">
                  <ValidationProvider
                    v-if="isNew"
                    v-slot="{ errors }"
                    name="비밀번호 확인"
                    rules="required|confirmed:password|max:20"
                  >
                    <v-text-field
                      v-model="password2"
                      label="비밀번호 확인"
                      :counter="20"
                      :error-messages="errors"
                      :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                      :type="show2 ? 'text' : 'password'"
                      clearable
                      @click:append="show2 = !show2"
                      class="required"
                    />
                  </ValidationProvider>
                  <v-btn
                    v-else-if="$store.getters.roleId !== vModel.role.id"
                    color="primary"
                    outlined
                    @click="resetPasswordDialog = true"
                  >
                    비밀번호 초기화
                  </v-btn>
                </v-col>
              </v-row>
            </ValidationObserver>
            <button-with-icon
              block
              text="저장"
              icon="mdi-content-save"
              :disabled="
                !loginIdSuccessText && originalLoginId !== vModel.loginId
              "
              :loading="saving"
              @click="save"
              v-if="!noneWriteAuthority"
            />
          </v-form>
        </v-card-text>
        <created-updated-bar
          :created-date-time="vModel.created"
          :updated-date-time="vModel.updated"
        />
      </v-card>
    </v-bottom-sheet>

    <reset-password-dialog
      :dialog.sync="resetPasswordDialog"
      :admin-id="vModel.id"
      v-if="resetPasswordDialog"
    />
  </div>
</template>

<script lang="ts">
import { getApi, patchApi, postApi } from "@/utils/apis";
import DatetimePicker from "@/components/picker/DatetimePicker.vue";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";
import DialogTitle from "@/components/title/DialogTitle.vue";
import ResetPasswordDialog from "@/views/components/ResetPasswordDialog.vue";
import type { Admin } from "@/definitions/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import RoleSelections from "@/views/management/role/RoleSelections.vue";
import { debounce, DebouncedFunc } from "lodash-es";
import setupEditDialog from "@/composition/setupEditDialog";
import {
  computed,
  defineComponent,
  onBeforeMount,
  PropType,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";
import store from "@/store";

export default defineComponent({
  components: {
    RoleSelections,
    ButtonWithIcon,
    CreatedUpdatedBar,
    DialogTitle,
    DatetimePicker,
    ResetPasswordDialog,
  },
  props: {
    value: {
      type: Object as PropType<Admin>,
      required: true,
    },
    dialog: {
      required: true,
      type: Boolean,
    },
  },
  setup(props, { emit }) {
    const editDialog = setupEditDialog<Admin>(props, emit, "admins/");
    const state = reactive({
      saving: false,
      password: "",
      password2: "",
      show1: false,
      show2: false,
      resetPasswordDialog: false,
      existsLoginId: false,
      originalLoginId: "",
      checked: true,
    });
    const computes = {
      debounce: computed(
        (): DebouncedFunc<() => Promise<void>> =>
          debounce(methods.checkExistsLoginId, 500),
      ),
      noneWriteAuthority: computed(
        (): boolean =>
          (!store.getters.writeAuthority ||
            store.getters.roleId === editDialog.vModel.value.role.id ||
            !editDialog.vModel.value.role.available) &&
          !editDialog.isNew.value,
      ),
      loginIdErrorText: computed((): string[] | undefined => {
        if (!state.checked) {
          return undefined;
        }
        if (editDialog.vModel.value.loginId === state.originalLoginId) {
          return undefined;
        }
        return state.existsLoginId
          ? ["이미 사용중인 아이디 입니다."]
          : undefined;
      }),
      loginIdSuccessText: computed((): string[] | undefined => {
        if (!state.checked) {
          return undefined;
        }
        if (editDialog.vModel.value.loginId === state.originalLoginId) {
          return undefined;
        }
        return state.existsLoginId ? undefined : ["사용 가능한 아이디 입니다."];
      }),
      loginIdAppendIcon: computed((): string | unknown => {
        if (computes.loginIdSuccessText.value) {
          return "mdi-check-circle";
        }
        if (computes.loginIdErrorText.value) {
          return "mdi-alert-circle";
        }
        return undefined;
      }),
    };
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
        state.saving = true;
        const params = { ...editDialog.vModel.value, password: state.password };
        if (params.password) {
          params.password = pbkdf2
            .pbkdf2Sync(params.password, "salt", 1, 32, "sha512")
            .toString();
        }
        const response = await postApi<Admin>("admins/", params);
        state.saving = false;
        if (response.success) {
          await store.dispatch("reloadAdminCodes");
          editDialog.syncedDialog.value = false;
          emit("created", response.data);
        }
      },
      update: async (): Promise<void> => {
        state.saving = true;
        const params = { ...editDialog.vModel.value, password: state.password };
        if (params.password) {
          params.password = pbkdf2
            .pbkdf2Sync(params.password, "salt", 1, 32, "sha512")
            .toString();
        }
        const response = await patchApi<Admin>(
          `admins/${editDialog.vModel.value.id}`,
          params,
        );
        state.saving = false;
        if (response.success) {
          if (editDialog.vModel.value.id === store.getters.admin.id) {
            await store.dispatch("reIssueAccessToken");
          }
          await store.dispatch("reloadAdminCodes");
          editDialog.syncedDialog.value = false;
          emit("updated", response.data);
        }
      },
      debounceCheckExistsLoginId: (): void => {
        if (editDialog.vModel.value.loginId === state.originalLoginId) {
          return;
        }
        editDialog.loading.value = true;
        state.checked = false;
        computes.debounce.value();
      },
      checkExistsLoginId: async (): Promise<void> => {
        editDialog.loading.value = true;
        const response = await getApi<boolean>(
          `admins/exists-login-id?loginId=${editDialog.vModel.value.loginId}`,
        );
        editDialog.loading.value = false;
        state.checked = true;
        state.existsLoginId = response.data;
      },
    };
    onBeforeMount(() => {
      state.originalLoginId = editDialog.vModel.value.loginId;
    });
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return {
      ...editDialog,
      ...toRefs(state),
      ...computes,
      ...methods,
      observer,
    };
  },
});
</script>
