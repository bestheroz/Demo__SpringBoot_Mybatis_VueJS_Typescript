<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <v-card flat>
          <v-card-text>
            <v-chip-group
              v-model="authority"
              column
              active-class="accent"
              mandatory
              dense
            >
              <v-chip
                v-for="item in AUTHORITY"
                :value="item.value"
                filter
                outlined
                :key="item.value"
              >
                {{ item.text }}
              </v-chip>
            </v-chip-group>
          </v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12">
        <member-menu-list
          :authority="authority"
          height="70vh"
          v-if="authority"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { SelectItem } from "@/common/types";
import MemberMenuList from "@/views/admin/member/menu/components/MemberMenuList.vue";
import { getCodesApi } from "@/utils/apis";

@Component({
  name: "MemberMenu",
  components: {
    MemberMenuList,
  },
})
export default class extends Vue {
  authority: string | null = null;
  AUTHORITY: SelectItem[] = [];

  protected async created(): Promise<void> {
    const data: SelectItem[] = await getCodesApi("AUTHORITY");
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
