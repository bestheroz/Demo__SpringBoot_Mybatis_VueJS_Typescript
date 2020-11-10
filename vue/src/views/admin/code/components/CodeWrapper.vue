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
          :delete-disabled="!syncedSelected || syncedSelected.length === 0"
          @click:add="addItem"
          @click:delete="deleteItem"
          @click:reload="reloadList"
        />
        <code-list
          ref="refList"
          :parent-item="parentItem"
          :selected.sync="syncedSelected"
          @row-id-clicked="editItem"
          :height="height"
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
import { Component, Prop, PropSync, Vue } from "vue-property-decorator";
import { TableCodeEntity, TableCodeGroupEntity } from "@/common/types";
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
  @PropSync("selected") syncedSelected!: TableCodeGroupEntity[];
  dialog = false;
  item: TableCodeEntity = Object.create(null);

  reloadList() {
    this.$refs.refList && (this.$refs.refList as any).getList();
  }

  addItem() {
    this.item = { codeGroup: this.parentItem.codeGroup };
    this.dialog = true;
  }

  editItem(value: TableCodeEntity) {
    this.item = { ...value };
    this.dialog = true;
  }

  deleteItem() {
    this.item = this.syncedSelected[0];
    (this.$refs.refEditDialog as any).delete();
  }
}
</script>
