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
        <router-view/>
      </v-container>
    </v-content>

    <Footer>
      <!-- -->
    </Footer>
  </v-app>
</template>

<script>
    import NavigationDrawers from "./components/common/NavigationDrawers";
    import AppBar from "./components/common/AppBar";
    import Footer from "./components/common/Footer";

    export default {
        name: 'App',
        components: {
            'NavigationDrawers': NavigationDrawers,
            'AppBar': AppBar,
            'Footer': Footer,
        }
    };
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
