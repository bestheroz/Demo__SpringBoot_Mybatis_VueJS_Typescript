<template>
  <div class="flex-grow-1 align-center justify-center d-flex flex-column">
    <h1
      style="font-size: 3rem"
      v-text="envs.PRODUCT_TITLE"
      class="primary--text"
    />
    <h1 :style="`color: ${color}`" v-text="now" />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import dayjs from "dayjs";
import envs from "@/constants/envs";

@Component({})
export default class extends Vue {
  readonly envs = envs;
  interval: number | null = null;
  interval2: number | null = null;
  now = "";
  color = "";

  protected beforeDestroy(): void {
    this.interval && clearInterval(this.interval);
    this.interval = null;
    this.$nextTick(() => {
      this.interval && clearInterval(this.interval);
      this.interval = null;
    });
    this.interval2 && clearInterval(this.interval2);
    this.interval2 = null;
    this.$nextTick(() => {
      this.interval2 && clearInterval(this.interval2);
      this.interval2 = null;
    });
  }

  protected async created(): Promise<void> {
    this.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
    this.color = this.getRandomColor();
    this.interval = window.setInterval(() => {
      this.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
    }, 1000);
    this.interval2 = window.setInterval(() => {
      this.color = this.getRandomColor();
    }, 10000);
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
