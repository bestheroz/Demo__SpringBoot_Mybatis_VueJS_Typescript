<template>
  <div class="w-full">
    <role-list />
  </div>
</template>

<script lang="ts">
import RoleList from "@/views/management/role/RoleList.vue";
import { defineComponent } from "@vue/composition-api";

export default defineComponent({
  components: {
    RoleList,
  },
});
</script>
