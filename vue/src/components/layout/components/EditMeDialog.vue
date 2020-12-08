<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="100%" width="25vw">
      <v-card>
        <v-card-title class="py-2 modal-header">
          내 정보 수정
          <v-spacer />

          <v-btn text small @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="item.id"
                  label="사용자아이디"
                  :counter="50"
                  disabled
                />
              </v-col>
              <v-col cols="12">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="사용자명"
                  rules="required|max:100"
                >
                  <v-text-field
                    v-model="item.name"
                    label="*사용자명"
                    :counter="100"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="7">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="비빌번호"
                  rules="required|max:20"
                >
                  <v-text-field
                    v-model="item.password"
                    label="*비빌번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="5" class="text-right pt-7">
                <v-btn
                  color="button-edit"
                  outlined
                  small
                  @click="newPasswordDialog = true"
                >
                  비밀번호 변경
                </v-btn>
              </v-col>
              <v-col cols="12">
                <datetime-picker
                  v-model="item.created"
                  disabled
                  label="가입일"
                />
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
    <change-password-dialog
      :dialog.sync="newPasswordDialog"
      v-if="newPasswordDialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from "vue-property-decorator";
import type { TableMemberEntity } from "@/common/types";
import { getApi, patchApi } from "@/utils/apis";
import DatetimePicker from "@/components/picker/DatetimePicker.vue";
import ChangePasswordDialog from "@/components/layout/components/ChangePasswordDialog.vue";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";

@Component({
  name: "EditMeDialog",
  components: { ChangePasswordDialog, DatetimePicker },
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;

  item: TableMemberEntity = Object.create(null);
  loading = false;
  show1 = false;
  newPasswordDialog = false;

  protected beforeDestroy(): void {
    this.syncedDialog = false;
    this.$nextTick(() => {
      this.syncedDialog = false;
    });
  }

  @Watch("syncedDialog", { immediate: true })
  protected async watchDialog(val: boolean): Promise<void> {
    if (val) {
      this.show1 = false;
      const response = await getApi<TableMemberEntity>("members/mine");
      this.item = response?.data || Object.create(null);
      this.$refs.observer &&
        (this.$refs.observer as InstanceType<
          typeof ValidationObserver
        >).reset();
    }
  }

  protected async save(): Promise<void> {
    const isValid = await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const payload = { ...this.item };
    payload.password = pbkdf2
      .pbkdf2Sync(this.item.password || "", "salt", 1, 32, "sha512")
      .toString();
    const response = await patchApi<TableMemberEntity>("members/mine", payload);
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("reissueAccessToken");
      await this.$store.dispatch("initMemberCodes");
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }
}
</script>
