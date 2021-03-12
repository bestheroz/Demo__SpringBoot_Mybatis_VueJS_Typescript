<template>
  <div>
    <v-dialog v-model="syncedDialog" max-width="100%" width="60vw">
      <v-card>
        <dialog-title :is-new="isNew" prefix="코드">
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
import { Component, PropSync, Ref, VModel, Vue } from "vue-property-decorator";
import type { SelectItem, TableCodeEntity } from "@/common/types";
import { getCodesApi, postApi, putApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import DialogTitle from "@/components/title/DialogTitle.vue";
import DialogActionButton from "@/components/button/DialogActionButton.vue";

@Component({
  name: "CodeEditDialog",
  components: { DialogActionButton, DialogTitle, ButtonIconTooltip },
})
export default class extends Vue {
  @VModel({ required: true }) item!: TableCodeEntity;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  AUTHORITY: SelectItem[] = [];
  loading = false;

  async created(): Promise<void> {
    this.AUTHORITY = await getCodesApi("AUTHORITY");
  }

  get isNew(): boolean {
    return !this.item.code;
  }

  async save(): Promise<void> {
    const isValid = this.observer.validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.put();
  }

  async create(): Promise<void> {
    this.loading = true;
    const response = await postApi<TableCodeEntity>(
      `admin/codes/${this.item.codeGroup}`,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  async put(): Promise<void> {
    this.loading = true;
    const response = await putApi<TableCodeEntity>(
      `admin/codes/${this.item.codeGroup}/${this.item.code}/`,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      window.localStorage.removeItem(`code__${this.item.codeGroup}`);
      this.$emit("finished");
    }
  }
}
</script>
