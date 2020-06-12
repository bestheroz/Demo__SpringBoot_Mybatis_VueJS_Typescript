<template>
  <div></div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import axios from 'axios';
import { ApiDataResult } from '@/utils/apis';
import { LoginVO, TableMemberVO } from '@/common/types';
import envs from '@/constants/envs';
import { alertError } from '@/utils/alerts';
import store from '@/store';

const pbkdf2 = require('pbkdf2');
const queryString = require('query-string');

@Component({
  name: 'Redirect',
})
export default class extends Vue {
  async mounted() {
    if (!(Vue.$storage.has('id') && Vue.$storage.has('password'))) {
      await this.$router.push('/login');
    }

    console.log(Vue.$storage.get('accessToken'));
    if (!Vue.$storage.has('accessToken')) {
      try {
        const params = {
          id: Vue.$storage.get('id'),
          password: Vue.$storage.get('password'),
        };
        console.log(params);
        const response = await axios.get<ApiDataResult<LoginVO>>(
          `${envs.API_HOST}api/auth/me?${queryString.stringify(params)}`,
        );
        store.commit('loginToken', response.data.data);
      } catch (e) {
        alertError(e);
        await store.commit('logout');
      }
    }
    await this.$router.push('/admin/menu');
  }
}
</script>
