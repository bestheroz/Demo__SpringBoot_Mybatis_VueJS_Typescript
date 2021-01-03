<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="100%" width="60vw">
      <v-card>
        <v-card-title class="py-2 modal-header">
          <v-icon v-if="isNew">mdi-pencil-plus-outline</v-icon>
          <v-icon v-else>mdi-pencil-outline</v-icon>
          사용자 {{ isNew ? "추가" : "수정" }}
          <v-spacer />

          <v-btn text small @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="사용자아이디"
                  rules="required|max:50"
                >
                  <v-text-field
                    v-model="item.id"
                    label="*사용자아이디"
                    :counter="50"
                    :error-messages="errors"
                    :disabled="!isNew"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
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
              <v-col cols="12" md="4">
                <v-switch
                  v-model="item.available"
                  :label="item.available | getSwitchLabel"
                />
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="권한"
                  rules="required"
                >
                  <v-select
                    v-if="AUTHORITY"
                    v-model.number="item.authority"
                    :items="
                      AUTHORITY.map((code) => {
                        return { value: parseInt(code.value), text: code.text };
                      })
                    "
                    label="*권한"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <datetime-picker
                  v-model="item.expired"
                  label="만료일"
                  full-width
                />
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-if="isNew"
                  v-slot="{ errors }"
                  name="비밀번호"
                  vid="password"
                  rules="max:20"
                >
                  <v-text-field
                    v-model="item.password"
                    label="비밀번호"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show1 ? 'text' : 'password'"
                    @click:append="show1 = !show1"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-if="isNew"
                  v-slot="{ errors }"
                  name="비밀번호 확인"
                  rules="required|confirmed:password|max:20"
                >
                  <v-text-field
                    v-model="password2"
                    label="*비밀번호 확인"
                    :counter="20"
                    :error-messages="errors"
                    :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
                    :type="show2 ? 'text' : 'password'"
                    clearable
                    @click:append="show2 = !show2"
                  />
                </ValidationProvider>
                <v-btn
                  v-if="!isNew"
                  color="warning"
                  outlined
                  @click="resetPassword"
                >
                  비밀번호 초기화
                </v-btn>
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
import { Component, Prop, PropSync, Vue, Watch } from "vue-property-decorator";
import type { SelectItem, TableMemberEntity } from "@/common/types";
import { getCodesApi, patchApi, postApi } from "@/utils/apis";
import DatetimePicker from "@/components/picker/DatetimePicker.vue";
import { ValidationObserver } from "vee-validate";
import pbkdf2 from "pbkdf2";

@Component({
  name: "MemberEditDialog",
  components: { DatetimePicker },
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly item!: TableMemberEntity;

  loading = false;
  AUTHORITY: SelectItem[] = [];
  password2: string | null = null;
  show1 = false;
  show2 = false;
  isNew = false;

  protected async created(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  @Watch("syncedDialog", { immediate: true })
  protected watchDialog(val: boolean): void {
    if (val) {
      this.password2 = "";
      this.show1 = false;
      this.show2 = false;
      this.isNew = !this.item.id;
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
    this.isNew ? await this.create() : await this.patch();
  }

  protected async create(): Promise<void> {
    this.loading = true;
    const params = { ...this.item };
    if (params.password) {
      params.password = pbkdf2
        .pbkdf2Sync(params.password, "salt", 1, 32, "sha512")
        .toString();
    }
    const response = await postApi<TableMemberEntity>("admin/members/", params);
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initMemberCodes");
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  protected async patch(): Promise<void> {
    this.loading = true;
    const params = { ...this.item };
    if (params.password) {
      params.password = pbkdf2
        .pbkdf2Sync(params.password, "salt", 1, 32, "sha512")
        .toString();
    }
    const response = await patchApi<TableMemberEntity>(
      `admin/members/${this.item.id}/`,
      params,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      if (this.item.id === this.$store.getters.user.id) {
        await this.$store.dispatch("reissueAccessToken");
      }
      await this.$store.dispatch("initMemberCodes");
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  protected async resetPassword(): Promise<void> {
    this.loading = true;
    await postApi<TableMemberEntity>(
      `admin/members/${this.item.id}/resetPassword`,
      this.item,
    );
    this.loading = false;
  }
}
</script>
