<template>
  <div>
    <v-select
      v-model="value"
      :items="MENU_TYPE"
      outlined
      dense
      hide-details
      clearable
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { SelectItem } from '@/common/types';
import { getCodesApi } from '@/utils/apis';
import { IFloatingFilterParams } from 'ag-grid-community';

@Component({
  name: 'FloatingFilterMenuType',
  components: {},
})
export default class extends Vue {
  value: string | null = null;
  MENU_TYPE: SelectItem[] | null = null;
  params: IFloatingFilterParams = Object.create(null);

  async beforeMount() {
    this.MENU_TYPE = await getCodesApi(`MENU_TYPE`);
  }

  @Watch('value')
  watchValue(val: string) {
    this.params.parentFilterInstance((instance: any) => {
      instance.onFloatingFilterChanged('equals', val);
    });
  }

  onParentModelChanged(parentModel: any) {
    // note that the filter could be anything here, but our purposes we're assuming a greater than filter only,
    // so just read off the value and use that
    this.value = !parentModel ? null : parentModel.filter;
  }
}
</script>
