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
      <v-toolbar-title>
        <v-btn x-large text :ripple="false" color="primary">
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
      </v-toolbar-title>
      <v-toolbar-title>
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
                <v-btn block text @click="editMe">
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
      </v-toolbar-title>
      <v-btn icon @click="changeTheme" color="button-default">
        <v-icon v-if="$vuetify.theme.dark">mdi-weather-sunny</v-icon>
        <v-icon v-else>mdi-weather-night</v-icon>
      </v-btn>
    </v-app-bar>
    <edit-me
      :edit-item="$store.state.user.user"
      :dialog.sync="editMeDialog"
      v-if="editMeDialog"
    />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from 'vue-property-decorator';
import Countdown from 'vue-awesome-countdown/src/vue-awesome-countdown.vue';
import { getVariableApi, postDataApi } from '@/utils/apis';
import { logout } from '@/utils/authentications';
import EditMe from '@/components/layout/components/EditMe.vue';
import { TableMemberEntity } from '@/common/types';

@Component({
  name: 'AppBar',
  components: { EditMe, Countdown },
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
    this.$vuetify.theme.dark = (this.user.theme || 'dark') === 'dark';
  }

  goHome() {
    this.$router.currentRoute.path !== '/' && this.$router.push('/');
  }

  editMe() {
    this.editMeDialog = true;
  }

  changeTheme() {
    this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
    window.localStorage.setItem(
      'theme',
      this.$vuetify.theme.dark ? 'dark' : 'light',
    );
    try {
      postDataApi<{
        theme: string;
      }>(
        `members/${this.user.id}/changeTheme/`,
        {
          theme: this.$vuetify.theme.dark ? 'dark' : 'light',
        },
        false,
      );
    } catch (e) {
      console.warn(e);
    }
  }
}
</script>
