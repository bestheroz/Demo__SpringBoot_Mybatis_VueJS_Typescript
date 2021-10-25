<template>
  <div>
    <v-card flat :loading="loading">
      <v-card-text class="py-0">
        <v-chip-group v-model="selected" column dense mandatory>
          <v-chip
            v-for="item in items"
            :value="item"
            filter
            :key="item"
            :outlined="selected !== item"
            color="primary"
            v-text="`# ${item}`"
            class="px-2"
            label
          />
        </v-chip-group>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Emit, Vue, Watch } from "vue-property-decorator";
import { getApi } from "@/utils/apis";
import { Code } from "@/definitions/models";

@Component({
  components: {},
})
export default class extends Vue {
  selected = "";
  items: string[] = [];
  loading = false;

  protected async mounted(): Promise<void> {
    this.items = [];
    this.loading = true;
    const response = await getApi<string[]>("codes/types/");
    this.loading = false;
    this.items = response.data ? response.data : [];
    if (this.items.length > 0) {
      this.selected = this.items[0];
    }
  }

  @Watch("selected")
  @Emit("selected")
  protected watchSelected(val: string): string {
    return val;
  }

  public addNewItem(value: Code): void {
    this.items = [value.type, ...this.items];
    this.selected = value.type;
  }
}
</script>
