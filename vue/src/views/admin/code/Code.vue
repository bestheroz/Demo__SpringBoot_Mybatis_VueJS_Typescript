<template>
  <div>
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <code-group-list
            ref="codeGroupList"
            height="23vh"
            @select-row="onSelectRow"
          />
        </v-col>
        <v-col cols="12">
          <v-divider />
        </v-col>
        <v-col cols="12">
          <code-list :parent-item="item" height="36vh" />
        </v-col>
      </v-row>
    </v-card>
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

  get item(): TableCodeGroupEntity {
    return this.selected || Object.create(null);
  }

  protected mounted(): void {
    this.codeGroupList.getList();
  }

  onSelectRow(val: TableCodeGroupEntity) {
    this.selected = val;
  }
}
</script>
