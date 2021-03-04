<template>
  <div>
    <v-system-bar app color="dark" dark>
      <v-subheader>
        <v-app-bar-nav-icon @click.stop="syncedDrawer = !syncedDrawer" />
        {{ title || "" }}
      </v-subheader>
      <button-icon-tooltip
        text="홈으로"
        icon="mdi-home-outline"
        @click="goHome"
      />
      <button-icon-tooltip
        :text="$vuetify.theme.dark ? 'Active light Mode' : 'Active Dark Mode'"
        :icon="$vuetify.theme.dark ? 'mdi-weather-night' : 'mdi-weather-sunny'"
        @click="$store.dispatch('toggleTheme')"
      />
      <button-icon-tooltip
        text="내 정보수정"
        icon="mdi-account-edit-outline"
        @click="editMeDialog = true"
      />
      <button-icon-tooltip
        text="Logout"
        icon="mdi-logout"
        @click="$store.dispatch('logout')"
      />
      <v-spacer />
      <v-subheader class="pl-0">
        <v-icon> mdi-account-outline</v-icon>
        {{ $store.getters.user.name }}
      </v-subheader>
      <v-subheader class="pl-0" v-text="now" />
    </v-system-bar>
    <edit-me-dialog :dialog.sync="editMeDialog" v-if="editMeDialog" />
  </div>
</template>

<script lang="ts">
import { Component, PropSync, Vue } from "vue-property-decorator";
import { getVariableApi } from "@/utils/apis";
import EditMeDialog from "@/components/layout/components/EditMeDialog.vue";
import ButtonIconTooltip from "@/components/button/ButtonIconTooltip.vue";
import dayjs from "dayjs";

@Component({
  name: "AppBar",
  components: { ButtonIconTooltip, EditMeDialog },
})
export default class extends Vue {
  @PropSync("drawer", { required: true, default: true }) syncedDrawer!: boolean;
  title: string | null = null;
  editMeDialog = false;

  interval: number | null = null;
  now = "";

  protected beforeDestroy(): void {
    this.interval && clearInterval(this.interval);
    this.interval = null;
    this.$nextTick(() => {
      this.interval && clearInterval(this.interval);
      this.interval = null;
    });
  }
  protected async created(): Promise<void> {
    this.title = await getVariableApi("title");
    this.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
    this.interval = window.setInterval(() => {
      this.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
    }, 1000);
  }

  protected goHome(): void {
    this.$router.currentRoute.path !== "/" && this.$router.push("/");
  }
}
</script>
