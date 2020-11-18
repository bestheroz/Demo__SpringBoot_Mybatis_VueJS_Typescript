<template>
  <div>
    <v-card>
      <v-card-title class="datatable-title">
        <v-icon> mdi-format-list-checkbox </v-icon>
        코드 관리 - Detail
      </v-card-title>
      <v-card-text class="pt-0 pb-1">
        <button-set
          :disabled="!parentItem.codeGroup"
          add-button
          delete-button
          reload-button
          :delete-disabled="!selected || selected.length === 0"
          @click:add="addItem"
          @click:delete="deleteItem"
          @click:reload="reloadList"
        />
        <code-list
          ref="refList"
          :parent-item="parentItem"
          :selected.sync="selected"
          :height="height"
          @row-id-clicked="editItem"
        />
        <code-edit-dialog
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
import { Component, Prop, Ref, Vue } from "vue-property-decorator";
import type { TableCodeEntity, TableCodeGroupEntity } from "@/common/types";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import CodeEditDialog from "@/views/admin/code/components/CodeEditDialog.vue";
import DataTableFilter from "@/components/datatable/DataTableFilter.vue";
import CodeList from "@/views/admin/code/components/CodeList.vue";

@Component({
  name: "CodeWrapper",
  components: {
    CodeList,
    DataTableFilter,
    CodeEditDialog,
    ButtonSet,
  },
})
export default class extends Vue {
  @Prop({ required: true }) readonly height!: number | string;
  @Prop({ required: true }) readonly parentItem!: TableCodeGroupEntity;
  @Ref("refList") readonly refList!: CodeList;
  @Ref("refEditDialog") readonly refEditDialog!: CodeEditDialog;

  selected: TableCodeEntity[] = [];
  dialog = false;
  item: TableCodeEntity = Object.create(null);

  reloadList(): void {
    this.refList.getList();
  }

  addItem(): void {
    this.item = { codeGroup: this.parentItem.codeGroup };
    this.dialog = true;
  }

  editItem(value: TableCodeEntity): void {
    this.item = { ...value };
    this.dialog = true;
  }

  deleteItem(): void {
    this.item = this.selected[0];
    this.refEditDialog.delete();
  }
}
</script>
