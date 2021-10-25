<template>
  <div>
    <h3 v-text="label" v-if="label" class="secondary mb-3" />
    <vue-editor
      v-model="textHtml"
      :editor-options="defaultOptions"
      :placeholder="placeholder"
      use-custom-image-handler
      @image-added="uploadImage"
      :style="{ height: height }"
    />
  </div>
</template>

<script lang="ts">
import { Component, Prop, VModel, Vue } from "vue-property-decorator";
import { VueEditor } from "vue2-editor";
import { uploadFileApi } from "@/utils/apis";
import { getImageUrl } from "@/utils/formatter";

@Component({
  name: "TextEditor",
  components: { VueEditor },
})
export default class extends Vue {
  @VModel({ required: true }) textHtml!: string;
  @Prop() readonly label!: string;
  @Prop({ default: "uploaded/textEditor" }) readonly folderName!: string;
  @Prop({ default: "upload/file" }) readonly apiUrl!: string;
  @Prop({ default: "40vh" }) readonly height!: string | number;
  @Prop({ default: "Please enter text." }) readonly placeholder!: string;

  get defaultOptions(): {
    modules: { toolbar: unknown[] };
  } {
    return {
      modules: {
        toolbar: [
          [{ color: [] }],
          ["bold", "italic", "underline", "strike"],
          [
            { align: "" },
            { align: "center" },
            { align: "right" },
            { align: "justify" },
          ],

          [{ list: "ordered" }, { list: "bullet" }],
          ["link", "image", "video"],

          ["clean"], // remove formatting button
        ],
      },
    };
  }

  protected async uploadImage(
    file: File,
    // eslint-disable-next-line @typescript-eslint/ban-types
    Editor: { insertEmbed: Function },
    cursorLocation: number,
  ): Promise<void> {
    const formData = new FormData();
    formData.append("folderName", this.folderName);
    formData.append("file", file);
    const result = await uploadFileApi(this.apiUrl, formData);
    Editor.insertEmbed(cursorLocation, "image", getImageUrl(result.data));
  }
}
</script>

<style lang="scss">
.quillWrapper {
  padding-bottom: 40px;
  .ql-toolbar {
    background-color: #a6a6a6;
  }
  .ql-container {
    overflow-y: auto;
    .ql-tooltip {
      left: 0 !important;
    }
  }
}
</style>
