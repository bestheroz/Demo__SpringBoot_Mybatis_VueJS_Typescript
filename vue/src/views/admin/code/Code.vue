<template>
  <div>
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <grid-layout
            :layout.sync="layout"
            :col-num="12"
            :row-height="30"
            :is-draggable="false"
            :is-resizable="!$store.state.layout.lockLayout"
            :margin="[0, 0]"
          >
            <grid-item
              v-for="item in layout"
              :x="item.x"
              :y="item.y"
              :w="item.w"
              :h="item.h"
              :i="item.i"
              :key="item.i"
            >
              <code-group-list
                @select="
                  (item) => {
                    parentItem = (item && item[0]) || Object.create(null);
                  }
                "
                @updated="parentItem = Object.create(null)"
                v-if="item.i === 'code-group-list'"
                :height="height[0]"
              />
              <v-divider class="mr-1" />
              <code-list
                :parent-item="parentItem"
                v-if="item.i === 'code-list'"
                :height="height[1]"
              />
            </grid-item>
          </grid-layout>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { TableCodeGroupEntity } from '@/common/types';
import CodeList from '@/views/admin/code/components/CodeList.vue';
import CodeGroupList from '@/views/admin/code/components/CodeGroupList.vue';
import { getRealHeightOfLayout } from '@/utils/layout';

@Component({
  name: 'Code',
  components: {
    CodeGroupList,
    CodeList,
  },
})
export default class extends Vue {
  parentItem: TableCodeGroupEntity = Object.create(null);
  layout: {
    x: number;
    y: number;
    w: number;
    h: number;
    i: string;
  }[] = [
    { x: 0, y: 0, w: 12, h: 10, i: 'code-group-list' },
    { x: 0, y: 0, w: 12, h: 16, i: 'code-list' },
  ];

  get height(): number[] {
    return getRealHeightOfLayout(this.layout);
  }

  @Watch('$store.state.layout.menuId', { immediate: true })
  async watchLayoutMenuId(val: number) {
    if (val) {
      const layout = await this.$store.dispatch('getLayout');
      layout && (this.layout = layout);
    }
  }

  @Watch('$store.state.layout.lockLayout')
  watchLockLayout(val: boolean) {
    val && this.$store.dispatch('saveLayout', this.layout);
  }
}
</script>
