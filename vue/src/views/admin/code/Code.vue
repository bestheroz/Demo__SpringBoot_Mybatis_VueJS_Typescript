<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <code-group-list
          ref="codeGroupList"
          height="25vh"
          @select-row="onSelectRow"
        />
      </v-col>
      <v-col cols="12">
        <v-divider />
      </v-col>
      <v-col cols="12">
        <code-list :code-group="codeGroup" height="36vh" />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import type { TableCodeGroupEntity } from "@/common/types";
import CodeList from "@/views/admin/code/components/CodeList.vue";
import CodeGroupList from "@/views/admin/code/components/CodeGroupList.vue";

@Component({
  name: "Code",
  components: {
    CodeGroupList,
    CodeList,
  },
})
export default class extends Vue {
  @Ref() readonly codeGroupList!: CodeGroupList;
  selected: TableCodeGroupEntity = Object.create(null);

  get codeGroup(): string {
    return this.selected?.codeGroup;
  }

  protected mounted(): void {
    this.codeGroupList.getList();
  }

  onSelectRow(val: TableCodeGroupEntity): void {
    this.selected = val;
  }
}
</script>
