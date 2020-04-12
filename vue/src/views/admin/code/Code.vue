<template>
  <div>
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <code-mst-list
            @select="
              (item) => {
                parentItem = (item && item[0]) || {};
              }
            "
            @updated="parentItem = {}"
          />
        </v-col>
        <v-divider class="mr-1" />
        <v-col cols="12">
          <code-det-list :parent-item="parentItem" />
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { TableSampleCodeMstVO } from '@/common/types';
import CodeDetList from '@/views/admin/code/components/CodeDetList.vue';
import CodeMstList from '@/views/admin/code/components/CodeMstList.vue';

@Component({
  name: 'Code',
  components: {
    CodeMstList,
    CodeDetList,
  },
})
export default class extends Vue {
  parentItem: TableSampleCodeMstVO = {};
}
</script>
