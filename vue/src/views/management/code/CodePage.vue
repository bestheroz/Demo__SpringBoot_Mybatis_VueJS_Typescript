<template>
  <div class="w-full">
    <code-list />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import CodeList from "@/views/management/code/CodeList.vue";

@Component({
  components: {
    CodeList,
  },
})
export default class extends Vue {}
</script>
