<template>
  <div>
    <v-list flat>
      <v-list-item
        v-for="item in filter.items"
        :key="item.value"
        :ripple="false"
      >
        <v-list-item-action class="mr-4" v-if="filter.type === 'checkbox'">
          <v-checkbox
            v-model="item.checked"
            :true-value="item.value"
            :disabled="item.checked && filter.single && filter.required"
            @click="onClickCheckbox(item)"
          />
        </v-list-item-action>
        <v-list-item-action
          class="mr-4"
          v-else-if="isDialogComponent && !fromChip"
        >
          <v-btn icon @click="searchDialog = true">
            <v-icon> mdi-dock-window </v-icon>
          </v-btn>
        </v-list-item-action>
        <v-list-item-content v-else-if="filter.type === 'text'" class="py-0">
          <v-text-field
            :value="item.value"
            :label="item.text"
            @input="(value) => onUpdateTextField(value, item)"
            :disabled="fromChip"
            clearable
          />
        </v-list-item-content>
        <v-list-item-content v-if="filter.type !== 'text'">
          <v-list-item-title
            v-text="item.text"
            :class="item.checked ? 'success--text' : undefined"
          />
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </div>
</template>

<script lang="ts">
import {
  Component,
  Emit,
  Prop,
  VModel,
  Vue,
  Watch,
} from "vue-property-decorator";
import { debounce } from "lodash-es";
import type { Filter, FilterItem } from "@/definitions/types";
import { FilterItemType } from "@/definitions/types";

@Component({
  components: {},
})
export default class extends Vue {
  @VModel({ required: true }) filter!: Filter;
  @Prop({ type: Boolean }) readonly fromChip!: boolean;

  searchDialog = false;

  get isDialogComponent(): boolean {
    return ["dateStartEndPicker"].includes(this.filter.type);
  }

  protected created(): void {
    if (this.fromChip && this.isDialogComponent) {
      this.searchDialog = true;
    }
  }

  @Watch("searchDialog")
  protected watchSearchDialog(val: boolean): void {
    if (!val) {
      this.$emit("closed");
    }
  }

  @Emit("change")
  protected onClickCheckbox(filterItem: FilterItem<FilterItemType>): void {
    if (this.filter.single) {
      this.filter = {
        ...this.filter,
        items: this.filter.items.map((item) => {
          return { ...item, checked: item.value === filterItem.value };
        }),
      };
    }
    this.$forceUpdate();
  }

  protected onUpdateTextField(
    value: string,
    item: FilterItem<FilterItemType>,
  ): void {
    this.fetchComments(value, item);
  }

  protected fetchComments = debounce(
    (value: string, item: FilterItem<FilterItemType>): void => {
      item.value = value;
      item.checked = false;
      // chip 에 바로 적용안되는 이슈가 있어서 추가함
      Vue.nextTick(() => {
        item.checked = !!item.value;
        item.chipText = item.value;
      });
    },
    300,
  );
}
</script>
