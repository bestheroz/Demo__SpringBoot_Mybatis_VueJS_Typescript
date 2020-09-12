<template>
  <div>
    <v-card>
      <v-row no-gutters>
        <v-col cols="12">
          <member-list :height="720" />
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import MemberList from '@/views/admin/member/components/MemberList.vue';

@Component({
  name: 'Member',
  components: { MemberList },
})
export default class extends Vue {}
</script>
