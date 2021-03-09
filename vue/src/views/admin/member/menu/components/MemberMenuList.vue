<template>
  <div>
    <button-set
      :loading="saving"
      reload-button
      @click:reload="getList"
      save-button
      :save-disabled="!items || items.length === 0"
      @click:save="saveItems"
    />
    <v-card flat :loading="loading">
      <v-card-text>
        <v-row dense>
          <v-col cols="3">
            <v-list dense>
              <draggable
                tag="div"
                v-model="items"
                v-bind="dragOptions"
                handle=".drag-handle"
              >
                <transition-group type="transition" name="flip-list">
                  <v-list-item
                    :key="item.id"
                    v-for="item in items"
                    class="elevation-1"
                    dense
                  >
                    <v-list-item-icon>
                      <v-icon v-text="item.icon" />
                    </v-list-item-icon>
                    <v-list-item-content
                      style="display: inline-block"
                      class="py-0"
                    >
                      <v-icon color="primary" class="drag-handle">
                        mdi-sort
                      </v-icon>
                      {{ item.name }}
                    </v-list-item-content>
                  </v-list-item>
                </transition-group>
              </draggable>
            </v-list>
          </v-col>
          <v-col cols="9">
            <v-card-text class="py-0 elevation-1">
              <v-chip-group
                v-model="selected"
                column
                multiple
                active-class="accent"
                dense
              >
                <v-chip
                  v-for="item in menus"
                  :value="item.id"
                  :key="item.id"
                  filter
                  outlined
                >
                  <v-icon v-text="item.icon" v-if="item.icon" />
                  {{ item.name }}
                </v-chip>
              </v-chip-group>
            </v-card-text>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from "vue-property-decorator";
import type { TableMemberMenuEntity, TableMenuEntity } from "@/common/types";
import { getApi, postApi } from "@/utils/apis";
import ButtonSet from "@/components/speeddial/ButtonSet.vue";
import draggable from "vuedraggable";
import { defaultTableMemberMenuEntity } from "@/common/values";

@Component({
  name: "MemberMenuList",
  components: { ButtonSet, draggable },
})
export default class extends Vue {
  @Prop() readonly height!: number | string;
  @Prop({ required: true }) readonly authority!: number;
  menus: TableMenuEntity[] = [];
  items: TableMemberMenuEntity[] = [];
  selected: number[] = [];
  loading = false;
  saving = false;
  drag = false;

  get dragOptions(): { animation: number } {
    return {
      animation: 200,
    };
  }

  protected async created(): Promise<void> {
    const response = await getApi<TableMenuEntity[]>("admin/menus/");
    this.menus = response?.data || [];
    this.getList().then();
  }

  @Watch("selected")
  watchSelected(val: number[]): void {
    this.items = val.map((item) => {
      return {
        ...(this.menus.find((menu) => menu.id === item) ||
          defaultTableMemberMenuEntity()),
        authority: this.authority,
      };
    });
  }

  @Watch("authority")
  public async getList(): Promise<void> {
    this.items = [];
    this.loading = true;
    const response = await getApi<TableMemberMenuEntity[]>(
      `admin/member/menus/${this.authority}`,
    );
    this.items = response?.data || [];
    this.selected = this.items.map((item) => item.id || 0);
    this.loading = false;
  }

  protected async saveItems(): Promise<void> {
    let parentId = 0;
    this.saving = true;
    const response = await postApi<TableMemberMenuEntity[]>(
      `admin/member/menus/${this.authority}/save`,
      this.items.map((item, index) => {
        if (item.type === "G") {
          parentId = item.id || 0;
        }
        return {
          ...item,
          authority: this.authority,
          displayOrder: index + 1,
          parentId: item.type === "G" ? 0 : parentId,
        };
      }),
    );
    this.saving = false;
    if (response?.code?.startsWith("S")) {
      await this.$store.dispatch("initDrawers");
      this.getList().then();
    }
  }
}
</script>
