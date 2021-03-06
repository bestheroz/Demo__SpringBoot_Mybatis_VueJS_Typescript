<template>
  <div>
    <v-dialog v-model="syncedDialog" max-width="100%" width="25vw">
      <v-card>
        <dialog-title text="비밀번호 변경">
          <template #buttons>
            <button-icon-tooltip
              icon="mdi-window-close"
              text="닫기"
              @click="syncedDialog = false"
              top
            />
          </template>
        </dialog-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row dense>
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
        <dialog-action-button
          :loading="loading"
          @click:save="save"
          @click:close="syncedDialog = false"
        />
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Ref, Vue, Watch } from "vue-property-decorator";
import { postApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";
import DialogTitle from "@/components/title/DialogTitle.vue";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import DialogActionButton from "@/components/button/DialogActionButton.vue";

@Component({
  name: "ChangePasswordDialog",
  components: { DialogActionButton, ButtonIconTooltip, DialogTitle },
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  loading = false;
  oldPassword = "";
  password = "";
  password2 = "";
  show1 = false;
  show2 = false;
  show3 = false;

  @Watch("syncedDialog", { immediate: true })
  protected watchDialog(val: boolean): void {
    if (val) {
      // pass
    } else {
      this.observer.reset();
      this.oldPassword = "";
      this.password = "";
      this.password2 = "";
      this.show1 = false;
      this.show2 = false;
      this.show3 = false;
    }
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const response = await postApi<{
      oldPassword: string;
      newPassword: string;
    }>("members/mine/changePassword/", {
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
