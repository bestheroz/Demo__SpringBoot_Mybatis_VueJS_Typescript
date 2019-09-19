<template>
  <div>
    <v-app-bar
      color="deep-purple accent-4"
      dark
      app
    >
      <v-btn
        icon
        to="/"
      >
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              dark
              v-on="on"
            >
              home
            </v-icon>
          </template>
          <span>Home</span>
        </v-tooltip>
      </v-btn>
      <v-toolbar-title>My Spring + Vue + Vuetify ver.190826</v-toolbar-title>

      <div class="flex-grow-1" />

      <v-btn icon>
        <v-tooltip bottom>
          <template v-slot:activator="{ on }">
            <v-icon
              dark
              v-on="on"
            >
              exit_to_app
            </v-icon>
          </template>
          <span>Logout</span>
        </v-tooltip>
      </v-btn>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: 'AppBar'
}
</script>

<style scoped>

</style>
