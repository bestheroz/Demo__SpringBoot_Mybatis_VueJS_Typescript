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
import {
  Component,
  Prop,
  Ref,
  VModel,
  Vue,
  Watch,
} from "vue-property-decorator";
// eslint-disable-next-line
// @ts-ignore
import vue2Dropzone from "vue2-dropzone";
import "vue2-dropzone/dist/vue2Dropzone.min.css";
import envs from "@/constants/envs";
import { toastError } from "@/utils/alerts";
import { getImageUrl } from "@/utils/formatter";

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

@Component({
  components: {
    vue2Dropzone,
  },
})
export default class extends Vue {
  @VModel({ required: true }) filePath!: string | string[] | null;
  @Prop({ required: true }) readonly folderName!: string;
  @Prop() readonly width!: string;
  @Prop() readonly height!: string;
  @Prop({ type: Boolean }) readonly multiple!: boolean;
  @Prop({ default: 300 }) readonly maxFilesize!: number;
  @Prop({ default: 3 }) readonly maxFiles!: number;
  @Prop({ default: "/upload/file" }) readonly apiUrl!: string;
  @Prop({ default: "파일 첨부" }) readonly label!: string;
  @Prop() readonly accept!: string;

  @Ref("vueDropzone") readonly vueDropzone!: vue2Dropzone;

  interval: number | null = null;
  showFileUploader = false;

  get dropzoneOptions(): unknown {
    return {
      url: `${envs.FILE_API_HOST}api${this.apiUrl}`,
      maxFilesize: this.maxFilesize, // MB
      maxFiles: this.multiple ? this.maxFiles : 1,
      headers: {
        Authorization: this.$store.getters.accessToken,
      },
      acceptedFiles: this.accept,
      autoProcessQueue: true,
      addRemoveLinks: true,
      timeout: 7_200_000,
      params: () => {
        return { folderName: this.folderName };
      },
    };
  }

  protected beforeDestroy(): void {
    this.interval && clearInterval(this.interval);
    this.interval = null;
    this.$nextTick(() => {
      this.interval && clearInterval(this.interval);
      this.interval = null;
    });
  }

  protected async created(): Promise<void> {
    this.showFileUploader = false;
    await this.$store.dispatch("reIssueAccessToken");
    this.showFileUploader = true;
  }

  @Watch("folderName")
  protected reloadComponent(): void {
    this.showFileUploader = false;
    this.$nextTick(() => (this.showFileUploader = true));
  }

  @Watch("$store.getters.accessToken")
  protected watchAccessToken(val: string): void {
    this.vueDropzone &&
      this.vueDropzone.setOption("headers", {
        Authorization: val,
      });
  }

  @Watch("accept")
  protected watchAccept(val: string): void {
    this.vueDropzone.setOption("acceptedFiles", val);
  }

  @Watch("maxFilesize")
  protected watchMaxFilesize(val: number): void {
    this.vueDropzone.setOption("maxFilesize", val);
  }

  @Watch("multiple")
  @Watch("maxFiles")
  protected watchMaxFiles(): void {
    this.vueDropzone.setOption("maxFiles", this.multiple ? this.maxFiles : 1);
  }

  @Watch("apiUrl")
  protected watchApiUrl(): void {
    this.vueDropzone.setOption("url", `${envs.FILE_API_HOST}api${this.apiUrl}`);
  }

  protected onVdropzoneSuccess(file: DropzoneFile): void {
    const response = JSON.parse(file.xhr.responseText);
    if (response) {
      if (response.code.startsWith("S")) {
        this.multiple
          ? (this.filePath = [
              // eslint-disable-next-line @typescript-eslint/ban-ts-comment
              // @ts-ignore
              ...this.filePath,
              response.data,
            ])
          : (this.filePath = response.data);
        document.querySelectorAll("div.dz-filename>span").forEach((element) => {
          if (element.textContent === file.name) {
            element.innerHTML = response.data.split("/").pop() || file.name;
          }
        });
      } else {
        toastError(response.message);
        this.vueDropzone.removeFile(file);
      }
    }
  }

  protected onVdropzoneMounted(): void {
    this.interval = window.setInterval(() => {
      this.$store.dispatch("reIssueAccessToken");
    }, 180_000);

    if (!this.filePath) {
      return;
    }
    if (
      this.multiple &&
      typeof this.filePath === "object" &&
      this.filePath.length > 0
    ) {
      for (const value of this.filePath) {
        this.manuallyAddFile(getImageUrl(value));
      }
    } else if (!this.multiple && typeof this.filePath === "string") {
      this.manuallyAddFile(getImageUrl(this.filePath));
    }
  }

  protected manuallyAddFile(fileUrl: string): void {
    const xhr = new XMLHttpRequest();
    xhr.open("HEAD", fileUrl, true);
    xhr.onreadystatechange = () => {
      if (xhr.readyState === 4 && xhr.status === 200) {
        const mockFile = {
          name: fileUrl.split("/").pop(),
          size: xhr.getResponseHeader("Content-Length"),
        };
        this.vueDropzone.dropzone.emit("addedfile", mockFile);
        this.vueDropzone.dropzone.emit("thumbnail", mockFile, fileUrl);
        this.vueDropzone.dropzone.emit("complete", mockFile);
      }
    };
    xhr.send(null);
  }

  protected onVdropzoneRemovedFile(file: File): void {
    if (this.multiple) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      this.filePath = this.filePath.filter((path: string) =>
        path.endsWith(file.name),
      );
    } else {
      this.filePath = null;
    }
  }
}
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
