<template>
  <div>
    <v-dialog v-model="syncedDialog" max-width="100%" width="60vw">
      <v-card>
        <dialog-title :is-new="isNew" prefix="코드그룹">
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
              <v-col cols="12" md="6">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="그룹코드"
                  rules="required|max:32"
                >
                  <v-text-field
                    v-model="item.codeGroup"
                    label="*그룹코드"
                    :counter="32"
                    :error-messages="errors"
                    :disabled="!isNew"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="6">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="그룹코드명"
                  rules="required|max:100"
                >
                  <v-text-field
                    v-model="item.name"
                    label="그룹코드명"
                    :counter="100"
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
import type { TableCodeGroupEntity } from "@/common/types";
import { postApi, putApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogActionButton from "@/components/button/DialogActionButton.vue";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import DialogTitle from "@/components/title/DialogTitle.vue";

@Component({
  name: "CodeGroupEditDialog",
  components: { DialogTitle, ButtonIconTooltip, DialogActionButton },
})
export default class extends Vue {
  @VModel({ required: true }) item!: TableCodeGroupEntity;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  loading = false;

  get isNew(): boolean {
    return !this.item.codeGroup;
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.put();
  }

  protected async create(): Promise<void> {
    this.loading = true;
    const response = await postApi<TableCodeGroupEntity>(
      "admin/code/groups/",
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  protected async put(): Promise<void> {
    this.loading = true;
    const response = await putApi<TableCodeGroupEntity>(
      `admin/code/groups/${this.item.codeGroup}/`,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }
}
</script>
