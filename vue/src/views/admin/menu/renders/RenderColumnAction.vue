<template>
  <span>
    <v-btn class="mx-1" tile color="button-add" small> 하위메뉴입력 </v-btn>
    <v-btn class="mx-1" tile color="button-edit" small> 수정 </v-btn>
    <v-btn class="mx-1" tile color="button-delete" small> 삭제 </v-btn>
  </span>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { ICellRendererParams } from 'ag-grid-community';

@Component({
  name: 'RenderColumnAction',
  components: {},
})
export default class extends Vue {
  params: ICellRendererParams = Object.create(null);
}
</script>
