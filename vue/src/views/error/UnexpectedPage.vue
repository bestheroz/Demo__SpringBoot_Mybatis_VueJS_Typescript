<template>
  <v-card class="text-center w-full error-page pa-3">
    <v-img
      src="/images/illustrations/500-illustration.svg"
      max-height="400"
      contain
    />
    <div class="text-h3 mt-10">OOPS! Something went wrong here</div>
    <div class="my-3">Our experts are working to fix the issue.</div>
    <v-btn block large color="primary" @click="$router.back()"
      >Send me back</v-btn
    >
  </v-card>
</template>

<style>
.error-page {
  max-width: 500px;
}
</style>
