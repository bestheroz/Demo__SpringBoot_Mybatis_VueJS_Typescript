<template>
  <div>
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <code-group-list
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
          <code-list :parent-item="parentItem" />
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { TableCodeGroupVO } from '@/common/types';
import CodeList from '@/views/admin/code/components/CodeList.vue';
import CodeGroupList from '@/views/admin/code/components/CodeGroupList.vue';

@Component({
  name: 'Code',
  components: {
    CodeGroupList,
    CodeList,
  },
})
export default class extends Vue {
  parentItem: TableCodeGroupVO = {};
}
</script>
