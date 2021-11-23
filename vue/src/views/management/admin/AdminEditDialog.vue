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
                      v-model="vModel.adminId"
                      label="관리자 아이디"
                      :counter="50"
                      :error-messages="adminIdErrorText || errors"
                      :success-messages="adminIdSuccessText"
                      :append-icon="adminIdAppendIcon"
                      class="required"
                      :loading="loading"
                      @input="debounceCheckExistsAdminId"
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
                !adminIdSuccessText && originalAdminId !== vModel.adminId
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
import { Component, PropSync, Ref, VModel, Vue } from "vue-property-decorator";
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

@Component({
  components: {
    RoleSelections,
    ButtonWithIcon,
    CreatedUpdatedBar,
    DialogTitle,
    DatetimePicker,
    ResetPasswordDialog,
  },
})
export default class extends Vue {
  @VModel({ required: true }) vModel!: Admin;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  saving = false;
  password = "";
  password2 = "";
  show1 = false;
  show2 = false;
  resetPasswordDialog = false;
  existsAdminId = false;
  originalAdminId = "";
  checked = true;
  loading = false;

  readonly debounce: DebouncedFunc<() => Promise<void>> = debounce(
    this.checkExistsAdminId,
    500,
  );

  get isNew(): boolean {
    return !this.vModel.id;
  }

  get noneWriteAuthority(): boolean {
    return (
      (!this.$store.getters.writeAuthority ||
        this.$store.getters.roleId === this.vModel.role.id ||
        !this.vModel.role.available) &&
      !this.isNew
    );
  }

  get adminIdErrorText(): string[] | undefined {
    if (!this.checked) {
      return undefined;
    }
    if (this.vModel.adminId === this.originalAdminId) {
      return undefined;
    }
    return this.existsAdminId ? ["이미 사용중인 아이디 입니다."] : undefined;
  }
  get adminIdSuccessText(): string[] | undefined {
    if (!this.checked) {
      return undefined;
    }
    if (this.vModel.adminId === this.originalAdminId) {
      return undefined;
    }
    return this.existsAdminId ? undefined : ["사용 가능한 아이디 입니다."];
  }

  get adminIdAppendIcon(): string | unknown {
    if (this.adminIdSuccessText) {
      return "mdi-check-circle";
    }
    if (this.adminIdErrorText) {
      return "mdi-alert-circle";
    }
    return undefined;
  }

  protected created(): void {
    this.originalAdminId = this.vModel.adminId;
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.update();
  }

  protected async create(): Promise<void> {
    this.saving = true;
    const params = { ...this.vModel, password: this.password };
    if (params.password) {
      params.password = pbkdf2
        .pbkdf2Sync(params.password, "salt", 1, 32, "sha512")
        .toString();
    }
    const response = await postApi<Admin>("admins/", params);
    this.saving = false;
    if (response.success) {
      await this.$store.dispatch("reloadAdminCodes");
      this.syncedDialog = false;
      this.$emit("created", response.data);
    }
  }

  protected async update(): Promise<void> {
    this.saving = true;
    const params = { ...this.vModel, password: this.password };
    if (params.password) {
      params.password = pbkdf2
        .pbkdf2Sync(params.password, "salt", 1, 32, "sha512")
        .toString();
    }
    const response = await patchApi<Admin>(`admins/${this.vModel.id}`, params);
    this.saving = false;
    if (response.success) {
      if (this.vModel.id === this.$store.getters.admin.id) {
        await this.$store.dispatch("reIssueAccessToken");
      }
      await this.$store.dispatch("reloadAdminCodes");
      this.syncedDialog = false;
      this.$emit("updated", response.data);
    }
  }

  protected debounceCheckExistsAdminId(): void {
    if (this.vModel.adminId === this.originalAdminId) {
      return;
    }
    this.loading = true;
    this.checked = false;
    this.debounce();
  }

  protected async checkExistsAdminId(): Promise<void> {
    this.loading = true;
    const response = await getApi<boolean>(
      `admins/exists-admin-id?adminId=${this.vModel.adminId}`,
    );
    this.loading = false;
    this.checked = true;
    this.existsAdminId = response.data;
  }
}
</script>
