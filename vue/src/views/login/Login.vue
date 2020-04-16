<template>
  <v-container id="login" class="fill-height" tag="section">
    <v-row justify="center">
      <v-slide-y-transition appear>
        <v-card max-width="100%" width="600" class="elevation-12">
          <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Login at {{ appTitle }}</v-toolbar-title>
            <v-spacer />
            <template v-slot:heading>
              <div class="text-center">
                <h1 class="display-2 font-weight-bold">
                  <v-icon>mdi-lock-outline</v-icon>
                  Login
                </h1>
              </div>
            </template>
          </v-toolbar>

          <ValidationObserver ref="observer">
            <v-card-text class="text-center">
              <ValidationProvider
                name="ID"
                rules="required"
                v-slot="{ errors, valid }"
              >
                <v-text-field
                  v-model="id"
                  label="ID..."
                  prepend-icon="mdi-key"
                  @keyup.enter="login"
                  :error-messages="errors"
                  :success="valid"
                />
              </ValidationProvider>
              <ValidationProvider
                name="Password"
                rules="required"
                v-slot="{ errors, valid }"
              >
                <v-text-field
                  v-model="password"
                  label="Password..."
                  prepend-icon="mdi-lock-outline"
                  @keyup.enter="login"
                  :error-messages="errors"
                  :success="valid"
                  :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
                  :type="show1 ? 'text' : 'password'"
                  @click:append="show1 = !show1"
                />
              </ValidationProvider>
              <v-divider />
              <v-alert
                border="top"
                colored-border
                color="success"
                dense
                class="mb-0"
              >
                테스트 계정 ==> 1 / 1
              </v-alert>
              <v-divider />
              <v-btn
                large
                color="success"
                depressed
                text
                rounded
                @click="login"
              >
                Let's Go
              </v-btn>
            </v-card-text>
          </ValidationObserver>
        </v-card>
      </v-slide-y-transition>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { ApiDataResult, getVariableApi } from '@/utils/apis';
import _ from 'lodash';
import axios from 'axios';
import envs from '@/constants/envs';
import { TableMemberVO, TableMenuVO } from '@/common/types';
import { alertError } from '@/utils/alerts';

const SHA512 = require('crypto-js/sha512');

@Component({ name: 'Login' })
export default class extends Vue {
  id: string | null = null;
  password: string | null = null;
  show1: boolean = false;
  appTitle: string | null = null;

  async mounted() {
    if (this.$route.query.need === 'login') {
      this.$toasted.error('로그인이 필요합니다.');
    }
    this.appTitle = await getVariableApi('appTitle');
  }

  async login() {
    const inValid = await (this.$refs.observer as any).validate();
    if (!inValid) {
      return;
    }

    const response = await axios.post<ApiDataResult<TableMemberVO>>(
      `${envs.API_HOST}auth/login`,
      {
        id: this.id,
        password: SHA512(this.password).toString(),
      },
    );
    if (_.startsWith(response.data.code, `S`)) {
      this.$store.commit('loginToken', response.data.data);
      this.$toasted.clear();
      const response2 = await this.$store.state.axiosInstance.get('/menu');
      this.$storage.set('drawer', response2.data.data);
      await this.$router.push('/');
    } else {
      alertError(response.data.message);
    }
  }
}
</script>
