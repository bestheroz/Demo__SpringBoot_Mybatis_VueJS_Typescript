<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import axios from 'axios';
import { ApiDataResult } from '@/utils/apis';
import { TableMemberVO } from '@/common/types';
import envs from '@/constants/envs';
import { alertError } from '@/utils/alerts';

const pbkdf2 = require('pbkdf2');
const queryString = require('query-string');

@Component({
  name: 'Redirect',
})
export default class extends Vue {
  async mounted() {
    if (
      Vue.$storage.has('id') &&
      Vue.$storage.has('password') &&
      !Vue.$storage.has('accessToken')
    ) {
      try {
        const params = {
          id: Vue.$storage.get('id'),
          password: Vue.$storage.get('password'),
        };
        const response = await axios.get<ApiDataResult<TableMemberVO>>(
          `${envs.API_HOST}api/auth/me?`,
        );
        // ${queryString.stringify(params)
      } catch (e) {
        alertError(e);
        await this.$router.push('/login');
      }
    }
    // await this.$router.push('/admin/menu');
  }
}
</script>
