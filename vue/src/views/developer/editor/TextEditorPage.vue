<template>
  <div class="w-full">
    <v-card-title> TextEditor.vue </v-card-title>
    <v-card-text>
      <text-editor v-model="textHtml" height="60vh" />
    </v-card-text>
    <v-card-title class="text-h2"> v-model="value" </v-card-title>
    <v-card-text v-text="textHtml" />
  </div>
</template>

<script lang="ts">
import TextEditor from "@/components/texteditor/TextEditor.vue";
import { defineComponent, reactive, toRefs } from "@vue/composition-api";

export default defineComponent({
  components: { TextEditor },
  setup() {
    const state = reactive({ textHtml: "텍스트에디터 테스트" });
    return { ...toRefs(state) };
  },
});
</script>
