<template>
  <div>
    <v-card flat :loading="loading">
      <v-card-text class="py-0">
        <v-chip-group v-model="vModel" column mandatory dens>
          <v-chip
            v-for="item in roles"
            :value="item"
            filter
            :outlined="vModel.id !== item.id"
            :color="item.id === $store.getters.roleId ? 'orange' : 'primary'"
            :key="item.id"
            v-text="item.name"
            class="px-4"
            label
          />
        </v-chip-group>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, VModel, Vue } from "vue-property-decorator";
import { getApi } from "@/utils/apis";
import type { Role } from "@/definitions/models";
import { defaultRole } from "@/definitions/defaults";

@Component({
  components: {},
})
export default class extends Vue {
  @VModel({ required: true, default: () => defaultRole() }) vModel!: Role;
  roles: Role[] = [];
  loading = false;

  protected async created(): Promise<void> {
    this.loading = true;
    const response = await getApi<Role[]>("mine/roles/selections/");
    this.roles = (response.data || []).filter((r) => r.id !== 1);
    this.loading = false;
  }
}
</script>
