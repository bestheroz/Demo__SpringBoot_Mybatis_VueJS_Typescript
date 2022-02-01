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
      <v-list-item dense :key="item.menu.id" v-for="item in vModel">
        <v-row no-gutters>
          <v-col sm="12" lg="5">
            <v-list-item-icon>
              <v-icon v-text="item.menu.icon" style="top: 4px" />
            </v-list-item-icon>
            <v-list-item-title class="d-inline">
              <v-btn icon v-if="$store.getters.writeAuthority" small>
                <v-icon class="drag-handle"> mdi-sort </v-icon>
              </v-btn>
              {{ item.menu.name }}
            </v-list-item-title>
          </v-col>
          <v-col sm="12" lg="7" class="text-right">
            <v-btn-toggle
              v-model="item.authoritiesJson"
              active-class="primary"
              multiple
              v-if="item.menu.type !== MENU_TYPE.GROUP"
            >
              <v-btn
                :value="ROLE_AUTHORITY_TYPE.VIEW"
                disabled
                class="success-color"
              >
                <v-icon>mdi-eye</v-icon>
              </v-btn>

              <v-btn
                :value="ROLE_AUTHORITY_TYPE.WRITE"
                :disabled="
                  $store.getters.writeAuthority &&
                  roleId === $store.getters.roleId
                "
                :class="
                  item.authoritiesJson.includes(ROLE_AUTHORITY_TYPE.WRITE)
                    ? 'success-color'
                    : undefined
                "
              >
                <v-icon>mdi-content-save-outline</v-icon>
              </v-btn>

              <v-btn
                :value="ROLE_AUTHORITY_TYPE.DELETE"
                :disabled="
                  $store.getters.writeAuthority &&
                  roleId === $store.getters.roleId
                "
                :class="
                  item.authoritiesJson.includes(ROLE_AUTHORITY_TYPE.DELETE)
                    ? 'success-color'
                    : undefined
                "
              >
                <v-icon>mdi-delete-outline</v-icon>
              </v-btn>

              <v-btn
                :value="ROLE_AUTHORITY_TYPE.EXCEL"
                :disabled="
                  $store.getters.writeAuthority &&
                  roleId === $store.getters.roleId
                "
                :class="
                  item.authoritiesJson.includes(ROLE_AUTHORITY_TYPE.EXCEL)
                    ? 'success-color'
                    : undefined
                "
              >
                <v-icon>mdi-file-excel-outline</v-icon>
              </v-btn>
            </v-btn-toggle>
          </v-col>
          <v-col cols="12" v-if="item.menu.type === MENU_TYPE.GROUP">
            <role-menu-nested-draggable
              v-model="item.children"
              :role-id="roleId"
            />
          </v-col>
        </v-row>
      </v-list-item>
    </draggable>
  </v-list>
</template>
<script lang="ts">
import { RoleMenuMap } from "@/definitions/models";
import draggable from "vuedraggable";
import { MENU_TYPE, ROLE_AUTHORITY_TYPE } from "@/definitions/selections";
import {
  defineComponent,
  PropType,
  reactive,
  toRefs,
} from "@vue/composition-api";
import setupVModel from "@/composition/setupVModel";

export default defineComponent({
  name: "RoleMenuNestedDraggable",
  components: { draggable },
  props: {
    value: {
      type: Array as PropType<RoleMenuMap[]>,
      required: true,
    },
    roleId: {
      type: Number,
      required: true,
    },
  },
  setup(props, { emit }) {
    const vModel = setupVModel<RoleMenuMap[]>(props, emit);
    const state = reactive({
      ROLE_AUTHORITY_TYPE: ROLE_AUTHORITY_TYPE,
      MENU_TYPE: MENU_TYPE,
    });
    return { ...vModel, ...toRefs(state) };
  },
});
</script>
<style scoped lang="scss">
.dragArea {
  min-height: 30px;
  border: 1px dotted var(--v-primary-base);
  .v-item-group {
    .success-color {
      background-color: var(--v-primary-base) !important;
    }
  }
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
