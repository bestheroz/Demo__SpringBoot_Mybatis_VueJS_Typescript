<template>
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
            <menu-list v-if="item.i === 'menu-list'" :height="height[0]" />
          </grid-item>
        </grid-layout>
      </v-col>
    </v-row>
  </v-card>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import MenuList from '@/views/admin/menu/components/MenuList.vue';
import { getRealHeightOfLayout } from '@/utils/layout';

@Component({
  name: 'Menu',
  components: {
    MenuList,
  },
})
export default class extends Vue {
  layout: {
    x: number;
    y: number;
    w: number;
    h: number;
    i: string;
  }[] = [{ x: 0, y: 0, w: 12, h: 26, i: 'menu-list' }];

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
