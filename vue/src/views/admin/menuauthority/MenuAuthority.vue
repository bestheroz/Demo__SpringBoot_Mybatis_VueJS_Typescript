<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <v-card flat>
          <v-card-text>
            <v-select
              v-model="authority"
              label="권한 선택"
              :loading="loading"
              :items="AUTHORITY"
              dense
              hide-details
            />
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12">
        <menu-authority-list :authority="authority" height="75vh" />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import type { SelectItem } from "@/common/types";
import { getCodesApi } from "@/utils/apis";
import MenuAuthorityList from "@/views/admin/menuauthority/components/MenuAuthorityList.vue";

@Component({
  name: "MenuAuthority",
  components: {
    MenuAuthorityList,
  },
})
export default class extends Vue {
  authority: string | null = null;
  loading = false;
  AUTHORITY: SelectItem[] = [];

  protected mounted(): void {
    this.getCodeList();
  }

  protected async getCodeList(): Promise<void> {
    this.loading = true;
    const data: SelectItem[] = await getCodesApi("AUTHORITY");
    this.loading = false;
    this.AUTHORITY =
      data.filter(
        (value) =>
          ![this.$store.getters.user.authority, 999].includes(
            parseInt(value.value),
          ),
      ) || [];
  }
}
</script>
