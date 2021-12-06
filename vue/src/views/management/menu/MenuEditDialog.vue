<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable>
      <v-card class="pb-4">
        <dialog-title
          :is-new="isNew"
          prefix="메뉴"
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <v-form :readonly="!$store.getters.writeAuthority">
            <ValidationObserver ref="observer">
              <v-row>
                <v-col cols="12" md="3">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="메뉴명"
                    rules="required|max:50"
                  >
                    <v-text-field
                      v-model="vModel.name"
                      label="메뉴명"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
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
                      v-model="vModel.type"
                      :items="MenuTypes"
                      label="타입"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="6" v-if="vModel.type !== MENU_TYPE.GROUP">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="링크 URL"
                    rules="max:255"
                  >
                    <v-text-field
                      v-model="vModel.url"
                      label="링크 URL"
                      :counter="255"
                      :error-messages="errors"
                      clearable
                    />
                  </ValidationProvider>
                </v-col>
                <v-col v-if="vModel.type === MENU_TYPE.GROUP" cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="메뉴 아이콘"
                    rules="max:50|required"
                  >
                    <v-text-field
                      v-model="vModel.icon"
                      label="메뉴 아이콘"
                      append-icon="mdi-open-in-new"
                      :counter="50"
                      :error-messages="errors"
                      @click:append="linkIconSite"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col v-if="vModel.type === MENU_TYPE.GROUP" cols="12" md="1">
                  <v-icon v-text="vModel.icon" size="3.5rem" />
                </v-col>
              </v-row>
            </ValidationObserver>
            <button-with-icon
              block
              text="저장"
              icon="mdi-content-save"
              :loading="loading"
              @click="save"
              v-if="$store.getters.writeAuthority"
            />
          </v-form>
        </v-card-text>
        <created-updated-bar
          :created-date-time="vModel.created"
          :updated-date-time="vModel.updated"
        />
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Ref, VModel, Vue } from "vue-property-decorator";
import { postApi, putApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogTitle from "@/components/title/DialogTitle.vue";
import type { Menu } from "@/definitions/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import { MENU_TYPE, MenuTypes } from "@/definitions/selections";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";
import { routerToNewTab } from "@/utils/commands";

@Component({
  components: {
    ButtonWithIcon,
    CreatedUpdatedBar,
    DialogTitle,
  },
})
export default class extends Vue {
  @VModel({ required: true }) vModel!: Menu;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  loading = false;
  readonly MenuTypes = MenuTypes;
  readonly MENU_TYPE = MENU_TYPE;

  get isNew(): boolean {
    return !this.vModel.id;
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }
    this.isNew ? await this.create() : await this.update();
  }

  protected async create(): Promise<void> {
    this.loading = true;
    const response = await postApi<Menu>("menus/", this.vModel);
    this.loading = false;
    if (response.success) {
      this.$store.dispatch("reloadRole").then();
      this.syncedDialog = false;
      this.$emit("created", response.data);
    }
  }

  protected async update(): Promise<void> {
    this.loading = true;
    const response = await putApi<Menu>(`menus/${this.vModel.id}`, this.vModel);
    this.loading = false;
    if (response.success) {
      this.$store.dispatch("reloadRole").then();
      this.syncedDialog = false;
      this.$emit("updated", response.data);
    }
  }

  protected linkIconSite(): void {
    routerToNewTab("https://pictogrammers.github.io/@mdi/font/6.4.95/");
  }
}
</script>
