<template>
  <v-content>
    <v-container class="fill-height" fluid>
      <v-row align="center" justify="center">
        <v-col cols="5">
          <v-card class="elevation-12">
            <v-toolbar color="deep-purple accent-4" dark flat>
              <v-toolbar-title>Login form</v-toolbar-title>
              <div class="flex-grow-1"></div>
            </v-toolbar>
            <v-card-text>
              <v-form>
                <v-text-field
                  label="Login"
                  prepend-icon="person"
                  type="text"
                  v-model="memberId"
                ></v-text-field>

                <v-text-field
                  label="Password"
                  prepend-icon="lock"
                  type="password"
                  v-model="memberPw"
                ></v-text-field>
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
import { postDataApi } from '@/utils/api';
import _ from 'lodash';
import { Member } from '@/views/manage/member/common/types';

const SHA512 = require('crypto-js/sha512');

@Component
export default class Login extends Vue {
  memberId: string = '';
  memberPw: string = '';

  async login() {
    const response = await postDataApi<Member>(
      `sample/auth/login`,
      {
        memberId: this.memberId,
        memberPw: SHA512(this.memberPw).toString(),
      },
      this,
    );
    if (_.startsWith(response.code, `S`)) {
      this.$store.commit('loginToken', response.data!.token);
      await this.$router.push('/');
    }
  }
}
</script>

<style scoped></style>
