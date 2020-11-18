<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="100%" width="60vw">
      <v-card>
        <v-card-title class="py-2 modal-header">
          <v-icon v-if="isNew">mdi-pencil-plus-outline</v-icon>
          <v-icon v-else>mdi-pencil-outline</v-icon>
          코드 {{ isNew ? "추가" : "수정" }}
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
                  name="그룹 코드"
                  rules="required"
                >
                  <v-text-field
                    v-model="item.codeGroup"
                    label="*그룹 코드"
                    disabled
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
              <v-col cols="0" md="4" />
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="상세 코드"
                  rules="max:50|required"
                >
                  <v-text-field
                    v-model="item.code"
                    label="*상세 코드"
                    :counter="50"
                    :error-messages="errors"
                    :disabled="!isNew"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="상세 코드명"
                  rules="max:100"
                >
                  <v-text-field
                    v-model="item.name"
                    label="상세 코드명"
                    :counter="100"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
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
                      AUTHORITY.map((item) => {
                        return { value: parseInt(item.value), text: item.text };
                      })
                    "
                    label="*권한"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="4">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="정렬순서"
                  rules="required|numeric"
                >
                  <v-text-field
                    v-model="item.displayOrder"
                    label="*정렬순서"
                    :error-messages="errors"
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
import { Component, Prop, PropSync, Vue, Watch } from "vue-property-decorator";
import type { SelectItem, TableCodeEntity } from "@/common/types";
import { deleteApi, getCodesApi, patchApi, postApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import { ValidationObserver } from "vee-validate";

@Component({
  name: "CodeEditDialog",
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly item!: TableCodeEntity;

  readonly ENDPOINT_URL = "admin/codes/";
  AUTHORITY: SelectItem[] = [];
  isNew = false;
  loading = false;

  beforeDestroy(): void {
    this.syncedDialog = false;
  }

  async beforeMount(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  @Watch("syncedDialog", { immediate: true })
  watchDialog(val: boolean): void {
    if (val) {
      this.isNew = !this.item.code;
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
    this.isNew ? await this.create() : await this.patch();
  }

  async create(): Promise<void> {
    this.loading = true;
    const response = await postApi<TableCodeEntity>(
      `${this.ENDPOINT_URL}${this.item.codeGroup}`,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  async patch(): Promise<void> {
    this.loading = true;
    const response = await patchApi<TableCodeEntity>(
      `${this.ENDPOINT_URL}${this.item.codeGroup}/${this.item.code}/`,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      window.localStorage.removeItem(`code__${this.item.codeGroup}`);
      this.$emit("finished");
    }
  }

  async delete(): Promise<void> {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteApi<TableCodeEntity>(
        `${this.ENDPOINT_URL}${this.item.codeGroup}/${this.item.code}/`,
      );
      this.loading = false;
      if (response?.code?.startsWith("S")) {
        window.localStorage.removeItem(`code__${this.item.codeGroup}`);
        this.$emit("finished");
      }
    }
  }
}
</script>
