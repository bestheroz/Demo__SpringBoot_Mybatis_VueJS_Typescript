import { Validation } from 'vuelidate';
import * as v from 'vuelidate/lib/validators';

const touchMap = new WeakMap();

export const delayTouch = function($v: Validation) {
  $v.$reset();
  if (touchMap.has($v)) {
    clearTimeout(touchMap.get($v));
  }
  touchMap.set($v, setTimeout($v.$touch, 500));
};

const errorMessages = {
  required: `필수 항목입니다!`,
  minLength: `글자 수가 모자랍니다!`,
  maxLength: `글자 수가 초과되었습니다!`,
  email: `이메일 형식을 확인하세요.`,
  phone: `전화번호 형식을 확인하세요.`,
};

export const getVErrors = function($v: any): Array<string> {
  if (!$v.$dirty) return [];

  const errors = Object.entries(errorMessages)
    .filter(entry => $v[entry[0]] === false)
    .map(entry => entry[1]);
  return errors;
};

export import required = v.required;
export import minLength = v.minLength;
export import maxLength = v.maxLength;
export import numeric = v.numeric;
export import between = v.between;
export import email = v.email;
export const phone = (value: string = ``) => {
  return value.length == 0 || /^([0-9\\+\\-]*)$/.test(value);
};
