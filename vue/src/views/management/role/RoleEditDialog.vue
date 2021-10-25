<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable>
      <v-card class="pb-4">
        <dialog-title
          v-model="vModel.available"
          :is-new="isNew"
          prefix="역할"
          with-switch
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <v-form :readonly="!$store.getters.writeAuthority">
            <ValidationObserver ref="observer">
              <v-row>
                <v-col cols="12" md="3">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="역할명"
                    rules="required|max:50"
                  >
                    <v-text-field
                      v-model="vModel.name"
                      label="역할명"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
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
import type { Role } from "@/definitions/models";
import CreatedUpdatedBar from "@/components/history/CreatedUpdatedBar.vue";
import ButtonWithIcon from "@/components/button/ButtonWithIcon.vue";

@Component({
  components: {
    ButtonWithIcon,
    CreatedUpdatedBar,
    DialogTitle,
  },
})
export default class extends Vue {
  @VModel({ required: true }) vModel!: Role;
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  loading = false;

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
    const response = await postApi<Role>(
      `roles/?parentId=${
        this.$store.getters.isSuperAdmin ? "" : this.$store.getters.roleId
      }`,
      this.vModel,
    );
    this.loading = false;
    if (response.code.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("created", response.data);
    }
  }

  protected async update(): Promise<void> {
    this.loading = true;
    const response = await putApi<Role>(`roles/${this.vModel.id}`, this.vModel);
    this.loading = false;
    if (response.code.startsWith("S")) {
      this.syncedDialog = false;
      this.$emit("updated", response.data);
    }
  }
}
</script>
