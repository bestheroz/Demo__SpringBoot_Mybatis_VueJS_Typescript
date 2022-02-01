<template>
  <div class="w-full">
    <menu-list />
  </div>
</template>

<script lang="ts">
import MenuList from "@/views/management/menu/MenuList.vue";
import { defineComponent } from "@vue/composition-api";

export default defineComponent({
  components: {
    MenuList,
  },
});
</script>
