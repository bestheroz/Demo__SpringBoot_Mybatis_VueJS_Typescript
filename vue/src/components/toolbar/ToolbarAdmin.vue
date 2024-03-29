<template>
  <div>
    <v-menu offset-y left transition="slide-y-transition">
      <template #activator="{ on }">
        <v-btn icon class="elevation-2" v-on="on">
          <v-badge color="success" dot bordered offset-x="10" offset-y="10">
            <v-avatar size="40">
              <v-img src="/images/avatars/avatar1.svg"></v-img>
            </v-avatar>
          </v-badge>
        </v-btn>
      </template>

      <v-list dense nav>
        <v-list-item
          v-for="(item, index) in menu"
          :key="index"
          :to="item.link"
          :exact="item.exact"
          :disabled="item.disabled"
          link
          @click="getActions(item)"
        >
          <v-list-item-icon>
            <v-icon
              :class="{ 'grey--text': item.disabled }"
              v-text="item.icon"
            />
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title v-text="item.text" />
          </v-list-item-content>
        </v-list-item>

        <v-divider class="my-1"></v-divider>

        <v-list-item @click="signOut">
          <v-list-item-icon>
            <v-icon>mdi-logout-variant</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title> 로그아웃 </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-menu>
    <edit-me-dialog
      :dialog.sync="editMeDialog"
      :admin-password="adminEncodedPassword"
      v-if="editMeDialog"
    />
    <edit-password-dialog
      :dialog.sync="editMePasswordDialog"
      v-if="editMePasswordDialog"
    />
    <toolbar-theme v-model="toolbarThemeDialog" />
  </div>
</template>

<script lang="ts">
import config from "../../configs";
import EditMeDialog from "@/views/components/EditMeDialog.vue";
import ToolbarTheme from "@/components/config/ToolbarTheme.vue";
import EditPasswordDialog from "@/views/components/EditPasswordDialog.vue";
import { signOut } from "@/utils/commands";
import { promptPassword, toastError } from "@/utils/alerts";
import pbkdf2 from "pbkdf2";
import { postApi } from "@/utils/apis";
import store from "@/store";
import { defineComponent, reactive, toRefs } from "@vue/composition-api";

export default defineComponent({
  components: { ToolbarTheme, EditMeDialog, EditPasswordDialog },
  setup() {
    const state = reactive({
      menu: config.toolbar.admin,
      editMeDialog: false,
      editMePasswordDialog: false,
      toolbarThemeDialog: false,
      adminEncodedPassword: "",
      signOut: signOut,
    });
    const methods = {
      getActions: (item: { key: string }): void => {
        switch (item.key) {
          case "adminmenu.profile":
            methods.confirmPassword();
            break;
          case "adminmenu.changePassword":
            state.editMePasswordDialog = true;
            break;
          case "adminmenu.theme":
            state.toolbarThemeDialog = true;
            break;
        }
      },

      confirmPassword: async (): Promise<void> => {
        const adminPassword = await promptPassword(
          "내 정보 확인/수정",
          store.getters.admin.name +
            " 님의 정보확인을 위해 비밀번호를 입력 해 주세요",
        );

        if (!adminPassword) {
          return;
        }

        const adminEncodedPassword = pbkdf2
          .pbkdf2Sync(adminPassword, "salt", 1, 32, "sha512")
          .toString();

        const response = await postApi<void>(
          "mine/verify-password",
          adminEncodedPassword,
          false,
        );
        if (response.success) {
          state.adminEncodedPassword = adminEncodedPassword; // edit-me-dialog로 넘길 사용자 인코딩 패스워드
          state.editMeDialog = true;
        } else {
          toastError(response.message);
        }
      },
    };
    return { ...toRefs(state), ...methods };
  },
});
</script>
