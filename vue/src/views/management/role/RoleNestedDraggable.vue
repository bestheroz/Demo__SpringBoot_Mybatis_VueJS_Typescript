<template>
  <v-list>
    <draggable
      class="dragArea"
      tag="div"
      :list="roles"
      :group="{ name: 'g1' }"
      v-bind="dragOptions"
      handle=".drag-handle"
    >
      <v-list-item
        :key="role.id"
        v-for="role in roles"
        :class="role.id === 1 ? 'd-none' : undefined"
      >
        <v-list-item-title class="d-inline" v-if="role.id !== 1">
          <v-btn icon v-if="$store.getters.writeAuthority">
            <v-icon class="drag-handle"> mdi-sort </v-icon>
          </v-btn>
          <a
            class="text--anchor"
            @click="$emit('click:edit', role)"
            v-text="role.name"
          />
          <role-nested-draggable
            v-model="role.children"
            @click:edit="(item) => $emit('click:edit', item)"
            @click:delete="(item) => $emit('click:delete', item)"
          />
        </v-list-item-title>
      </v-list-item>
    </draggable>
  </v-list>
</template>
<script lang="ts">
import { Component, VModel, Vue } from "vue-property-decorator";
import { Role } from "@/definitions/models";
import draggable from "vuedraggable";

@Component({
  components: {
    RoleNestedDraggable: () =>
      import("@/views/management/role/RoleNestedDraggable.vue"),
    draggable,
  },
})
export default class extends Vue {
  @VModel({ required: true, type: Array, default: () => [] })
  readonly roles!: Role[];

  get dragOptions(): { animation: number } {
    return {
      animation: 200,
    };
  }
}
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
