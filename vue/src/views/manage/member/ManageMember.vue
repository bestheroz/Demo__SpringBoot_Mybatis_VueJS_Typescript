<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="listData"
      sort-by="calories"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar color="white" flat>
          <v-toolbar-title>Manage Member</v-toolbar-title>
          <v-divider class="mx-4" inset vertical />
          <div class="flex-grow-1" />
          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on }">
              <v-btn class="mb-2" color="primary" dark v-on="on">
                New Item
              </v-btn>
            </template>
            <v-card>
              <v-card-title>
                <span class="headline">{{ formTitle }}</span>
              </v-card-title>

              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12" md="4" sm="6">
                      <v-text-field
                        v-model="editedItem.name"
                        label="Dessert name"
                      />
                    </v-col>
                    <v-col cols="12" md="4" sm="6">
                      <v-text-field
                        v-model="editedItem.calories"
                        label="Calories"
                      />
                    </v-col>
                    <v-col cols="12" md="4" sm="6">
                      <v-text-field v-model="editedItem.fat" label="Fat (g)" />
                    </v-col>
                    <v-col cols="12" md="4" sm="6">
                      <v-text-field
                        v-model="editedItem.carbs"
                        label="Carbs (g)"
                      />
                    </v-col>
                    <v-col cols="12" md="4" sm="6">
                      <v-text-field
                        v-model="editedItem.protein"
                        label="Protein (g)"
                      />
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <div class="flex-grow-1" />
                <v-btn color="blue darken-1" text @click="close">
                  Cancel
                </v-btn>
                <v-btn color="blue darken-1" text @click="save">
                  Save
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:item.action="{ item }">
        <v-icon small class="mr-2" @click="editItem(item)">
          edit
        </v-icon>
        <v-icon small @click="deleteItem(item)">
          delete
        </v-icon>
      </template>
      <template v-slot:item.expireDt="{ item }">
        {{ $moment(item.expireDt).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template v-slot:no-data>
        <v-btn color="primary" @click="getList">
          Reset
        </v-btn>
      </template>
    </v-data-table>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { Member } from "@/views/manage/member/common/types";
import { getPagingListApi } from "@/utils/api";
@Component({})
export default class ManageMember extends Vue {
  dialog: boolean = false;
  headers: Object[] = [];
  listData: Member[] = [];
  editedIndex: number = -1;
  editedItem: Member = {
    memberId: "",
    memberPw: "",
    memberNm: "",
    memberTyp: "",
    loginFailCnt: 0,
    isClosed: false,
    expireDt: new Date()
  };

  created() {
    this.headers = [
      {
        text: "회원 아이디",
        align: "left",
        sortable: false,
        value: "memberId"
      },
      { text: "회원 명", value: "memberNm" },
      { text: "회원 타입", value: "memberTyp" },
      { text: "로그인 실패 건수", value: "loginFailCnt" },
      { text: "계정 잠김 여부", value: "isClosed" },
      { text: "계정 만료 일시", value: "expireDt", sortable: false }
    ];
    this.getList();
  }

  get formTitle() {
    return this.editedIndex === -1 ? "New Item" : "Edit Item";
  }

  @Watch("dialog", { immediate: true, deep: true })
  onPersonChanged1(val: boolean) {
    val || this.close();
  }

  async getList() {
    const result = await getPagingListApi<Member[]>(`/sample/admin/member/`);
    console.log(result);
    this.listData = result.responseData;
  }

  editItem(item: Member) {
    this.editedIndex = this.listData.indexOf(item);
    this.editedItem = Object.assign({}, item);
    this.dialog = true;
  }

  deleteItem(item: Member) {
    const index = this.listData.indexOf(item);
    confirm("Are you sure you want to delete this item?") &&
      this.listData.splice(index, 1);
  }

  close() {
    this.dialog = false;
  }

  save() {
    if (this.editedIndex > -1) {
      Object.assign(this.listData[this.editedIndex], this.editedItem);
    } else {
      this.listData.push(this.editedItem);
    }
    this.close();
  }
}
</script>

<style scoped></style>
