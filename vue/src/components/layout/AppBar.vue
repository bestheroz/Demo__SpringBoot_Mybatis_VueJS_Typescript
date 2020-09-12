<template>
  <div>
    <v-app-bar dense app clipped-left v-if="!isPopup">
      <v-app-bar-nav-icon @click.stop="syncedDrawer = !syncedDrawer" />
      <v-toolbar-title>
        <v-btn x-large text @click="goHome" color="primary">
          {{ title }}
        </v-btn>
      </v-toolbar-title>
      <v-spacer />
      <v-btn x-large text :ripple="false" style="cursor: default" class="pr-0">
        <countdown
          ref="countdown"
          :end-time="logoutTimer"
          @finished="logout"
          :speed="1000"
        >
          <template v-slot:process="anyYouWantedScopeName">
            <v-icon>mdi-timer-sand</v-icon>
            {{
              `${anyYouWantedScopeName.timeObj.h}시간 ${anyYouWantedScopeName.timeObj.m}분 ${anyYouWantedScopeName.timeObj.s}초`
            }}
          </template>
          <template v-slot:finish>
            <span>Logout!</span>
          </template>
        </countdown>
      </v-btn>
      <v-menu open-on-hover bottom offset-y>
        <template v-slot:activator="{ on }">
          <v-btn color="primary" x-large text v-on="on">
            <v-icon> mdi-account</v-icon>
            {{ user.name }}
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
              <v-btn block text @click="logout">
                <v-icon>mdi-logout</v-icon>
                Logout
              </v-btn>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-app-bar>
    <edit-me-dialog :dialog.sync="editMeDialog" />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from 'vue-property-decorator';
import Countdown from 'vue-awesome-countdown/src/vue-awesome-countdown.vue';
import { getVariableApi } from '@/utils/apis';
import { logout } from '@/utils/authentications';
import EditMeDialog from '@/components/layout/components/EditMeDialog.vue';
import { TableMemberEntity } from '@/common/types';

@Component({
  name: 'AppBar',
  components: { EditMeDialog, Countdown },
})
export default class extends Vue {
  @PropSync('drawer', { required: true, default: true }) syncedDrawer!: boolean;
  readonly logout: typeof logout = logout;
  title: string | null = null;
  user: TableMemberEntity = { name: '' };
  editMeDialog: boolean = false;

  get isPopup(): boolean {
    return !window.toolbar.visible;
  }

  get logoutTimer() {
    return this.$store.state.user.logoutTimer;
  }

  async mounted() {
    this.title = await getVariableApi('title');
  }

  @Watch('$store.state.user.logoutTimer', { immediate: true })
  watchLogoutTime() {
    if (this.$refs.countdown) {
      (this.$refs.countdown as any).startCountdown('restart');
    }
  }

  @Watch('$store.state.user.user', { immediate: true })
  async watchUser() {
    this.user = await this.$store.dispatch('getUser');
    this.$vuetify.theme.dark = (this.user.theme || 'light') === 'dark';
  }

  goHome() {
    this.$router.currentRoute.path !== '/' && this.$router.push('/');
  }
}
</script>
