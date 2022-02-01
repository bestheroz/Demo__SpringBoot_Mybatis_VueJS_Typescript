import envs from "@/constants/envs";
import dayjs from "dayjs";
import { computed } from "@vue/composition-api";

export default function () {
  const computes = {
    envs: computed((): typeof envs => envs),
    dayjs: computed((): typeof dayjs => dayjs),
  };
  return { ...computes };
}
