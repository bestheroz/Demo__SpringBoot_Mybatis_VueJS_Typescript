<template>
  <v-app-bar dense app clipped-left v-if="!isPopup">
    <v-app-bar-nav-icon @click.stop="syncedDrawer = !syncedDrawer" />
    <v-toolbar-title>
      <v-btn x-large text dark @click="goHome" color="primary">
        {{ appTitle }}
      </v-btn>
    </v-toolbar-title>
    <v-spacer />
    <v-toolbar-title>
      <v-menu
        open-on-hover
        bottom
        offset-y
        v-for="item in items"
        :key="item.title"
      >
        <template v-slot:activator="{ on }">
          <v-btn x-large text dark v-on="on">
            <v-icon v-if="item.icon"> {{ item.icon }}</v-icon>
            {{ item.title }}
          </v-btn>
        </template>

        <v-list dense v-if="item.children">
          <v-list-item
            v-for="(child, i) in item.children"
            :key="i"
            @click="child.type === 'W' ? popupWindow(`${child.to}`) : undefined"
            :to="child.type !== 'W' ? `${child.to}` : undefined"
            link
          >
            <v-list-item-content>
              <v-list-item-title>
                {{ child.title }}
                <v-icon v-if="child.type === 'W'">mdi-dock-window</v-icon>
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-toolbar-title>
    <v-spacer />
    <v-toolbar-title v-if="cmpAUrl || cmpBUrl">
      <v-menu open-on-hover bottom offset-y>
        <template v-slot:activator="{ on }">
          <v-btn color="primary" x-large text dark v-on="on">
            <v-icon> mdi-dock-window</v-icon>
          </v-btn>
        </template>

        <v-list dense>
          <v-list-item v-if="cmpAUrl">
            <v-list-item-title>
              <v-btn text @click="goCmpA"> CMP (A) </v-btn>
            </v-list-item-title>
          </v-list-item>
          <v-list-item v-if="cmpBUrl">
            <v-list-item-title>
              <v-btn text @click="goCmpB"> CMP (B) </v-btn>
            </v-list-item-title>
          </v-list-item>
          <v-list-item v-if="cmpAUrl">
            <v-list-item-title>
              <v-btn text to="/tester/grafana"> grafana </v-btn>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-toolbar-title>
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
            {{ $storage.get('userVO').empnm }}
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
  cmpAUrl: string | null = null;
  cmpBUrl: string | null = null;
  appTitle: string | null = null;
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
    this.cmpAUrl = await getVariableApi('cmpAUrl');
    this.cmpBUrl = await getVariableApi('cmpAUrl');
    this.appTitle = await getVariableApi('appTitle');
  }

  @Watch('$store.state.logoutTime')
  watchLogoutTime() {
    if (this.$refs.countdown) {
      (this.$refs.countdown as any).startCountdown('restart');
    }
  }

  popupWindow(url: string) {
    window.open(
      `#${url}`,
      '_blank',
      'location=false,menubar=false,scrollbars=true,status=false,toolbar=false',
    );
  }

  goHome() {
    this.$router.currentRoute.path !== '/' && this.$router.push('/');
  }

  goCmpA() {
    window.open(`${this.cmpAUrl}`, '_blank');
  }

  goCmpB() {
    window.open(`${this.cmpBUrl}`, '_blank');
  }
}
</script>
