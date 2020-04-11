<template>
  <div>
    <v-app-bar app dark>
      <v-btn icon to="/">
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon dark v-on="on">
              home
            </v-icon>
          </template>
          <span>Home</span>
        </v-tooltip>
      </v-btn>
      <v-toolbar-title>
        {{ `${envs.APP_NAME} ver.${envs.APP_VERSIONS}` }}
      </v-toolbar-title>

      <div class="flex-grow-1" />

      <v-btn @click="$store.commit('logout')" icon>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon dark v-on="on">
              exit_to_app
            </v-icon>
          </template>
          <span>Logout</span>
        </v-tooltip>
      </v-btn>
    </v-app-bar>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import envs from '@/constants/envs';

@Component({})
export default class AppBar extends Vue {
  readonly envs: typeof envs = envs;
}
</script>
