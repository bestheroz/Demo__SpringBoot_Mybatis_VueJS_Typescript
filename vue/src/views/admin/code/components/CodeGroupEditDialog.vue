<template>
  <div>
    <modal
      name="CodeGroupEditDialog"
      draggable
      width="50%"
      height="auto"
      :shiftX="0.4"
      :shiftY="0.1"
      :clickToClose="false"
    >
      <v-card>
        <v-card-title class="py-2 modal-header">
          <v-icon v-if="isNew">mdi-pencil-plus-outline</v-icon>
          <v-icon v-else>mdi-pencil-outline</v-icon>
          코드그룹 {{ isNew ? "추가" : "수정" }}
          <v-spacer />
          <v-btn text small :ripple="false" style="cursor: default">
            <v-icon> mdi-cursor-move</v-icon>
          </v-btn>
          <v-btn text small @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12" md="6">
                <ValidationProvider
                  name="그룹코드"
                  rules="required|max:32"
                  v-slot="{ errors }"
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
                  name="그룹코드명"
                  rules="required|max:100"
                  v-slot="{ errors }"
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
        <v-divider />
        <v-card-actions class="py-1">
          <v-spacer />
          <v-btn text @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
            닫기
          </v-btn>
          <v-btn text @click="save" :loading="loading">
            <v-icon> mdi-content-save-settings-outline</v-icon>
            저장
          </v-btn>
        </v-card-actions>
      </v-card>
    </modal>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Vue, Watch } from "vue-property-decorator";
import { TableCodeGroupEntity } from "@/common/types";
import { deleteApi, patchApi, postApi } from "@/utils/apis";
import { confirmDelete } from "@/utils/alerts";
import { ValidationObserver } from "vee-validate";

@Component({
  name: "CodeGroupEditDialog",
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly item!: TableCodeGroupEntity;

  readonly ENDPOINT_URL = "admin/codeGroups/";
  loading = false;
  isNew = false;

  beforeDestroy() {
    this.syncedDialog = false;
  }

  mounted() {}

  @Watch("syncedDialog", { immediate: true })
  watchDialog(val: boolean) {
    if (val) {
      this.isNew = !this.item.codeGroup;
      this.$refs.observer &&
        (this.$refs.observer as InstanceType<
          typeof ValidationObserver
        >).reset();
      this.$modal.show("CodeGroupEditDialog");
    } else {
      this.$modal.hide("CodeGroupEditDialog");
    }
  }

  async save() {
    const isValid = await (this.$refs.observer as InstanceType<
      typeof ValidationObserver
    >).validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.patch();
  }

  async create() {
    this.loading = true;
    const response = await postApi<TableCodeGroupEntity>(
      this.ENDPOINT_URL,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  async patch() {
    this.loading = true;
    const response = await patchApi<TableCodeGroupEntity>(
      `${this.ENDPOINT_URL}${this.item.codeGroup}/`,
      this.item,
    );
    this.loading = false;
    if (response?.code?.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  async delete() {
    const result = await confirmDelete();
    if (result.value) {
      this.loading = true;
      const response = await deleteApi<TableCodeGroupEntity>(
        `${this.ENDPOINT_URL}${this.item.codeGroup}/`,
      );
      this.loading = false;
      if (response?.code?.startsWith("S")) {
        this.$emit("finished");
      }
    }
  }
}
</script>
