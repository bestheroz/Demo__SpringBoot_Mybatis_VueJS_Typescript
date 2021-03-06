<template>
  <div>
    <v-dialog v-model="syncedDialog" max-width="100%" width="25vw">
      <v-card>
        <dialog-title text="내 정보 변경">
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
                  color="warning"
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
        <dialog-action-button
          :loading="loading"
          @click:save="save"
          @click:close="syncedDialog = false"
        />
      </v-card>
    </v-dialog>
    <change-password-dialog
      :dialog.sync="newPasswordDialog"
      v-if="newPasswordDialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Ref, Vue } from "vue-property-decorator";
import type { TableMemberEntity } from "@/common/types";
import { getApi, patchApi } from "@/utils/apis";
import DatetimePicker from "@/components/picker/DatetimePicker.vue";
import ChangePasswordDialog from "@/components/layout/components/ChangePasswordDialog.vue";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import DialogTitle from "@/components/title/DialogTitle.vue";
import DialogActionButton from "@/components/button/DialogActionButton.vue";
import { defaultTableMemberEntity } from "@/common/values";

@Component({
  name: "EditMeDialog",
  components: {
    DialogActionButton,
    DialogTitle,
    ButtonIconTooltip,
    ChangePasswordDialog,
    DatetimePicker,
  },
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  item: TableMemberEntity = defaultTableMemberEntity();
  loading = false;
  show1 = false;
  newPasswordDialog = false;

  protected async created(): Promise<void> {
    const response = await getApi<TableMemberEntity>("members/mine");
    this.item = response?.data || defaultTableMemberEntity();
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
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
