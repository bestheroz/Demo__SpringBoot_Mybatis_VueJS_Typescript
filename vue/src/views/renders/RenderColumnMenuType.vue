<template>
  <span>{{ params.value | getCodeText(MENU_TYPE) }}</span>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { SelectItem } from '@/common/types';
import { getCodesApi } from '@/utils/apis';

@Component({
  name: 'RenderColumnMenuType',
  components: {},
})
export default class extends Vue {
  params: { value?: string | null } = { value: null };
  MENU_TYPE: SelectItem[] | null = null;

  async mounted() {
    this.MENU_TYPE = await getCodesApi(`MENU_TYPE`);
  }
}
</script>
