<template>
  <v-list>
    <draggable
      class="dragArea"
      tag="div"
      :list="vModel"
      :group="{ name: 'g1' }"
      :animation="200"
      handle=".drag-handle"
    >
      <v-list-item dense :key="menu.id" v-for="menu in vModel">
        <v-list-item-icon>
          <v-icon v-text="menu.icon" />
        </v-list-item-icon>
        <v-list-item-title class="d-inline">
          <v-row no-gutters>
            <v-col cols="2">
              <v-btn icon v-if="$store.getters.writeAuthority">
                <v-icon class="drag-handle"> mdi-sort </v-icon>
              </v-btn>
              <a
                class="text--anchor"
                @click="$emit('click:edit', menu)"
                v-text="menu.name"
              />
            </v-col>
            <v-col cols="10" class="pt-1">
              <span v-text="menu.url" />
            </v-col>
          </v-row>
          <menu-nested-draggable
            v-if="menu.type === MENU_TYPE.GROUP"
            v-model="menu.children"
            @click:edit="(item) => $emit('click:edit', item)"
            @click:delete="(item) => $emit('click:delete', item)"
          />
        </v-list-item-title>
        <v-list-item-action
          style="display: inline-block"
          class="my-0"
          v-if="$store.getters.deleteAuthority"
        >
          <div class="actions">
            <v-btn icon @click="$emit('click:delete', menu)">
              <v-icon color="error"> mdi-delete-outline </v-icon>
            </v-btn>
          </div>
        </v-list-item-action>
      </v-list-item>
    </draggable>
  </v-list>
</template>
<script lang="ts">
import { Menu } from "@/definitions/models";
import draggable from "vuedraggable";
import { MENU_TYPE } from "@/definitions/selections";
import {
  defineComponent,
  PropType,
  reactive,
  toRefs,
} from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  name: "MenuNestedDraggable",
  components: {
    draggable,
  },
  props: {
    value: {
      type: Array as PropType<Menu[]>,
      required: true,
      default: () => [],
    },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<Menu[]>(props, emit);
    const state = reactive({ MENU_TYPE: MENU_TYPE });
    return { ...vModel, ...toRefs(state) };
  },
});
</script>
<style scoped lang="scss">
.dragArea {
  min-height: 30px;
  border: 1px dotted var(--v-primary-base);
  .dragArea {
    min-height: 30px;
    border: 1px dotted var(--v-warning-base);
    .dragArea {
      min-height: 30px;
      border: 1px dotted var(--v-info-base);
      .dragArea {
        min-height: 30px;
        border: 1px dotted var(--v-error-base);
        .dragArea {
          min-height: 30px;
          border: 1px dotted var(--v-success-base);
          .dragArea {
            min-height: 30px;
            border: 1px dotted var(--v-secondary-base);
          }
        }
      }
    }
  }
}
</style>
