<template>
  <v-container id="login" class="fill-height" tag="section">
    <v-row justify="center">
      <v-slide-y-transition appear>
        <v-card :min-width="600" width="40%" class="elevation-12">
          <v-toolbar color="primary" dark flat>
            <v-toolbar-title>Login at {{ title }}</v-toolbar-title>
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
              <v-btn
                large
                color="success"
                text
                outlined
                rounded
                :loading="loading"
                @click="login"
              >
                <v-icon class="pr-2">mdi-login</v-icon>
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
import {
  alertAxiosError,
  ApiDataResult,
  axiosInstance,
  getVariableApi,
} from '@/utils/apis';
import { alertError, alertSuccess } from '@/utils/alerts';
import _ from 'lodash';

const pbkdf2 = require('pbkdf2');

@Component({ name: 'Login' })
export default class extends Vue {
  id: string | null = null;
  password: string | null = null;
  show1: boolean = false;
  title: string | null = null;
  loading: boolean = false;

  async mounted() {
    if (this.$route.query.login === 'need') {
      this.$toasted.error('로그인이 필요합니다.');
    }
    this.title = await getVariableApi('title');
    Vue.$storage.clear();
  }

  async login() {
    const inValid = await (this.$refs.observer as any).validate();
    if (!inValid) {
      return;
    }
    this.loading = true;
    try {
      const pbkdf2Password: string = pbkdf2
        .pbkdf2Sync(this.password, 'salt', 1, 32, 'sha512')
        .toString();
      const response = await axiosInstance.post<
        ApiDataResult<{
          id: string;
          password: string;
        }>
      >(`api/auth/login`, {
        id: this.id,
        password: pbkdf2Password,
      });
      if (!_.startsWith(response.data.code, `S`)) {
        alertError(response.data.message);
        return;
      }
      this.$store.commit('saveToken', response.data.data);
      this.$toasted.clear();
      await this.$router.push('/');
    } catch (e) {
      alertAxiosError(e);
    }
    this.loading = false;
  }
}
</script>
