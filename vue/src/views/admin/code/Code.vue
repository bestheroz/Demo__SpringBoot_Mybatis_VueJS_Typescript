<template>
  <div>
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <code-group-wrapper :selected.sync="selected" height="23vh" />
        </v-col>
        <v-col cols="12">
          <v-divider />
        </v-col>
        <v-col cols="12">
          <code-wrapper :parent-item="item" height="36vh" />
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { TableCodeGroupEntity } from '@/common/types';
import CodeList from '@/views/admin/code/components/CodeList.vue';
import CodeGroupList from '@/views/admin/code/components/CodeGroupList.vue';
import CodeGroupWrapper from '@/views/admin/code/components/CodeGroupWrapper.vue';
import CodeWrapper from '@/views/admin/code/components/CodeWrapper.vue';

@Component({
  name: 'Code',
  components: {
    CodeWrapper,
    CodeGroupWrapper,
    CodeGroupList,
    CodeList,
  },
})
export default class extends Vue {
  selected: TableCodeGroupEntity[] = [];

  get item() {
    console.log(this.selected);
    return (
      (this.selected && this.selected.length > 0 && this.selected[0]) || []
    );
  }
}
</script>
