<template>
  <div>
    <v-main>
      <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
          <v-col cols="12" class="text-center">
            <h1 style="font-size: 3rem">{{ title }}</h1>
          </v-col>
          <v-col cols="12" class="text-center">
            <h1 :style="`color: ${color}`">{{ now }}</h1>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { getVariableApi } from "@/utils/apis";
import dayjs from "dayjs";

@Component({
  name: "Home",
})
export default class extends Vue {
  title: string | null = null;
  interval: any = null;
  now = "";
  color = "";

  async mounted(): Promise<void> {
    this.title = await getVariableApi("title");
    this.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
    this.color = this.getRandomColor();
    this.interval = setInterval(() => {
      this.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
      this.color = this.getRandomColor();
    }, 1000);
  }

  beforeDestroy() {
    this.interval && clearInterval(this.interval);
    this.interval = null;
  }

  getRandomColor() {
    const letters = "0123456789ABCDEF";
    let color = "#";
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
}
</script>
