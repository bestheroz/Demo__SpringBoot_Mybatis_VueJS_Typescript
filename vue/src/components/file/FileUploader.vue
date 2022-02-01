<template>
  <div>
    <div v-text="label" style="font-size: 1rem" />
    <vue2-dropzone
      ref="vueDropzone"
      id="dropzone"
      :options="dropzoneOptions"
      @vdropzone-success="onVdropzoneSuccess"
      @vdropzone-mounted="onVdropzoneMounted"
      @vdropzone-removed-file="onVdropzoneRemovedFile"
      :style="`width: ${width}; height: ${height}; display: inline-flex;`"
      v-if="showFileUploader"
    />
  </div>
</template>

<script lang="ts">
// eslint-disable-next-line
// @ts-ignore
import vue2Dropzone from "vue2-dropzone";
import "vue2-dropzone/dist/vue2Dropzone.min.css";
import envs from "@/constants/envs";
import { toastError } from "@/utils/alerts";
import { getImageUrl } from "@/utils/formatter";
import {
  computed,
  defineComponent,
  nextTick,
  onBeforeMount,
  onUnmounted,
  PropType,
  reactive,
  ref,
  toRefs,
  watch,
} from "@vue/composition-api";
import store from "@/store";
import setupVModel from "@/composition/setupVModel";

interface DropzoneFile {
  height: number;
  dataURL: string;
  name: string;
  size: number;
  status: "queued" | "error" | "success";
  type: string;
  width: number;
  xhr: XMLHttpRequest;
}

export default defineComponent({
  components: { vue2Dropzone },
  props: {
    value: {
      type: Array as PropType<string | string[] | null>,
      required: true,
    },
    folderName: { type: String, required: true },
    width: { type: String, default: undefined },
    height: { type: String, default: undefined },
    multiple: { type: Boolean },
    maxFilesize: { type: Number, default: 300 },
    maxFiles: { type: Number, default: 3 },
    apiUrl: { type: String, default: "/upload/file" },
    label: { type: String, default: "파일 첨부" },
    accept: { type: String, default: undefined },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<string | string[] | null>(props, emit);
    const state = reactive({
      interval: null as number | null,
      showFileUploader: false,
    });
    const computes = {
      dropzoneOptions: computed((): unknown => ({
        url: `${envs.FILE_API_HOST}api${props.apiUrl}`,
        maxFilesize: props.maxFilesize, // MB
        maxFiles: props.multiple ? props.maxFiles : 1,
        headers: {
          Authorization: window.localStorage.getItem("accessToken") ?? "",
        },
        acceptedFiles: props.accept,
        autoProcessQueue: true,
        addRemoveLinks: true,
        timeout: 7_200_000,
        params: () => ({ folderName: props.folderName }),
      })),
    };
    const methods = {
      onVdropzoneSuccess: (file: DropzoneFile): void => {
        const response = JSON.parse(file.xhr.responseText);
        if (response) {
          if (response.success) {
            props.multiple
              ? (vModel.vModel.value = [
                  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                  // @ts-ignore
                  ...vModel.vModel.value,
                  response.data,
                ])
              : (vModel.vModel.value = response.data);
            document
              .querySelectorAll("div.dz-filename>span")
              .forEach((element) => {
                if (element.textContent === file.name) {
                  element.innerHTML =
                    response.data.split("/").pop() || file.name;
                }
              });
          } else {
            toastError(response.message);
            vueDropzone.value?.removeFile(file);
          }
        }
      },

      onVdropzoneMounted: (): void => {
        state.interval = window.setInterval(() => {
          store.dispatch("reIssueAccessToken");
        }, 180_000);

        if (!vModel.vModel.value) {
          return;
        }
        if (
          props.multiple &&
          typeof vModel.vModel.value === "object" &&
          vModel.vModel.value.length > 0
        ) {
          for (const value of vModel.vModel.value) {
            methods.manuallyAddFile(getImageUrl(value));
          }
        } else if (!props.multiple && typeof vModel.vModel.value === "string") {
          methods.manuallyAddFile(getImageUrl(vModel.vModel.value));
        }
      },

      manuallyAddFile: (fileUrl: string): void => {
        const xhr = new XMLHttpRequest();
        xhr.open("HEAD", fileUrl, true);
        xhr.onreadystatechange = () => {
          if (xhr.readyState === 4 && xhr.status === 200) {
            const mockFile = {
              name: fileUrl.split("/").pop(),
              size: xhr.getResponseHeader("Content-Length"),
            };
            vueDropzone.value?.dropzone.emit("addedfile", mockFile);
            vueDropzone.value?.dropzone.emit("thumbnail", mockFile, fileUrl);
            vueDropzone.value?.dropzone.emit("complete", mockFile);
          }
        };
        xhr.send(null);
      },

      onVdropzoneRemovedFile: (file: File): void => {
        if (props.multiple) {
          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          // @ts-ignore
          vModel.vModel.value = vModel.vModel.value.filter((path: string) =>
            path.endsWith(file.name),
          );
        } else {
          vModel.vModel.value = null;
        }
      },
    };
    onUnmounted(() => {
      state.interval && clearInterval(state.interval);
      state.interval = null;
      nextTick(() => {
        state.interval && clearInterval(state.interval);
        state.interval = null;
      });
    });
    onBeforeMount(async () => {
      state.showFileUploader = false;
      await store.dispatch("reIssueAccessToken");
      state.showFileUploader = true;
    });
    watch(
      () => props.folderName,
      () => {
        state.showFileUploader = false;
        nextTick(() => (state.showFileUploader = true));
      },
    );
    watch(
      () => store.getters.accessToken,
      () => {
        vueDropzone &&
          vueDropzone.value?.setOption("headers", {
            Authorization: window.localStorage.getItem("accessToken"),
          });
      },
    );
    watch(
      () => props.accept,
      (val: string) => {
        vueDropzone.value?.setOption("acceptedFiles", val);
      },
    );
    watch(
      () => props.maxFilesize,
      (val: number) => {
        vueDropzone.value?.setOption("maxFilesize", val);
      },
    );
    watch(
      () => props.multiple,
      () => {
        vueDropzone.value?.setOption(
          "maxFiles",
          props.multiple ? props.maxFiles : 1,
        );
      },
    );
    watch(
      () => props.maxFiles,
      () => {
        vueDropzone.value?.setOption(
          "maxFiles",
          props.multiple ? props.maxFiles : 1,
        );
      },
    );
    watch(
      () => props.apiUrl,
      () => {
        vueDropzone.value?.setOption(
          "url",
          `${envs.FILE_API_HOST}api${props.apiUrl}`,
        );
      },
    );
    const vueDropzone = ref<null | InstanceType<vue2Dropzone>>(null);
    return { ...toRefs(state), ...computes, ...methods, vueDropzone };
  },
});
</script>

<style lang="scss">
.dropzone {
  background: var(--v-background-base);

  > .dz-preview {
    &.dz-image-preview {
      min-width: 200px;
      min-height: 200px;

      .dz-image {
        min-width: 200px;
        min-height: 200px;

        img {
          margin: auto;
        }
      }
    }

    .dz-remove {
      top: 0;
      right: 0;
      bottom: inherit;
      padding: 8px 8px;
    }

    .dz-details {
      .dz-filename {
        white-space: normal;
      }
    }
  }
}
</style>
