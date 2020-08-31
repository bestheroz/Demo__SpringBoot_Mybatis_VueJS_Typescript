<template>
  <v-app-bar
    absolute
    :dark="$vuetify.theme.dark"
    color="transparent"
    flat
    height="85"
    v-if="!isPopup"
  >
    <v-container>
      <v-row align="center">
        <v-toolbar-title v-text="title" :style="{ cursor: 'pointer' }" />
        <v-spacer />
        <v-toolbar-title
          class="font-weight-light"
          :style="{ cursor: 'pointer' }"
          @click="logout"
          v-if="$router.currentRoute.path !== '/login' && accessToken"
        >
          <v-icon>mdi-logout</v-icon>
          로그아웃
        </v-toolbar-title>
      </v-row>
    </v-container>
  </v-app-bar>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getVariableApi } from '@/utils/apis';
import { logout } from '@/utils/authentications';

@Component({ name: 'AppBarNoDrawer' })
export default class extends Vue {
  readonly logout: typeof logout = logout;
  title: string | null = null;

  get isPopup(): boolean {
    return (
      !window.toolbar.visible || this.$route.fullPath.indexOf('/popup') === 0
    );
  }

  get accessToken() {
    if (window && window.localStorage) {
      return window.localStorage.getItem('accessToken');
    } else {
      return null;
    }
  }

  async mounted() {
    this.title = await getVariableApi('title');
  }
}
</script>
