<template>
  <div>
    <v-select
      v-model="value"
      :items="[
        { value: 'true', text: '예' },
        { value: 'false', text: '아니요' },
      ]"
      outlined
      hide-details
      dense
      clearable
    />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { IFloatingFilterParams } from 'ag-grid-community';

@Component({
  name: 'FloatingFilterAuthority',
  components: {},
})
export default class extends Vue {
  value: string | null = null;
  params: IFloatingFilterParams = Object.create(null);

  @Watch('value')
  watchValue(val: boolean) {
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
