<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable>
      <v-card class="pb-4">
        <dialog-title
          v-model="vModel.available"
          :is-new="isNew"
          prefix="코드"
          with-switch
          @click:close="syncedDialog = false"
        />
        <v-card-text>
          <v-form :readonly="!$store.getters.writeAuthority">
            <ValidationObserver ref="observer">
              <v-row>
                <v-col cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="그룹 코드"
                    rules="max:50|required"
                  >
                    <v-text-field
                      v-model="vModel.type"
                      label="그룹 코드"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="코드"
                    rules="max:50|required"
                  >
                    <v-text-field
                      v-model="vModel.value"
                      label="코드"
                      :counter="50"
                      :error-messages="errors"
                      class="required"
                    />
                  </ValidationProvider>
                </v-col>
                <v-col cols="12" md="4">
                  <ValidationProvider
                    v-slot="{ errors }"
                    name="코드명"
                    rules="max:100|required"
                  >
                    <v-text-field
                      v-model="vModel.text"
                      label="코드명"
                      :counter="100"
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
import type { Code } from "@/definitions/models";
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
  @VModel({ required: true }) vModel!: Code;
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
    this.isNew ? await this.create() : await this.put();
  }

  protected async create(): Promise<void> {
    this.loading = true;
    const response = await postApi<Code>("codes/", this.vModel);
    this.loading = false;
    if (response.success) {
      this.syncedDialog = false;
      this.$emit("created", response.data);
    }
  }

  protected async put(): Promise<void> {
    this.loading = true;
    const response = await putApi<Code>(`codes/${this.vModel.id}`, this.vModel);
    this.loading = false;
    if (response.success) {
      this.syncedDialog = false;
      window.localStorage.removeItem(`code__${this.vModel.type}`);
      this.$emit("updated", response.data);
    }
  }
}
</script>
