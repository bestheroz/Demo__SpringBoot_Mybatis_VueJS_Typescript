<template>
  <v-app>
    <drawer :drawer.sync="drawer" />
    <app-bar :drawer.sync="drawer" />
    <viewer />
  </v-app>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import AppBar from '@/components/layout/AppBar.vue';
import Drawer from '@/components/layout/Drawer.vue';
import Viewer from '@/components/layout/Viewer.vue';

@Component({
  name: 'Index',
  components: {
    Viewer,
    Drawer,
    AppBar,
  },
})
export default class extends Vue {
  drawer: boolean = true;

  @Watch('$store.state.user.user', { immediate: true })
  async watchUser() {
    await this.$store.dispatch('getUser');
  }

  @Watch('$store.state.cache.members', { immediate: true })
  async watchCacheMembers() {
    await this.$store.dispatch('getMemberCodes');
  }

  @Watch('$store.state.drawer.drawers', { immediate: true })
  async watchDrawers() {
    await this.$store.dispatch('getDrawers');
  }
}
</script>
