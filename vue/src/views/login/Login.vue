<template>
  <v-content>
    <v-container class="fill-height" fluid>
      <v-row align="center" justify="center">
        <v-col cols="5">
          <v-card class="elevation-12">
            <v-toolbar color="deep-purple accent-4" dark flat>
              <v-toolbar-title>
                Login form ( Tester Account 1 / 1 )
              </v-toolbar-title>
              <div class="flex-grow-1"></div>
            </v-toolbar>
            <v-card-text>
              <v-form>
                <ValidationObserver ref="observer">
                  <ValidationProvider
                    name="ID"
                    ref="memberId"
                    rules="required"
                    v-slot="{ errors }"
                  >
                    <v-text-field
                      color="secondary"
                      label="ID..."
                      prepend-icon="mdi-identifier"
                      v-model="memberId"
                    />
                    <span class="red--text">{{ errors[0] }}</span>
                  </ValidationProvider>
                  <ValidationProvider
                    name="Password"
                    ref="memberPw"
                    rules="required"
                    v-slot="{ errors }"
                  >
                    <v-text-field
                      type="password"
                      color="secondary"
                      label="Password..."
                      prepend-icon="mdi-lock-outline"
                      v-model="memberPw"
                    />
                    <span class="red--text">{{ errors[0] }}</span>
                  </ValidationProvider>
                </ValidationObserver>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <div class="flex-grow-1"></div>
              <v-btn @click="login" color="deep-purple accent-4" dark
                >Login
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-content>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { ApiDataResult } from '@/utils/api';
import _ from 'lodash';
import { Member } from '@/views/manage/member/common/types';
import axios from 'axios';

const SHA512 = require('crypto-js/sha512');
let timerInterval;
@Component({
  components: {},
})
export default class Login extends Vue {
  $toasted: any;
  memberId: string = '';
  memberPw: string = '';

  mounted() {
    if (this.$route.query.need === 'login') {
      this.$toasted.error('로그인이 필요합니다.', { duration: 10000 });
    }
  }

  async login() {
    // @ts-ignore
    const inValid = await this.$refs.observer.validate();
    if (!inValid) {
      this.$swal({
        title: '입력 검증 후 다시 시도해주세요.',
        type: 'error',
      });
      return;
    }

    const response = await axios.post<ApiDataResult<Member>>(
      `${this.$store.state.host}sample/auth/login`,
      {
        memberId: this.memberId,
        memberPw: SHA512(this.memberPw).toString(),
      },
    );
    if (_.startsWith(response.data.responseCode, `S`)) {
      this.$store.commit('loginToken', response.data.responseData!.token);
      await this.$router.push('/');
    } else {
      this.$swal({ title: response.data.responseMessage, type: 'error' });
    }
  }
}
</script>

<style scoped></style>
