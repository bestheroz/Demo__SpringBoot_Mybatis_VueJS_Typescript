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
                <ValidationObserver ref="form" v-slot="{ validate }">
                  <ValidationProvider
                    name="ID"
                    rules="required"
                    v-slot="{ errors }"
                  >
                    <v-text-field
                      color="secondary"
                      label="ID..."
                      prepend-icon="mdi-identifier"
                      v-model="memberId"
                    />
                    <div class="error--text">{{ errors[0] }}</div>
                  </ValidationProvider>
                  <ValidationProvider
                    name="password"
                    rules="required"
                    v-slot="{ errors }"
                  >
                    <v-text-field
                      color="secondary"
                      label="Password..."
                      prepend-icon="mdi-lock-outline"
                      v-model="memberPw"
                    />
                    <div class="error--text">{{ errors[0] }}</div>
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
import { postDataApi } from '@/utils/api';
import _ from 'lodash';
import { Member } from '@/views/manage/member/common/types';
import { ValidationObserver, ValidationProvider } from 'vee-validate';

const SHA512 = require('crypto-js/sha512');

@Component({ components: { ValidationProvider, ValidationObserver } })
export default class Login extends Vue {
  memberId: string = '';
  memberPw: string = '';

  mounted() {
    console.info(this.$route.query.need);
    if (
      this.$route.query.need !== undefined &&
      this.$route.query.need !== '' &&
      typeof this.$route.query.need === 'string'
    ) {
      this.$toast.warning(this.$route.query.need);
    }
  }

  async login() {
    this.$refs.form.validate();
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
