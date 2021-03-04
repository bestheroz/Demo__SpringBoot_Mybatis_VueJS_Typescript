<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <member-list ref="memberList" />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import MemberList from "@/views/admin/member/components/MemberList.vue";

@Component({
  name: "Member",
  components: { MemberList },
})
export default class extends Vue {
  @Ref() readonly memberList!: MemberList;

  protected mounted(): void {
    this.memberList.getList();
  }
}
</script>
