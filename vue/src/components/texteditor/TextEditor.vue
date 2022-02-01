<template>
  <div>
    <h3 v-text="label" v-if="label" class="secondary mb-3" />
    <vue-editor
      v-model="vModel"
      :editor-options="defaultOptions"
      :placeholder="placeholder"
      use-custom-image-handler
      @image-added="uploadImage"
      :style="{ height: height }"
    />
  </div>
</template>

<script lang="ts">
import { VueEditor } from "vue2-editor";
import { uploadFileApi } from "@/utils/apis";
import { getImageUrl } from "@/utils/formatter";
import { computed, defineComponent } from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  components: { VueEditor },
  props: {
    value: {
      type: String,
      required: true,
    },
    label: { type: String, default: undefined },
    folderName: { type: String, default: "uploaded/textEditor" },
    apiUrl: { type: String, default: "upload/file" },
    height: { type: [String, Number], default: "40vh" },
    placeholder: { type: String, default: "Please enter text." },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<string>(props, emit);
    const computes = {
      defaultOptions: computed(
        (): {
          modules: { toolbar: unknown[] };
        } => ({
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
        }),
      ),
    };
    const methods = {
      uploadImage: async (
        file: File,
        // eslint-disable-next-line @typescript-eslint/ban-types
        Editor: { insertEmbed: Function },
        cursorLocation: number,
      ): Promise<void> => {
        const formData = new FormData();
        formData.append("folderName", props.folderName);
        formData.append("file", file);
        const result = await uploadFileApi(props.apiUrl, formData);
        Editor.insertEmbed(cursorLocation, "image", getImageUrl(result.data));
      },

      validateText: (): boolean => {
        // html 태그 삭제 정규식
        const extractTextPattern = /(<([^>]+)>)/gi;
        return !!vModel.vModel.value.replace(extractTextPattern, "");
      },
    };
    return { ...vModel, ...computes, ...methods };
  },
});
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
