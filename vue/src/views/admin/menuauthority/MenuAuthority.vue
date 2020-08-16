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
          <menu-authority-list :authority="authority" />
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { SelectItem } from '@/common/types';
import { getCodeListApi } from '@/utils/apis';
import MenuAuthorityList from '@/views/admin/menuauthority/components/MenuAuthorityList.vue';

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

  mounted() {
    this.getCodeList();
  }

  async getCodeList() {
    this.loading = true;
    const data: SelectItem[] = await getCodeListApi('AUTHORITY');
    this.loading = false;
    this.AUTHORITY =
      data.filter(
        (value) =>
          ![window.localStorage.getItem('authority'), 999].includes(
            parseInt(value.value),
          ),
      ) || [];
  }
}
</script>
