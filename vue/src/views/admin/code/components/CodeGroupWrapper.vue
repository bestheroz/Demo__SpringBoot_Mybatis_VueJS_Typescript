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
          @row-id-clicked="editItem"
          :height="height"
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
import { Component, Prop, PropSync, Vue } from "vue-property-decorator";
import { TableCodeGroupEntity } from "@/common/types";
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
  dialog = false;
  item: TableCodeGroupEntity = Object.create(null);

  reloadList() {
    this.$refs.refList && (this.$refs.refList as any).getList();
  }

  addItem() {
    this.item = Object.create(null);
    this.dialog = true;
  }

  editItem(value: TableCodeGroupEntity) {
    this.item = { ...value };
    this.dialog = true;
  }

  deleteItem() {
    this.item = this.syncedSelected[0];
    (this.$refs.refEditDialog as any).delete();
  }
}
</script>
