<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="listData"
      sort-by="calories"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar
          color="white"
          flat
        >
          <v-toolbar-title>Manage Member</v-toolbar-title>
          <v-divider
            class="mx-4"
            inset
            vertical
          />
          <div class="flex-grow-1" />
          <v-dialog
            max-width="500px"
            v-model="dialog"
          >
            <template v-slot:activator="{ on }">
              <v-btn
                class="mb-2"
                color="primary"
                dark
                v-on="on"
              >
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
                    <v-col
                      cols="12"
                      md="4"
                      sm="6"
                    >
                      <v-text-field
                        label="Dessert name"
                        v-model="editedItem.name"
                      />
                    </v-col>
                    <v-col
                      cols="12"
                      md="4"
                      sm="6"
                    >
                      <v-text-field
                        label="Calories"
                        v-model="editedItem.calories"
                      />
                    </v-col>
                    <v-col
                      cols="12"
                      md="4"
                      sm="6"
                    >
                      <v-text-field
                        label="Fat (g)"
                        v-model="editedItem.fat"
                      />
                    </v-col>
                    <v-col
                      cols="12"
                      md="4"
                      sm="6"
                    >
                      <v-text-field
                        label="Carbs (g)"
                        v-model="editedItem.carbs"
                      />
                    </v-col>
                    <v-col
                      cols="12"
                      md="4"
                      sm="6"
                    >
                      <v-text-field
                        label="Protein (g)"
                        v-model="editedItem.protein"
                      />
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <div class="flex-grow-1" />
                <v-btn
                  @click="close"
                  color="blue darken-1"
                  text
                >
                  Cancel
                </v-btn>
                <v-btn
                  @click="save"
                  color="blue darken-1"
                  text
                >
                  Save
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>
      <template v-slot:item.action="{ item }">
        <v-icon
          small
          class="mr-2"
          @click="editItem(item)"
        >
          edit
        </v-icon>
        <v-icon
          small
          @click="deleteItem(item)"
        >
          delete
        </v-icon>
      </template>
      <template v-slot:item.expireDt="{ item }">
        {{ $moment(item.expireDt).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template v-slot:no-data>
        <v-btn
          @click="getList"
          color="primary"
        >
          Reset
        </v-btn>
      </template>
    </v-data-table>
  </div>
</template>

<script>
export default {
  name: 'ManageMember',
  components: {},
  data: () => ({
    dialog: false,
    headers: [
      {
        text: '회원 아이디',
        align: 'left',
        sortable: false,
        value: 'memberId'
      },
      { text: '회원 명', value: 'memberNm' },
      { text: '회원 타입', value: 'memberTyp' },
      { text: '로그인 실패 건수', value: 'loginFailCnt' },
      { text: '계정 잠김 여부', value: 'closeYn' },
      { text: '계정 만료 일시', value: 'expireDt', sortable: false }
    ],
    listData: [],
    editedIndex: -1,
    editedItem: {
      name: '',
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0
    },
    defaultItem: {
      name: '',
      calories: 0,
      fat: 0,
      carbs: 0,
      protein: 0
    }
  }),

  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'New Item' : 'Edit Item'
    }
  },

  watch: {
    dialog (val) {
      val || this.close()
    }
  },

  created () {
    this.getList()
  },

  methods: {
    async getList () {
      const result = await this.axios.get(
        `${this.apiURL}/sample/admin/member/`
      );
      console.log(result);
      this.listData = result.data.responseData
    },

    editItem (item) {
      this.editedIndex = this.desserts.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true
    },

    deleteItem (item) {
      const index = this.desserts.indexOf(item);
      confirm('Are you sure you want to delete this item?') &&
        this.desserts.splice(index, 1)
    },

    close () {
      this.dialog = false;
      setTimeout(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1
      }, 300)
    },

    save () {
      if (this.editedIndex > -1) {
        Object.assign(this.desserts[this.editedIndex], this.editedItem)
      } else {
        this.desserts.push(this.editedItem)
      }
      this.close()
    }
  }
}
</script>

<style scoped></style>
