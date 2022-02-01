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
import dayjs from "dayjs";
import envs from "@/constants/envs";
import {
  computed,
  defineComponent,
  nextTick,
  onMounted,
  onUnmounted,
  reactive,
  toRefs,
} from "@vue/composition-api";

export default defineComponent({
  setup() {
    const state = reactive({
      interval: null as number | null,
      interval2: null as number | null,
      now: "",
      color: "",
    });

    const computes = {
      envs: computed((): typeof envs => envs),
    };
    const methods = {
      getRandomColor: (): string => {
        const letters = "0123456789ABCDEF";
        let color = "#";
        for (let i = 0; i < 6; i++) {
          color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
      },
    };
    onUnmounted(() => {
      state.interval && clearInterval(state.interval);
      state.interval = null;
      nextTick(() => {
        state.interval && clearInterval(state.interval);
        state.interval = null;
      });
      state.interval2 && clearInterval(state.interval2);
      state.interval2 = null;
      nextTick(() => {
        state.interval2 && clearInterval(state.interval2);
        state.interval2 = null;
      });
    });
    onMounted(() => {
      state.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
      state.color = methods.getRandomColor();
      state.interval = window.setInterval(() => {
        state.now = dayjs().format("YYYY년 MM월 DD일 HH시 mm분 ss초");
      }, 1000);
      state.interval2 = window.setInterval(() => {
        state.color = methods.getRandomColor();
      }, 10000);
    });
    return { ...toRefs(state), ...computes, ...methods };
  },
});
</script>
