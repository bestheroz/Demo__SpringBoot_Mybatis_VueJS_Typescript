<template>
  <div>
    <v-row no-gutters>
      <v-col cols="12">
        <menu-list ref="menuList" height="80vh" />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from "vue-property-decorator";
import MenuList from "@/views/admin/menu/components/MenuList.vue";

@Component({
  name: "Menu",
  components: {
    MenuList,
  },
})
export default class extends Vue {
  @Ref() readonly menuList!: MenuList;

  protected mounted(): void {
    this.menuList.getList();
  }
}
</script>
