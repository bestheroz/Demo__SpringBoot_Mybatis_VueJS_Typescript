<template>
  <div>
    <v-container class="fill-height" fluid style="height: 60vh">
      <v-row align="center" justify="center">
        <v-col cols="12" class="text-center">
          <h1 style="font-size: 3rem">{{ title }}</h1>
        </v-col>
        <v-col cols="12" class="text-center">
          <h1 :style="`color: ${color}`">{{ now }}</h1>
        </v-col>
      </v-row>
    </v-container>
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
  interval: number | null = null;
  now = "";
  color = "";

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
    this.color = this.getRandomColor();
    this.interval = window.setInterval(() => {
      this.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
      this.color = this.getRandomColor();
    }, 1000);
  }

  protected getRandomColor(): string {
    const letters = "0123456789ABCDEF";
    let color = "#";
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
}
</script>
