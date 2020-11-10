<template>
  <div>
    <v-card>
      <button-set reload-button @click:reload="reloadList" />
      <v-row no-gutters>
        <v-col cols="12">
          <menu-list
            ref="refList"
            height="80vh"
            @add-item="addItem"
            @edit-item="editItem"
            @delete-item="deleteItem"
          />
        </v-col>
      </v-row>
    </v-card>
    <menu-edit-dialog
      ref="refEditDialog"
      :item="item"
      :dialog.sync="dialog"
      @finished="reloadList"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import MenuList from "@/views/admin/menu/components/MenuList.vue";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import MenuEditDialog from "@/views/admin/menu/components/MenuEditDialog.vue";
import { TableMenuEntity } from "@/common/types";

@Component({
  name: "Menu",
  components: {
    MenuEditDialog,
    ButtonSet,
    MenuList,
  },
})
export default class extends Vue {
  item: TableMenuEntity = Object.create(null);
  dialog = false;

  reloadList() {
    this.$refs.refList && (this.$refs.refList as any).getList();
  }

  addItem(value: TableMenuEntity) {
    this.item = {
      parentId: value.id,
    };
    this.dialog = true;
  }

  editItem(value: TableMenuEntity) {
    this.item = { ...value };
    this.dialog = true;
  }

  deleteItem(value: TableMenuEntity) {
    this.item = { ...value };
    (this.$refs.refEditDialog as any).delete();
  }
}
</script>
