<template>
  <div class="w-full">
    <admin-list />
  </div>
</template>

<script lang="ts">
import AdminList from "@/views/management/admin/AdminList.vue";

import { defineComponent } from "@vue/composition-api";

export default defineComponent({
  components: { AdminList },
});
</script>
