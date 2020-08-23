<template>
  <div>
    <v-main>
      <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
          <v-col cols="12" class="text-center">
            <h1>{{ title }}</h1>
          </v-col>
          <v-col cols="12" class="text-center">
            <h2>{{ now }}</h2>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getVariableApi } from '@/utils/apis';

@Component({
  name: 'Home',
})
export default class extends Vue {
  title: string | null = null;
  interval: any = null;
  now: string = '';

  async mounted() {
    this.title = await getVariableApi('title');
    this.interval = setInterval(() => {
      this.now = new Date().toString();
    }, 1000);
  }

  beforeDestroy() {
    this.interval && clearInterval(this.interval);
    this.interval = null;
  }
}
</script>
