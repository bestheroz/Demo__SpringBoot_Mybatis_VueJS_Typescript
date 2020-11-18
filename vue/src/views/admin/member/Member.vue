<template>
  <div>
    <button-set
      add-button
      delete-button
      :delete-disabled="!selected || selected.length === 0"
      reload-button
      @click:add="addItem"
      @click:delete="deleteItem"
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
import { Component, Ref, Vue } from "vue-property-decorator";
import MemberList from "@/views/admin/member/components/MemberList.vue";
import MemberEditDialog from "@/views/admin/member/components/MemberEditDialog.vue";
import type { TableMemberEntity } from "@/common/types";
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

  @Ref("refEditDialog") readonly refEditDialog!: MemberEditDialog;
  @Ref("refList") readonly refList!: MemberList;

  reloadList(): void {
    this.refList.getList();
  }

  addItem(): void {
    this.item = {
      expired: dayjs().add(1, "year").toDate(),
    };
    this.dialog = true;
  }

  editItem(value: TableMemberEntity): void {
    this.item = value;
    this.dialog = true;
  }

  deleteItem(): void {
    this.item = this.selected[0];
    this.refEditDialog.delete();
  }
}
</script>
