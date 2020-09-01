<template>
  <div>
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <v-card-text class="py-1">
            <v-select
              v-model="authority"
              label="권한 선택"
              :loading="loading"
              :items="AUTHORITY"
              hide-details
            />
          </v-card-text>
        </v-col>
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
              <menu-authority-list
                :authority="authority"
                v-if="item.i === 'menu-authority-list'"
                :height="height[0]"
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
import { SelectItem } from '@/common/types';
import { getCodesApi } from '@/utils/apis';
import MenuAuthorityList from '@/views/admin/menuauthority/components/MenuAuthorityList.vue';
import { getRealHeightOfLayout } from '@/utils/layout';

@Component({
  name: 'MenuAuthority',
  components: {
    MenuAuthorityList,
  },
})
export default class extends Vue {
  authority: string | null = null;
  loading: boolean = false;
  AUTHORITY: SelectItem[] | null = null;
  layout: {
    x: number;
    y: number;
    w: number;
    h: number;
    i: string;
  }[] = [{ x: 0, y: 0, w: 12, h: 26, i: 'menu-authority-list' }];

  get height(): number[] {
    return getRealHeightOfLayout(this.layout);
  }

  mounted() {
    this.getCodeList();
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

  async getCodeList() {
    this.loading = true;
    const data: SelectItem[] = await getCodesApi('AUTHORITY');
    const user = await this.$store.dispatch('getUser');
    this.loading = false;
    this.AUTHORITY =
      data.filter(
        (value) => ![user.authority, 999].includes(parseInt(value.value)),
      ) || [];
  }
}
</script>
