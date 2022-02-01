<template>
  <div>
    <v-card class="text-center pa-1">
      <v-card-title
        class="justify-center text-h4 mb-2"
        v-text="envs.PRODUCT_TITLE"
      />
      <v-card-subtitle>Sign in to your account</v-card-subtitle>

      <!-- sign in form -->
      <v-card-text>
        <v-form ref="form">
          <ValidationObserver ref="observer">
            <ValidationProvider
              v-slot="{ errors, valid }"
              name="ID"
              rules="required"
            >
              <v-text-field
                v-model="loginId"
                :validate-on-blur="false"
                label="ID"
                name="loginId"
                outlined
                :error-messages="errors"
                :success="valid"
                @keyup.enter="submit"
                @input="resetErrors"
              />
            </ValidationProvider>
            <ValidationProvider
              v-slot="{ errors, valid }"
              name="Password"
              rules="required"
            >
              <v-text-field
                v-model="password"
                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                :type="showPassword ? 'text' : 'password'"
                label="Password"
                name="password"
                outlined
                :error-messages="errors"
                :success="valid"
                @input="resetErrors"
                @keyup.enter="submit"
                @click:append="showPassword = !showPassword"
              />
            </ValidationProvider>

            <v-btn
              :loading="loading"
              block
              x-large
              color="primary"
              @click="submit"
            >
              Sign In
            </v-btn>
            <div
              v-if="errorProvider"
              class="error--text"
              v-text="errorProviderMessages"
            />
          </ValidationObserver>
        </v-form>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import envs from "@/constants/envs";
import pbkdf2 from "pbkdf2";
import { ApiDataResult, axiosInstance } from "@/utils/apis";
import { toastCloseAll } from "@/utils/alerts";
import { defaultAdminConfig } from "@/definitions/defaults";
import { getYourConfig, routerReplace } from "@/utils/commands";
import { AxiosResponse } from "axios";
import { ValidationObserver } from "vee-validate";
import {
  computed,
  defineComponent,
  onBeforeMount,
  onMounted,
  reactive,
  ref,
  toRefs,
} from "@vue/composition-api";
import Vuetify from "@/plugins/vuetify";
import store from "@/store";

export default defineComponent({
  setup() {
    const state = reactive({
      loginId: "1",
      password: "1",
      loading: false,
      errorProvider: false,
      errorProviderMessages: "",
      showPassword: false,
    });
    const computes = {
      envs: computed((): typeof envs => envs),
    };
    const methods = {
      submit: async (): Promise<void> => {
        methods.resetErrors();
        const inValid = await observer.value?.validate();
        if (!inValid) {
          return;
        }

        const pbkdf2Password: string = pbkdf2
          .pbkdf2Sync(state.password || "", "salt", 1, 32, "sha512")
          .toString();
        state.loading = true;
        const response = await axiosInstance.post<
          { loginId: string; password: string },
          AxiosResponse<
            ApiDataResult<{
              accessToken: string;
              refreshToken: string;
            }>
          >
        >("api/sign-in", {
          loginId: state.loginId,
          password: pbkdf2Password,
        });
        state.loading = false;
        if (response.data.success && response.data.data) {
          await store.commit("saveToken", {
            accessToken: response.data.data.accessToken,
            refreshToken: response.data.data.refreshToken,
          });
          await store.dispatch("reloadRole");
          await store.commit(
            "setConfig",
            (await getYourConfig()) || defaultAdminConfig(),
          );
          await store.dispatch("reloadAdminCodes");
          toastCloseAll();
          await routerReplace("/");
        } else {
          state.errorProvider = true;
          state.errorProviderMessages = response.data.message;
        }
      },

      resetErrors(): void {
        state.errorProvider = false;
        state.errorProviderMessages = "";
      },
    };
    onBeforeMount(() => {
      Vuetify.framework.theme.dark = false;
    });
    onMounted(async () => {
      await store.commit("clearAdmin");
      await store.dispatch("reloadConfig");
      await store.commit("setRole", null);
      await store.commit("setAdminCodes", null);
      window.localStorage.clear();
      window.sessionStorage.clear();
    });
    const observer = ref<null | InstanceType<typeof ValidationObserver>>(null);
    return { ...toRefs(state), ...computes, ...methods, observer };
  },
});
</script>
