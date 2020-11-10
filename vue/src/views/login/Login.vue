<template>
  <v-container id="login" class="fill-height" tag="section">
    <v-row justify="center">
      <v-slide-y-transition appear>
        <v-card :min-width="600" width="40%" class="elevation-12">
          <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Login at {{ title }}</v-toolbar-title>
            <v-spacer />
            <template v-slot:heading>
              <div class="text-center">
                <h1 class="display-2 font-weight-bold">
                  <v-icon>mdi-lock-outline</v-icon>
                  Login
                </h1>
              </div>
            </template>
          </v-toolbar>

          <ValidationObserver ref="observer">
            <v-card-text class="text-center">
              <ValidationProvider
                name="ID"
                rules="required"
                v-slot="{ errors, valid }"
              >
                <v-text-field
                  v-model="id"
                  label="ID..."
                  prepend-icon="mdi-key"
                  @keyup.enter="login"
                  :error-messages="errors"
                  :success="valid"
                />
              </ValidationProvider>
              <ValidationProvider
                name="Password"
                rules="required"
                v-slot="{ errors, valid }"
              >
                <v-text-field
                  v-model="password"
                  label="Password..."
                  prepend-icon="mdi-lock-outline"
                  @keyup.enter="login"
                  :error-messages="errors"
                  :success="valid"
                  :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="show1 ? 'text' : 'password'"
                  @click:append="show1 = !show1"
                />
              </ValidationProvider>
              <v-divider />
              <v-alert
                border="top"
                colored-border
                color="divider"
                dense
                class="mb-0"
              >
                테스트 계정 ==> 1 / 1
              </v-alert>
              <v-btn
                large
                color="button-default"
                text
                outlined
                rounded
                :loading="loading"
                @click="login"
              >
                <v-icon class="pr-2">mdi-login</v-icon>
                Let's Go
              </v-btn>
            </v-card-text>
          </ValidationObserver>
        </v-card>
      </v-slide-y-transition>
    </v-row>
    <new-password-dialog :id="id" :dialog.sync="dialog" />
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import {
  alertAxiosError,
  ApiDataResult,
  axiosInstance,
  getVariableApi,
} from "@/utils/apis";
import { alertError } from "@/utils/alerts";
import NewPasswordDialog from "@/views/login/components/NewPasswordDialog.vue";
import { saveToken } from "@/utils/authentications";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";

@Component({ name: "Login", components: { NewPasswordDialog } })
export default class extends Vue {
  id: string | null = null;
  password: string | null = null;
  show1 = false;
  title: string | null = null;
  loading = false;
  dialog = false;

  async mounted(): void {
    await this.$store.dispatch("clearUser");
    await this.$store.dispatch("clearDrawer");
    await this.$store.dispatch("clearCache");
    window.localStorage.clear();
    if (this.$route.query.login === "need") {
      this.$toasted.error("로그인이 필요합니다.");
    }
    this.title = await getVariableApi("title");
  }

  async login(): void {
    const inValid = await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
    if (!inValid) {
      return;
    }
    this.loading = true;
    try {
      const pbkdf2Password: string = pbkdf2
        .pbkdf2Sync(this.password || "", "salt", 1, 32, "sha512")
        .toString();
      const response = await axiosInstance.post<
        ApiDataResult<{
          accessToken: string;
          refreshToken: string;
        }>
      >("api/auth/login", {
        id: this.id,
        password: pbkdf2Password,
      });
      if (response?.data?.code === "S002") {
        this.dialog = true;
        this.password = null;
      } else if (response?.data?.code?.startsWith("S")) {
        saveToken({
          accessToken: response?.data?.data?.accessToken,
          refreshToken: response?.data?.data?.refreshToken,
        });
        this.$toasted.clear();
        await this.$router.push("/");
      } else {
        alertError(response?.data?.message);
      }
    } catch (e) {
      alertAxiosError(e);
    }
    this.loading = false;
  }
}
</script>
