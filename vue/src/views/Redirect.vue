<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component({
  name: "Redirect",
})
export default class extends Vue {
  protected async mounted(): Promise<void> {
    if (!this.$store.getters.loggedIn) {
      await this.$router.push("/login");
      return;
    }
    this.$store.dispatch("initDrawers").then();
    this.$store.dispatch("initMemberCodes").then();
    await this.$router.push("/index");
  }
}
</script>
