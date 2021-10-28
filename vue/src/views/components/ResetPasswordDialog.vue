<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable max-width="25vw">
      <v-card class="pb-4">
        <dialog-title
          text="비밀번호 초기화"
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
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
                    :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="showPassword ? 'text' : 'password'"
                    @click:append="showPassword = !showPassword"
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
                    v-model="passwordCheck"
                    label="비밀번호 확인"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="showPasswordCheck ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="showPasswordCheck ? 'text' : 'password'"
                    @keyup.enter="resetPassword"
                    @click:append="showPasswordCheck = !showPasswordCheck"
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
            @click="resetPassword"
          />
        </v-card-text>
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Ref, Prop, Vue } from "vue-property-decorator";
import { patchApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";
import DialogTitle from "@/components/title/DialogTitle.vue";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import { Admin } from "@/definitions/models";

@Component({
  components: { ButtonWithIcon, DialogTitle },
})
export default class extends Vue {
  @Prop({}) readonly adminId!: number;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  loading = false;
  password = "";
  passwordCheck = "";
  showPassword = false;
  showPasswordCheck = false;

  protected async resetPassword(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }

    this.loading = true;

    const password = pbkdf2
      .pbkdf2Sync(this.password, "salt", 1, 32, "sha512")
      .toString();

    await patchApi<Admin>(`admins/${this.adminId}/reset-password`, password);
    this.loading = false;
    this.syncedDialog = false;
  }
}
</script>
