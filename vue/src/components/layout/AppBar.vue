<template>
  <div>
    <v-app-bar v-if="!isPopup" dense app clipped-left color="secondary">
      <v-app-bar-nav-icon @click.stop="syncedDrawer = !syncedDrawer" />
      <v-toolbar-title>
        <v-btn text @click="goHome">
          <v-icon class="mr-2"> mdi-home </v-icon>
          {{ title || "" }}
        </v-btn>
      </v-toolbar-title>
      <v-spacer />
      <v-menu open-on-hover bottom offset-y>
        <template #activator="{ on }">
          <v-btn x-large text v-on="on">
            <v-icon> mdi-account</v-icon>
            {{ $store.getters.user.name }}
          </v-btn>
        </template>

        <v-list dense>
          <v-list-item>
            <v-list-item-title>
              <v-btn block text @click="editMeDialog = true">
                <v-icon>mdi-account-edit-outline</v-icon>
                내 정보수정
              </v-btn>
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              <v-btn block text @click="$store.dispatch('logout')">
                <v-icon>mdi-logout</v-icon>
                Logout
              </v-btn>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
    <edit-me-dialog :dialog.sync="editMeDialog" v-if="editMeDialog" />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue } from "vue-property-decorator";
import { getVariableApi } from "@/utils/apis";
import EditMeDialog from "@/components/layout/components/EditMeDialog.vue";

@Component({
  name: "AppBar",
  components: { EditMeDialog },
})
export default class extends Vue {
  @PropSync("drawer", { required: true, default: true }) syncedDrawer!: boolean;
  title: string | null = null;
  editMeDialog = false;

  get isPopup(): boolean {
    return !window.toolbar.visible;
  }

  protected async created(): Promise<void> {
    this.title = await getVariableApi("title");
  }

  protected goHome(): void {
    this.$router.currentRoute.path !== "/" && this.$router.push("/");
  }
}
</script>
