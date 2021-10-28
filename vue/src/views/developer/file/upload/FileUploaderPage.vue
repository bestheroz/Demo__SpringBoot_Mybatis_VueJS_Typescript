<template>
  <div class="w-full">
    <v-card>
      <v-card-title> FileUploader.vue </v-card-title>
      <v-card-text>
        <file-uploader
          v-model="filePaths"
          folder-name="uploaded/test"
          multiple
        />
      </v-card-text>
      <v-card-text v-if="filePaths && filePaths.length > 0" class="pt-0">
        <v-subheader> 업로드 완료</v-subheader>
        <v-list dense outlined>
          <v-list-item v-for="filename in filePaths" :key="filename">
            <v-list-item-icon>
              <v-icon> mdi-file-check-outline </v-icon>
            </v-list-item-icon>
            <v-list-item-content v-text="filename" />
            <v-list-item-icon>
              <v-btn icon @click="downloadFile(filename)">
                <v-icon> mdi-download </v-icon>
              </v-btn>
              <v-btn icon @click="deleteFile(filename)">
                <v-icon> mdi-delete-forever </v-icon>
              </v-btn>
            </v-list-item-icon>
          </v-list-item>
        </v-list>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { deleteApi, downloadFileApi } from "@/utils/apis";
import { toastError } from "@/utils/alerts";
import FileUploader from "@/components/file/FileUploader.vue";

@Component({
  components: {
    FileUploader,
  },
})
export default class extends Vue {
  filePaths: string[] = [];

  async deleteFile(filepath: string): Promise<void> {
    const response = await deleteApi(`delete/file?filePath=${filepath}`, false);

    if (response.code.startsWith("S")) {
      this.filePaths = this.filePaths.filter(
        (filePath) => filePath !== filepath,
      );
    } else {
      toastError(response.message);
    }
  }
  async downloadFile(filepath: string): Promise<void> {
    await downloadFileApi(`download/file?filePath=${filepath}`);
  }
}
</script>
