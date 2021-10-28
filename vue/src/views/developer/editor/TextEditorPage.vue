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
import { Component, Vue } from "vue-property-decorator";
import TextEditor from "@/components/texteditor/TextEditor.vue";

@Component({
  components: { TextEditor },
})
export default class extends Vue {
  textHtml = "텍스트에디터 테스트";
}
</script>
