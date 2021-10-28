<template>
  <div>
    <v-card class="text-center pa-1">
      <v-card-title class="justify-center display-1 mb-2">Welcome</v-card-title>
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
                v-model="adminId"
                :validate-on-blur="false"
                label="ID"
                name="adminId"
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
            <div v-if="errorProvider" class="error--text">
              {{ errorProviderMessages }}
            </div>
          </ValidationObserver>
        </v-form>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";
import { ApiDataResult, axiosInstance } from "@/utils/apis";
import { toastCloseAll } from "@/utils/alerts";
import { defaultAdminConfig, defaultAdmin } from "@/definitions/defaults";
import { getYourConfig, routerReplace } from "@/utils/commands";
import { AxiosResponse } from "axios";

@Component({
  components: {},
})
export default class extends Vue {
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  adminId = "1";
  password = "1";

  dialog = false;
  loading = false;

  errorProvider = false;
  errorProviderMessages = "";

  // show password field
  showPassword = false;

  protected async created(): Promise<void> {
    this.$vuetify.theme.dark = false;
  }

  protected async mounted(): Promise<void> {
    await this.$store.commit("setAdmin", defaultAdmin());
    await this.$store.dispatch("reloadConfig");
    await this.$store.commit("setRole", null);
    await this.$store.commit("setAdminCodes", null);
    window.localStorage.clear();
  }

  protected async submit(): Promise<void> {
    this.resetErrors();
    const inValid = await this.observer.validate();
    if (!inValid) {
      return;
    }

    const pbkdf2Password: string = pbkdf2
      .pbkdf2Sync(this.password || "", "salt", 1, 32, "sha512")
      .toString();
    this.loading = true;
    const response = await axiosInstance.post<
      { adminId: string; password: string },
      AxiosResponse<
        ApiDataResult<{
          accessToken: string;
          refreshToken: string;
        }>
      >
    >("api/sign-in", {
      adminId: this.adminId,
      password: pbkdf2Password,
    });
    this.loading = false;
    if (response.data.code.startsWith("S") && response.data.data) {
      await this.$store.dispatch("saveToken", {
        accessToken: response.data.data.accessToken,
        refreshToken: response.data.data.refreshToken,
      });
      await this.$store.dispatch("reloadRole");
      await this.$store.commit(
        "setConfig",
        (await getYourConfig()) || defaultAdminConfig(),
      );
      await this.$store.dispatch("reloadAdminCodes");
      toastCloseAll();
      await routerReplace("/");
    } else {
      this.errorProvider = true;
      this.errorProviderMessages = response.data.message;
    }
  }
  protected resetErrors(): void {
    this.errorProvider = false;
    this.errorProviderMessages = "";
  }
}
</script>
