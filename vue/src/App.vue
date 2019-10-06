<template>
  <v-app>
    <NavigationDrawers>
      <!-- -->
    </NavigationDrawers>

    <AppBar>
      <!-- -->
    </AppBar>

    <!-- Sizes your content based upon application components -->
    <v-content>
      <!-- Provides the application the proper gutter -->
      <v-container fluid>
        <!-- If using vue-router -->
        <router-view />
      </v-container>
    </v-content>

    <Footer>
      <!-- -->
    </Footer>
  </v-app>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import NavigationDrawers from '@/components/layout/NavigationDrawers.vue';
import AppBar from '@/components/layout/AppBar.vue';
import Footer from '@/components/layout/Footer.vue';

@Component({
  components: {
    NavigationDrawers,
    AppBar,
    Footer,
  },
})
export default class App extends Vue {}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
