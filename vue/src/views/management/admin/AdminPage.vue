<template>
  <div class="w-full">
    <admin-list />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import AdminList from "@/views/management/admin/AdminList.vue";

@Component({
  components: { AdminList },
})
export default class extends Vue {}
</script>
