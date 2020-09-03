<template>
  <span>
    <v-btn class="mx-1" tile color="button-add" small>
      하위메뉴입력
    </v-btn>
    <v-btn class="mx-1" tile color="button-edit" small @click="editItem1">
      수정
    </v-btn>
    <v-btn class="mx-1" tile color="button-delete" small>
      삭제
    </v-btn>
  </span>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { TableMenuEntity } from '@/common/types';

@Component({
  name: 'RenderColumnAction',
  components: {},
})
export default class extends Vue {
  params: { value?: number | null; data: TableMenuEntity } = { value: null };

  editItem1() {
    this.$emit('editItem1');
  }
}

window.editMode = () => {
  this.editItem1();
};
</script>
