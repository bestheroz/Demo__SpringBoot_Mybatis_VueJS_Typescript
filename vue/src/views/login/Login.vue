<template>
  <v-container id="login" class="fill-height" tag="section">
    <v-row justify="center">
      <v-card min-width="25vw" width="25vw" class="elevation-12">
        <v-toolbar color="primary" dark flat>
          <v-toolbar-title>Login at demo</v-toolbar-title>
          <v-spacer />
          <template #heading>
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
              v-slot="{ errors, valid }"
              name="ID"
              rules="required"
            >
              <v-text-field
                v-model="id"
                label="ID..."
                :error-messages="errors"
                :success="valid"
                @keyup.enter="login"
              />
            </ValidationProvider>
            <ValidationProvider
              v-slot="{ errors, valid }"
              name="Password"
              rules="required"
            >
              <v-text-field
                v-model="password"
                label="Password..."
                :error-messages="errors"
                :success="valid"
                :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                :type="show1 ? 'text' : 'password'"
                @keyup.enter="login"
                @click:append="show1 = !show1"
              />
            </ValidationProvider>
            <v-btn
              large
              color="primary"
              outlined
              :loading="loading"
              @click="login"
            >
              <v-icon class="pr-2">mdi-login</v-icon>
              Let's Go
            </v-btn>
          </v-card-text>
          <v-system-bar color="secondary" class="text-center d-block" dark>
            테스트 계정 ==> 1 / 1
          </v-system-bar>
        </ValidationObserver>
      </v-card>
    </v-row>
    <new-password-dialog :id="id" :dialog.sync="dialog" v-if="dialog" />
  </v-container>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import {
  alertAxiosError,
  ApiDataResult,
  axiosInstance,
  getVariableApi,
} from "@/utils/apis";
import NewPasswordDialog from "@/views/login/components/NewPasswordDialog.vue";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";
import { toastCloseAll, toastError } from "@/utils/alerts";

@Component({ name: "Login", components: { NewPasswordDialog } })
export default class extends Vue {
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  id: string | null = null;
  password: string | null = null;
  show1 = false;
  title: string | null = null;
  loading = false;
  dialog = false;

  protected async created(): Promise<void> {
    this.title = await getVariableApi("title");
    this.$vuetify.theme.dark = false;
  }

  protected async mounted(): Promise<void> {
    await this.$store.dispatch("clearUser");
    await this.$store.dispatch("clearDrawer");
    await this.$store.dispatch("clearCache");
    await this.$store.dispatch("clearMenuSelected");
    window.localStorage.clear();
    if (this.$route.query.login === "need") {
      toastError("로그인이 필요합니다.");
    }
  }

  protected async login(): Promise<void> {
    const inValid = await this.observer.validate();
    if (!inValid) {
      return;
    }
    try {
      const pbkdf2Password: string = pbkdf2
        .pbkdf2Sync(this.password || "", "salt", 1, 32, "sha512")
        .toString();
      this.loading = true;
      const response = await axiosInstance.post<
        ApiDataResult<{
          accessToken: string;
          refreshToken: string;
        }>
      >("api/auth/login", {
        id: this.id,
        password: pbkdf2Password,
      });
      this.loading = false;
      if (response?.data?.code === "S002") {
        this.dialog = true;
        this.password = null;
      } else if (
        response?.data?.code?.startsWith("S") &&
        response?.data?.data
      ) {
        await this.$store.dispatch("saveToken", {
          accessToken: response.data.data.accessToken,
          refreshToken: response.data.data.refreshToken,
        });
        toastCloseAll();
        await this.$router.push("/");
      } else {
        toastError(response?.data?.message);
      }
    } catch (e) {
      alertAxiosError(e);
    }
  }
}
</script>
