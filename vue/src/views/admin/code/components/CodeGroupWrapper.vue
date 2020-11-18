<template>
  <div>
    <v-card>
      <v-card-title class="datatable-title">
        <v-icon> mdi-format-list-checkbox </v-icon>
        코드 관리 - Master
      </v-card-title>
      <v-card-text class="pt-0 pb-1">
        <button-set
          add-button
          delete-button
          reload-button
          :delete-disabled="!syncedSelected || syncedSelected.length === 0"
          @click:add="addItem"
          @click:delete="deleteItem"
          @click:reload="reloadList"
        />
        <code-group-list
          ref="refList"
          :selected.sync="syncedSelected"
          :height="height"
          @row-id-clicked="editItem"
        />
        <code-group-edit-dialog
          ref="refEditDialog"
          :item="item"
          :dialog.sync="dialog"
          @finished="reloadList"
        />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Prop, PropSync, Ref, Vue } from "vue-property-decorator";
import type { TableCodeGroupEntity } from "@/common/types";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import CodeGroupEditDialog from "@/views/admin/code/components/CodeGroupEditDialog.vue";
import CodeGroupList from "@/views/admin/code/components/CodeGroupList.vue";

@Component({
  name: "CodeGroupWrapper",
  components: {
    CodeGroupList,
    ButtonSet,
    CodeGroupEditDialog,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  @PropSync("selected") syncedSelected!: TableCodeGroupEntity[];
  @Ref("refList") refList!: CodeGroupList;
  @Ref("refEditDialog") refEditDialog!: CodeGroupEditDialog;

  dialog = false;
  item: TableCodeGroupEntity = Object.create(null);

  reloadList(): void {
    this.refList.getList();
  }

  addItem(): void {
    this.item = Object.create(null);
    this.dialog = true;
  }

  editItem(value: TableCodeGroupEntity): void {
    this.item = { ...value };
    this.dialog = true;
  }

  deleteItem(): void {
    this.item = this.syncedSelected[0];
    this.refEditDialog.delete();
  }
}
</script>
