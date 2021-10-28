<template>
  <div>
    <v-bottom-sheet v-model="syncedDialog" inset scrollable max-width="40vw">
      <v-card class="pb-4">
        <dialog-title text="내 정보" @click:close="syncedDialog = false" />
        <v-card-text>
          <ValidationObserver ref="observer">
            <v-row>
              <v-col cols="12">
                <v-text-field
                  :value="item.adminId"
                  label="나의 아이디"
                  disabled
                />
              </v-col>
              <v-col cols="12">
                <ValidationProvider
                  v-slot="{ errors }"
                  name="나의 이름"
                  rules="required|max:100"
                >
                  <v-text-field
                    v-model="item.name"
                    label="나의 이름"
                    :counter="100"
                    :error-messages="errors"
                    class="required"
                  />
                </ValidationProvider>
              </v-col>
              <v-col cols="12">
                <v-text-field
                  :value="item.role.name"
                  label="나의 역할"
                  disabled
                />
              </v-col>
            </v-row>
          </ValidationObserver>
          <button-with-icon
            block
            text="저장"
            icon="mdi-content-save"
            :loading="loading"
            @click="save"
          />
        </v-card-text>
        <created-updated-bar
          :created-date-time="item.created"
          :updated-date-time="item.updated"
        />
      </v-card>
    </v-bottom-sheet>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Ref, Vue } from "vue-property-decorator";
import { getApi, patchApi } from "@/utils/apis";
import { ValidationObserver } from "vee-validate";
import DialogTitle from "@/components/title/DialogTitle.vue";
import { defaultAdmin } from "@/definitions/defaults";
import type { Admin } from "@/definitions/models";
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
  @PropSync("dialog", { required: true, type: Boolean }) syncedDialog!: boolean;
  @Prop({}) readonly adminPassword!: string;
  @Ref("observer") readonly observer!: InstanceType<typeof ValidationObserver>;

  item: Admin = defaultAdmin();
  loading = false;

  protected async created(): Promise<void> {
    const response = await getApi<Admin>("mine");
    this.item = response.data || defaultAdmin();
  }

  protected async save(): Promise<void> {
    const isValid = await this.observer.validate();
    if (!isValid) {
      return;
    }

    this.loading = true;
    const payload = { ...this.item, password: this.adminPassword };
    const response = await patchApi<Admin>("mine", payload);
    this.loading = false;
    if (response.code.startsWith("S")) {
      await this.$store.dispatch("reIssueAccessToken");
      await this.$store.dispatch("reloadAdminCodes");
      this.syncedDialog = false;
    }
  }
}
</script>
