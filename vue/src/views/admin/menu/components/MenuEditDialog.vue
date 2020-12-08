<template>
  <div>
    <v-dialog v-model="syncedDialog" persistent max-width="100%" width="60vw">
      <v-card>
        <v-card-title class="py-2 modal-header">
          <v-icon v-if="isNew">mdi-pencil-plus-outline</v-icon>
          <v-icon v-else>mdi-pencil-outline</v-icon>
          메뉴 {{ isNew ? "추가" : "수정" }}
          <v-spacer />

          <v-btn text small @click="syncedDialog = false">
            <v-icon> mdi-window-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12" md="3">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="메뉴명"
                  rules="required|max:50"
                >
                  <v-text-field
                    v-model="item.name"
                    label="*메뉴명"
                    :counter="50"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="3">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="타입"
                  rules="required"
                >
                  <v-select
                    v-model="item.type"
                    :items="filterMenuType(item)"
                    label="*타입"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="6">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="링크 URL"
                  rules="max:255"
                >
                  <v-text-field
                    v-model="item.url"
                    label="링크 URL"
                    :counter="255"
                    :disabled="item.type === 'G'"
                    :error-messages="errors"
                    clearable
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12" md="2">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="메뉴 순서"
                  rules="required|numeric"
                >
                  <v-text-field
                    v-model="item.displayOrder"
                    label="*메뉴 순서"
                    :error-messages="errors"
                  />
                </ValidationProvider>
              </v-col>
              <v-col v-if="item.level === 2" cols="12" md="4">
                <v-text-field v-model="item.icon" label="메뉴 아이콘" />
              </v-col>
              <v-col v-if="item.level === 2" cols="12" md="1">
                <v-icon> {{ item.icon }}</v-icon>
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
          <v-btn text :saving="saving" @click="save">
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
import type { SelectItem, TableMenuEntity } from "@/common/types";
import { getCodesApi, patchApi, postApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";

interface MenuVO extends TableMenuEntity {
  level: number;
}

@Component({
  name: "MenuEditDialog",
})
export default class extends Vue {
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({ required: true }) readonly item!: MenuVO;

  readonly ENDPOINT_URL = "admin/menus/";
  saving = false;
  MENU_TYPE: SelectItem[] = [];
  isNew = false;

  protected beforeDestroy(): void {
    this.syncedDialog = false;
    this.$nextTick(() => {
      this.syncedDialog = false;
    });
  }

  protected async beforeMount(): Promise<void> {
    this.MENU_TYPE = await getCodesApi("MENU_TYPE");
  }

  @Watch("syncedDialog")
  protected watchDialog(val: boolean): void {
    if (val) {
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
    this.saving = true;
    const response = await postApi<TableMenuEntity>(
      this.ENDPOINT_URL,
      this.item,
    );
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initDrawers");
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  protected async patch(): Promise<void> {
    this.saving = true;
    const response = await patchApi<TableMenuEntity>(
      `${this.ENDPOINT_URL}${this.item.id}/`,
      this.item,
    );
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initDrawers");
      this.syncedDialog = false;
      this.$emit("finished");
    }
  }

  protected filterMenuType(item: MenuVO): SelectItem[] {
    if (item.parentId !== 1) {
      return this.MENU_TYPE.filter((item) => item.value !== "G");
    }
    return this.MENU_TYPE;
  }
}
</script>
