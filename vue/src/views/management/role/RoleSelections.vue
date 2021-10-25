<template>
  <div>
    <v-select
      v-model="vModel"
      :items="items"
      item-text="name"
      :label="label"
      :error-messages="errorMessages"
      :loading="loading"
      :clearable="clearable"
      return-object
      :class="required ? 'required' : undefined"
      :disabled="disabled"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, VModel, Vue } from "vue-property-decorator";
import { getApi } from "@/utils/apis";
import type { Role } from "@/definitions/models";
import { defaultRole } from "@/definitions/defaults";

@Component({
  components: {},
})
export default class extends Vue {
  @VModel({ required: true, default: () => defaultRole() }) vModel!: Role;
  @Prop() readonly errorMessages!: string[];
  @Prop({ default: "역할" }) readonly label!: string;
  @Prop({ type: Boolean }) readonly clearable!: boolean;
  @Prop({ type: Boolean }) readonly required!: boolean;
  @Prop({ type: Boolean }) readonly disabled!: boolean;
  @Prop() readonly paramAvailable?: boolean;
  items: Role[] = [];
  loading = false;

  protected async created(): Promise<void> {
    this.loading = true;
    const response = await getApi<Role[]>(
      `roles/selections/?available=${
        !this.disabled && this.paramAvailable !== undefined
          ? this.paramAvailable
          : ""
      }`,
    );
    this.items = response.data || [];
    this.loading = false;
  }
}
</script>
