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
                  :error-messages="getVErrors($v.memberId)"
                  @blur="delayTouch($v.memberId)"
                  @input="delayTouch($v.memberId)"
                  color="secondary"
                  label="ID..."
                  prepend-icon="mdi-identifier"
                  v-model="memberId"
                />
                <v-text-field
                  :error-messages="getVErrors($v.memberPw)"
                  @blur="delayTouch($v.memberPw)"
                  @input="delayTouch($v.memberPw)"
                  color="secondary"
                  label="Password..."
                  prepend-icon="mdi-lock-outline"
                  v-model="memberPw"
                />
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
import {
  delayTouch,
  getVErrors,
  maxLength,
  required,
} from '@/utils/validation-helper';
import { Validation } from 'vuelidate';

const SHA512 = require('crypto-js/sha512');

@Component({
  components: {},
  validations: {
    memberId: {
      required,
      maxLength: maxLength(20),
    },
    memberPw: {
      required,
      maxLength: maxLength(20),
    },
  },
})
export default class Login extends Vue {
  readonly delayTouch: typeof delayTouch = delayTouch;
  readonly getVErrors: typeof getVErrors = getVErrors;
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
    const $vForm: Validation = this.$v.item as Validation;
    $vForm.$touch();
    const valid = !$vForm.$pending && !$vForm.$error;
    if (!valid) {
      this.$toast.warning('입력 검증 후 다시 시도해주세요.');
      return;
    }

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
