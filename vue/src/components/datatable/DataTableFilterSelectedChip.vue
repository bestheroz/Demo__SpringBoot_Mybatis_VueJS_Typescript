<template>
  <div>
    <v-menu rounded="lg" v-model="isOpen" offset-y>
      <template #activator="{ on }">
        <v-chip
          :close="!filter.required"
          @click:close="emptySelectFilters"
          outlined
          :color="filter.required ? 'error' : 'primary'"
          v-on="on"
          class="pa-2"
          v-show="chipLabel"
          label
        >
          {{ chipLabel }}
        </v-chip>
      </template>
      <v-card
        v-if="isOpen && filtered.items.length > 0 && filtered.type !== 'text'"
      >
        <data-table-filter-items
          v-model="filter"
          from-chip
          @closed="isOpen = false"
          @change="$emit('change')"
        />
      </v-card>
    </v-menu>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator";
import type { Filter } from "@/definitions/types";
import DataTableFilterItems from "@/components/datatable/DataTableFilterItems.vue";

@Component({
  components: { DataTableFilterItems },
})
export default class extends Vue {
  @Prop({ required: true }) readonly filter!: Filter;

  isOpen = false;

  get filtered(): Filter {
    return {
      ...this.filter,
      items: this.filter.items.filter((i) => i.checked),
    };
  }

  get chipLabel(): string | undefined {
    if (this.filtered.items.length > 0) {
      if (["dateStartEndPicker", "text"].includes(this.filtered.type)) {
        return `${this.filtered.text} = ["${
          this.filtered.items[0].chipText || this.filtered.items[0].value
        }"]`;
      } else {
        return `${this.filtered.text} = ["${this.filtered.items
          .map((i) => i.text)
          /* eslint-disable quotes */
          .join('", "')}"]`;
      }
    }
    return undefined;
  }

  protected emptySelectFilters(): void {
    this.filter.items = this.filter.items.map((item) => {
      if (this.filter.type === "text") {
        return { ...item, checked: false, value: "" };
      } else {
        return { ...item, checked: false };
      }
    });
    this.$emit("change");
  }
}
</script>
