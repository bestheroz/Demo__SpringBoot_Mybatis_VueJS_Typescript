<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="100%" width="25vw">
      <v-card>
        <v-card-title class="py-2 modal-header">
          비밀번호 변경
          <v-spacer />
          <v-btn text small @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
          </v-btn>
        </v-card-title>
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
                    label="*이전 비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
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
                    label="*비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show2 ? 'text' : 'password'"
                    @click:append="show2 = !show2"
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
                    label="*비밀번호 확인"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show3 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show3 ? 'text' : 'password'"
                    @keyup.enter="save"
                    @click:append="show3 = !show3"
                  />
                </ValidationProvider>
              </v-col>
            </v-row>
          </ValidationObserver>
        </v-card-text>
        <v-divider />
        <v-card-actions class="py-1">
          <v-spacer />
          <v-btn text @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
            닫기
          </v-btn>
          <v-btn text :loading="loading" @click="save">
            <v-icon> mdi-content-save-settings-outline</v-icon>
            저장
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from "vue-property-decorator";
import { postApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";

@Component({
  name: "ChangePasswordDialog",
  components: {},
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;

  readonly ENDPOINT_URL: string = "members/";
  loading = false;
  oldPassword = "";
  password = "";
  password2 = "";
  show1 = false;
  show2 = false;
  show3 = false;

  beforeDestroy(): void {
    this.syncedDialog = false;
  }

  @Watch("syncedDialog")
  watchDialog(val: boolean): void {
    if (val) {
      this.oldPassword = "";
      this.password = "";
      this.password2 = "";
      this.show1 = false;
      this.show2 = false;
      this.show3 = false;
      this.$refs.observer &&
        (this.$refs.observer as InstanceType<
          typeof ValidationObserver
        >).reset();
    }
  }

  async save(): Promise<void> {
    const isValid = await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const response = await postApi<{
      oldPassword: string;
      newPassword: string;
    }>(`${this.ENDPOINT_URL}mine/changePassword/`, {
      oldPassword: pbkdf2
        .pbkdf2Sync(this.oldPassword, "salt", 1, 32, "sha512")
        .toString(),
      newPassword: pbkdf2
        .pbkdf2Sync(this.password, "salt", 1, 32, "sha512")
        .toString(),
    });
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }
}
</script>
