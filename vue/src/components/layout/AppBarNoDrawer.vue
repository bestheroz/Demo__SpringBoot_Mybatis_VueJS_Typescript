<template>
  <v-app-bar absolute dark color="transparent" flat height="85" v-if="!isPopup">
    <v-container>
      <v-row align="center">
        <v-toolbar-title v-text="title" :style="{ cursor: 'pointer' }" />
        <v-spacer />
        <v-toolbar-title
          class="font-weight-light"
          :style="{ cursor: 'pointer' }"
          @click="$store.commit('logout')"
          v-if="
            $router.currentRoute.path !== '/login' &&
            window.localStorage.getItem('accessToken')
          "
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
import envs from '@/constants/envs';
import router from '@/router';
import { getVariableApi } from '@/utils/apis';

@Component({ name: 'AppBarNoDrawer' })
export default class extends Vue {
  router: typeof router = router;
  envs: typeof envs = envs;
  title: string | null = null;

  get isPopup(): boolean {
    return (
      !window.toolbar.visible || this.$route.fullPath.indexOf('/popup') === 0
    );
  }

  async mounted() {
    this.title = await getVariableApi('title');
  }
}
</script>
