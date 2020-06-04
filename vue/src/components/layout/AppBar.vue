<template>
  <v-app-bar dense app clipped-left v-if="!isPopup">
    <v-app-bar-nav-icon @click.stop="syncedDrawer = !syncedDrawer" />
    <v-toolbar-title>
      <v-btn x-large text dark @click="goHome" color="primary">
        {{ title }}
      </v-btn>
    </v-toolbar-title>
    <v-spacer />
    <v-toolbar-title>
      <v-btn x-large text dark :ripple="false" color="primary">
        <countdown
          ref="countdown"
          :end-time="$store.state.logoutTime"
          @finished="$store.commit('logout')"
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
          <v-btn color="primary" x-large text dark v-on="on">
            <v-icon> mdi-account</v-icon>
            {{ $storage.get('userVO').name }}
          </v-btn>
        </template>

        <v-list dense>
          <v-list-item>
            <v-list-item-title>
              <v-btn block @click="$store.commit('logout')">
                <v-icon>mdi-logout</v-icon>
                Logout
              </v-btn>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-toolbar-title>
  </v-app-bar>
</template>

<script lang="ts">
import { Component, PropSync, Vue, Watch } from 'vue-property-decorator';
import envs from '@/constants/envs';
import Countdown from 'vue-awesome-countdown/src/vue-awesome-countdown.vue';
import { getVariableApi } from '@/utils/apis';
import { DrawerItem } from '@/common/types';

@Component({
  name: 'AppBar',
  components: { Countdown },
})
export default class extends Vue {
  @PropSync('drawer', { required: true, default: true }) syncedDrawer!: boolean;
  readonly envs: typeof envs = envs;
  title: string | null = null;
  items: DrawerItem[] | null = null;

  get isPopup(): boolean {
    return !window.toolbar.visible;
  }

  async created() {
    if (!this.$storage.has('drawer')) {
      const response = await this.$store.state.axiosInstance.get('/menu');
      this.$storage.set('drawer', response.data.data);
    }
    this.items = this.$storage.get('drawer');
  }

  async mounted() {
    this.title = await getVariableApi('title');
  }

  @Watch('$store.state.logoutTime')
  watchLogoutTime() {
    if (this.$refs.countdown) {
      (this.$refs.countdown as any).startCountdown('restart');
    }
  }

  goHome() {
    this.$router.currentRoute.path !== '/' && this.$router.push('/');
  }
}
</script>
