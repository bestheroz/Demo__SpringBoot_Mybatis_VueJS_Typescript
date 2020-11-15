<template>
  <div>
    <button-set
      add-button
      @click:add="addItem"
      delete-button
      :delete-disabled="!selected || selected.length === 0"
      @click:delete="deleteItem"
      reload-button
      @click:reload="reloadList"
    />
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <member-list
            ref="refList"
            height="74vh"
            :selected.sync="selected"
            @row-id-clicked="editItem"
          />
        </v-col>
      </v-row>
    </v-card>
    <member-edit-dialog
      ref="refEditDialog"
      :item="item"
      :dialog.sync="dialog"
      @finished="reloadList"
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import MemberList from "@/views/admin/member/components/MemberList.vue";
import MemberEditDialog from "@/views/admin/member/components/MemberEditDialog.vue";
import { TableMemberEntity } from "@/common/types";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import dayjs from "dayjs";

@Component({
  name: "Member",
  components: { ButtonSet, MemberEditDialog, MemberList },
})
export default class extends Vue {
  dialog = false;
  item: TableMemberEntity = { expired: null };
  selected: TableMemberEntity[] = [];

  reloadList() {
    this.$refs.refList && (this.$refs.refList as any).getList();
  }

  addItem() {
    this.item = {
      expired: dayjs().add(1, "year").toDate(),
    };
    this.dialog = true;
  }

  editItem(value: TableMemberEntity) {
    this.item = value;
    this.dialog = true;
  }

  deleteItem() {
    this.item = this.selected[0];
    (this.$refs.refEditDialog as any).delete();
  }
}
</script>
