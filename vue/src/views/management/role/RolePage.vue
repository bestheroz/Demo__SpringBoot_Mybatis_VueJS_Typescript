<template>
  <div class="w-full">
    <role-list />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import RoleList from "@/views/management/role/RoleList.vue";

@Component({
  components: {
    RoleList,
  },
})
export default class extends Vue {}
</script>
